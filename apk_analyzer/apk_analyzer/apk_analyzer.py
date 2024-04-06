import time
import asyncio

from androguard.misc import AnalyzeAPK

from apk_analyzer.dto import AnalysisResult, StartAnalysis, AnalysisStatus
from apk_analyzer.utils import download_file, calc_md5
from .emulators import EmulationPool
from .config import EmulatorConfig
from .dto import AnalysisStatus
from .frida.hooks import get_hooks


class ApkAnalyzer:
    def __init__(self, logger=None):
        self.logger = logger
        self.emulation_pool = EmulationPool(EmulatorConfig)
        self.emulation_pool.start_emulators()
        self.emulation_pool.setup_frida_all()
        self.statuses: dict[str, AnalysisStatus | None] = {}
        pass

    def start_analysis(self, file) -> StartAnalysis:
        downloaded_file_path = download_file(file)
        checksum = calc_md5(downloaded_file_path)
        self.statuses[checksum] = AnalysisStatus.get_initial(downloaded_file_path, checksum)
        asyncio.create_task(self.analyze(downloaded_file_path, checksum))
        return StartAnalysis(md5=checksum, filename=file.filename)

    async def analyze(self, filepath: str, md5: str) -> AnalysisResult:
        self.logger.info(f"starting to analyze sample: {filepath}")
        apk, _, _ = AnalyzeAPK(filepath.encode('utf-8'))
        emulator = self.emulation_pool.get_available_emulator(apk)
        if not emulator:
            pass  # TODO no good emulator found, update stats to failed or sm
        self.logger.info("found emulator: {}".format(emulator.avd))
        self.statuses.get(md5).found_emulator = True
        emulator.lock()
        emulator.install_sample(filepath)
        self.logger.info("installed sample on emulator")
        self.statuses.get(md5).installed = True
        hooks = get_hooks()
        self.logger.info("loaded hooks")
        emulator.instrument(apk, hooks)
        self.statuses.get(md5).started = True
        emulator.fool_around()
        emulator.release()
        return

    async def get_status(self, md5: str) -> AnalysisStatus | None:
        return self.statuses.get(md5)
