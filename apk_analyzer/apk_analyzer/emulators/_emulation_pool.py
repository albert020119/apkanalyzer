import time

from ._emulator import Emulator
from ..config import EmulatorConfig


class EmulationPool:
    def __init__(self, emulation_config: EmulatorConfig):
        self.emulators = []
        for (avd_path, port) in emulation_config.images:
            emu = Emulator(
                emulator_path=emulation_config.emulator_path,
                avd=avd_path,
                port=port
            )
            self.emulators.append(emu)

    def get_available_emulator(self, apk) -> Emulator | None:
        for emulator in self.emulators:
            if emulator.get_sdk() > int(apk.get_target_sdk_version()):
                return emulator
        return None

    def start_emulators(self):
        for emulator in self.emulators:
            emulator.start_emulator()
        time.sleep(15)
        for emulator in self.emulators:
            print("{} is running: {}".format(emulator.avd, emulator.is_running))


