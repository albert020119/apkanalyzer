import subprocess

from ..config import ADBConfig


class ADB:
    def __init__(self, adb_config: ADBConfig, port=None):
        self.adb_path = adb_config.adb_path
        self.port = port

    def cmd(self, commands: list):
        command = [self.adb_path, '-s', 'emulator-' + self.port]
        command.extend(commands)
        process = subprocess.run(command, shell=True, close_fds=True, capture_output=True)
        return process.stdout, process.stderr

    def shell(self, commands: list):
        command = [self.adb_path, '-s', 'emulator-' + self.port, 'shell']
        command.extend(commands)
        process = subprocess.run(command, shell=False, close_fds=True, capture_output=True, timeout=5)
        return process.stdout, process.stderr
