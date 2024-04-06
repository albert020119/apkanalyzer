import subprocess

from .adb import ADB
from ..config import ADBConfig
from ..frida.utils import get_frida_latest, download_frida_server, install_frida_server, start_frida_server
from com.dtmilano.android.viewclient import ViewClient


class Emulator:
    def __init__(self, emulator_path: str, avd: str, port: str):
        self.emulator_path = emulator_path
        self.avd = avd
        self.adb = ADB(ADBConfig, port=port)
        self.viewclient = None
        self.device = None
        self.serialno = None
        self.port = port
        self.available = True

    def start_emulator(self):
        self.run_command(self.emulator_path, '-port', self.port, self.avd)

    @staticmethod
    def run_command(executable, *args):
        command = [executable]
        command.extend(args)
        subprocess.Popen(command, shell=True, close_fds=True, stdout=None, start_new_session=True)

    def install_sample(self, path):
        output, _ = self.adb.cmd(["install", path])
        return output.decode('utf-8')

    @property
    def is_running(self):
        self.device, self.serialno = ViewClient.connectToDeviceOrExit(serialno="emulator-{}".format(self.port))
        self.viewclient = ViewClient(device=self.device, serialno=self.serialno)
        output, _ = self.adb.shell(["getprop", "init.svc.bootanim"])
        is_running = True if output.decode('utf-8').strip() == 'stopped' else False
        return is_running

    def get_sdk(self):
        output, _ = self.adb.shell(["getprop", "ro.build.version.sdk"])
        rez = output.decode('utf-8')
        if rez == '':
            return 0
        return int(output.decode('utf-8').strip())

    def available(self):
        return self.available

    def lock(self):
        self.available = False

    def release(self):
        self.available = True

    def setup_frida(self):
        version, cpu_arch = get_frida_latest(self.adb)
        download_frida_server(version=version, cpu_arch=cpu_arch)
        install_frida_server(self.adb, cpu_arch=cpu_arch)
        output = start_frida_server(self.adb)
        print(output)
