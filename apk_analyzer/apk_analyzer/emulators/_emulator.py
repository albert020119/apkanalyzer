import subprocess

from ._adb import ADB
from ..config import ADBConfig


class Emulator:
    def __init__(self, emulator_path: str, avd: str):
        self.emulator_path = emulator_path
        self.avd = avd
        self.adb = ADB(ADBConfig)

    def start_emulator(self):
        self.run_command(self.emulator_path, self.avd)

    @staticmethod
    def run_command(executable, *args):
        command = [executable]
        command.extend(args)
        subprocess.Popen(command, shell=True, close_fds=True, stdout=None)

    def is_running(self):
        pass

    def get_sdk(self):
        pass