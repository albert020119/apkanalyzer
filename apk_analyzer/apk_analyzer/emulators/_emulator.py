import subprocess
import time

import frida


from threading import Thread

from .adb import ADB
from ..config import ADBConfig
from ..frida.utils import get_frida_latest, download_frida_server, install_frida_server, start_frida_server, \
    restart_frida
from ..frida.hooks import Hook, HookHandler
from ..jester import Jester
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
        self.frida_session = None
        self.clown = None

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

    def uninstall(self, pkn: str):
        output, _ = self.adb.cmd(['uninstall', pkn])
        return output.decode('utf-8')

    def send_random_sms(self):
        output, _ = self.adb.cmd(["emu", "sms", "send", "12345", "asd"])
        return output.decode('utf-8')

    @property
    def is_running(self):
        output, _ = self.adb.shell(["getprop", "init.svc.bootanim"])
        is_running = True if output.decode('utf-8').strip() == 'stopped' else False
        return is_running

    def connect_to_viewclient(self):
        self.device, self.serialno = ViewClient.connectToDeviceOrExit(serialno="emulator-{}".format(self.port))
        self.viewclient = ViewClient(device=self.device, serialno=self.serialno)

    def get_sdk(self):
        output, _ = self.adb.shell(["getprop", "ro.build.version.sdk"])
        rez = output.decode('utf-8')
        if rez == '':
            return 0
        return int(output.decode('utf-8').strip())

    def available(self):
        return self.available

    def reboot(self):
        self.adb.cmd(["-e", "reboot"])

    def lock(self):
        self.available = False

    def release(self):
        self.available = True

    def setup_frida(self):
        version, cpu_arch = get_frida_latest(self.adb)
        download_frida_server(version=version, cpu_arch=cpu_arch)
        install_frida_server(self.adb, cpu_arch=cpu_arch)

    def instrument(self, apk, hooks: list[Hook], hook_handler: HookHandler):
        restart_frida(self.adb)
        device_manager = frida.get_device_manager()
        frida_device = None
        for device in device_manager.enumerate_devices():
            if self.port in device.name:
                frida_device = device
        if not frida_device:
            return
        pid = frida_device.spawn(apk.package)
        self.frida_session = frida_device.attach(pid)

        for hook in hooks:
            script = self.frida_session.create_script(hook.script)
            script.on('message', hook_handler.on_hook)
            script.load()
            print("loaded hook: {}".format(hook.name))
        time.sleep(0.5)
        frida_device.resume(pid)

    def fool_around(self, apk, time_to_run: int):
        jester = Jester(self, self.viewclient, apk, time_to_run=time_to_run)
        self.clown = Thread(target=jester.start)
        self.clown.start()

    def wait_for_clown(self):
        self.clown.join()

    def cancel_instrumentation(self):
        self.frida_session.detach()
        self.frida_session = None
