import time
import asyncio

from androguard.misc import AnalyzeAPK

from apk_analyzer.dto import AnalysisResult, StartAnalysis, AnalysisStatus
from apk_analyzer.utils import download_file, calc_md5
from .emulators import EmulationPool
from .config import EmulatorConfig
from .dto import AnalysisStatus


class ApkAnalyzer:
    def __init__(self, logger=None):
        self.logger = logger
        self.emulation_pool = EmulationPool(EmulatorConfig)
        self.emulation_pool.start_emulators()
        self.stats = []
        pass

    def start_analysis(self, file) -> StartAnalysis:
        downloaded_file_path = download_file(file)
        checksum = calc_md5(downloaded_file_path)
        asyncio.create_task(self.analyze(downloaded_file_path))
        return StartAnalysis(md5=checksum, filename=file.filename)

    async def analyze(self, filepath: str) -> AnalysisResult:
        self.logger.info(f"starting to analyze sample: {filepath}")
        apk, _, _ = AnalyzeAPK(filepath, raw=True)
        emulator = self.emulation_pool.get_available_emulator(apk)
        if not emulator:
            pass  # TODO no good emulator found, update stats to failed or sm
        pass

    async def get_status(self, md5: str) -> AnalysisStatus:
        pass
