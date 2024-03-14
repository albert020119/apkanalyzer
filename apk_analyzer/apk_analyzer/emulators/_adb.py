import subprocess

from ..config import ADBConfig


class ADB:
    def __init__(self, adb_config: ADBConfig, port=None):
        self.adb_path = adb_config.adb_path
        self.port = port

    def cmd(self, commands: list):
        command = [self.adb_path, self.port]
        command.extend(commands)
        subprocess.Popen(command, shell=True, close_fds=True, stdout=None)

    def shell(self, commands: list):
        command = [self.adb_path, self.port, 'shell']
        command.extend(commands)
        subprocess.Popen(command, shell=True, close_fds=True, stdout=None)

