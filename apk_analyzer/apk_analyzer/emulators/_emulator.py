import subprocess

from ._adb import ADB
from ..config import ADBConfig


class Emulator:
    def __init__(self, emulator_path: str, avd: str, port: str):
        self.emulator_path = emulator_path
        self.avd = avd
        self.adb = ADB(ADBConfig, port=port)
        self.port = port
        self.available = True

    def start_emulator(self):
        self.run_command(self.emulator_path, '-port', self.port, self.avd)

    @staticmethod
    def run_command(executable, *args):
        command = [executable]
        command.extend(args)
        subprocess.Popen(command, shell=True, close_fds=True, stdout=None, start_new_session=True)

    @property
    def is_running(self):
        output, _ = self.adb.shell(["getprop", "init.svc.bootanim"])
        is_running = True if output.decode('utf-8').strip() == 'stopped' else False
        return is_running

    def get_sdk(self):
        output, _ = self.adb.shell(["getprop", "ro.build.version.sdk"])
        # TODO fix this shit
        rez = output.decode('utf-8')
        if rez == '':
            return 0
        return int(output.decode('utf-8').strip())

    def available(self):
        return self.available
