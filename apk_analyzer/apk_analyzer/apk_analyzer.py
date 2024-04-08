import time
import asyncio

from androguard.core.apk import APK

from apk_analyzer.dto import AnalysisResult, StartAnalysis
from .dto import AnalysisStatus
from .emulators import EmulationPool
from .config import EmulatorConfig
from .repositories import AnalyzerMongo, ManifestInfo
from .frida.hooks import get_hooks, HookHandler


class ApkAnalyzer:
    def __init__(self, logger=None):
        self.logger = logger
        self.emulation_pool = EmulationPool(EmulatorConfig)
        self.analyzer_db = AnalyzerMongo()
        self.emulation_pool.start_emulators()
        self.emulation_pool.setup_frida_all()

    def analyze(self, filepath: str, md5: str) -> AnalysisResult:
        self.logger.info(f"starting to analyze sample: {filepath}")

        apk = APK(filepath.encode('utf-8'))
        self.analyzer_db.set_manifest_info(md5, ManifestInfo(
            pkn=apk.package,
            permissions=apk.get_permissions()
        ))

        emulator = self.emulation_pool.get_available_emulator(apk)
        if not emulator:
            pass  # TODO no good emulator found, update stats to failed or sm
        emulator.lock()
        self.logger.info("found emulator: {}".format(emulator.avd))

        emulator.install_sample(filepath)
        self.logger.info("installed sample on emulator")
        self.analyzer_db.installed(md5)

        hooks = get_hooks()
        hook_handler = HookHandler(
            md5=md5
        )
        self.logger.info("loaded hooks")

        emulator.instrument(apk, hooks, hook_handler)
        self.analyzer_db.started(md5)
        self.logger.info("started application")

        emulator.fool_around()

        emulator.cancel_instrumentation()
        emulator.release()
        emulator.uninstall(apk.package)
        self.logger.info("{} finished analysis".format(md5))
        return

    async def get_status(self, md5: str) -> AnalysisStatus | None:
        db_entry = self.analyzer_db.get_entry(md5)
        return AnalysisStatus(
            md5=db_entry.md5,
            installed=db_entry.installed,
            started=db_entry.started,
            manifest=db_entry.manifest.to_dict()
        )
