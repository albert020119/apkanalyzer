import lzma
import os
import subprocess

import requests

from bs4 import BeautifulSoup
from ..emulators.adb import ADB

RESET = "\033[0m"
BLACK = "\033[30m"
RED = "\033[31m"
GREEN = "\033[32m"
YELLOW = "\033[33m"
BLUE = "\033[34m"
MAGENTA = "\033[35m"
CYAN = "\033[36m"
WHITE = "\033[37m"


def get_frida_latest(device: ADB):
    url = "https://github.com/frida/frida/releases/latest"
    r = requests.get(url)
    soup = BeautifulSoup(r.content, "html.parser")
    version = soup.find("h1", class_="d-inline mr-3").text.split()[1]
    cpu_arch = device.shell(["getprop", "ro.product.cpu.abi"])[0].decode('utf-8').strip()
    if "arm64" in cpu_arch:
        cpu_arch = device.shell(["getprop", "ro.product.cpu.abi"])[0].decode('utf-8').strip().split('-')[0]
    return version, cpu_arch


def download_frida_server(version, cpu_arch):
    url = f"https://github.com/frida/frida/releases/download/{version}/frida-server-{version}-android-{cpu_arch}.xz"
    r = requests.get(url, stream=True)
    print("[~] Downloading the latest Frida Server...")
    with open(f"frida-{cpu_arch}-android.xz", "wb") as f:
        for chunk in r.iter_content(chunk_size=1024):
            f.write(chunk)
    print(f"{GREEN}[+] Done. Downloaded at {os.path.join(os.getcwd(), f.name)}\n{RESET}")
    return True


def install_frida_server(device: ADB, cpu_arch):
    try:
        with lzma.open(f"frida-{cpu_arch}-android.xz") as xz_file:
            with open(f"frida-server-{cpu_arch}", "wb") as f:
                for chunk in xz_file:
                    f.write(chunk)

        print("[~] Pushing frida-server to the device...")
        device.cmd(["push", f"frida-server-{cpu_arch}", "/data/local/tmp/frida-server"])

        print("[~] Settings permissions for frida-server...")
        device.shell(["chmod", "700", "/data/local/tmp/frida-server"])

        print("[~] Cleaning up the downloads...")
        os.remove(f"frida-server-{cpu_arch}")
        os.remove(f"frida-{cpu_arch}-android.xz")

        print(f"\n{GREEN}[+] Done. Frida server installed at /data/local/tmp/frida-server\n{RESET}")
        return True

    except Exception as e:
        print(f"{RED}[!] Error installing frida-server:\n{e}\n{RESET}")
        return False


def start_frida_server(device):
    out, err = device.cmd(["root"])
    print(out)
    start_cmd = ["/data/local/tmp/frida-server", "&"]
    try:
        device.shell(start_cmd)
    except subprocess.TimeoutExpired:
        pass
    get_pid_cmd = ["ps", "-A", "|", "grep", "frida"]
    output, _ = device.shell(get_pid_cmd)
    return output.decode('utf-8')
