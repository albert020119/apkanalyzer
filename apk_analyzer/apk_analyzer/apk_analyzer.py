import time
import asyncio

from androguard.core.apk import APK

from apk_analyzer.dto import AnalysisResult, StartAnalysis
from .dto import AnalysisStatus
from .dto.analysis_status import PermissionStatus, ManifestStatus, HookStatus
from .emulators import EmulationPool
from .config import EmulatorConfig, permissions_danger, permissions_descriptions, dynamic_info
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
        self.analyzer_db.set_manifest_info(md5, ManifestInfo(
            pkn="",
            label="",
            permissions=[]
        ))
        apk_parsing = time.time()
        apk = APK(filepath.encode('utf-8'))
        self.logger.info("apk parsing for md5 {} took {}".format(md5, time.time() - apk_parsing))
        self.analyzer_db.set_manifest_info(md5, ManifestInfo(
            pkn=apk.package,
            label=apk.get_app_name(),
            permissions=apk.get_permissions()
        ))

        emulator = self.emulation_pool.get_available_emulator(apk)
        if not emulator:
            self.logger.info("no open emulator found")
            return
        emulator.lock()
        self.logger.info("found emulator: {}".format(emulator.avd))

        emulator.install_sample(filepath)
        self.logger.info("installed sample on emulator")
        self.analyzer_db.installed(md5)

        hooks = get_hooks()
        hook_handler = HookHandler(
            md5=md5,
            logger=self.logger,
            repo=self.analyzer_db
        )
        self.logger.info("loaded hooks")

        emulator.connect_to_viewclient()
        emulator.fool_around(apk, time_to_run=25)
        emulator.instrument(apk, hooks, hook_handler)
        self.analyzer_db.started(md5)
        self.logger.info("started application")

        emulator.wait_for_clown()
        emulator.cancel_instrumentation()
        emulator.release()
        emulator.uninstall(apk.package)
        self.logger.info("{} finished analysis".format(md5))
        self.analyzer_db.finished(md5)
        return

    async def get_status(self, md5: str) -> AnalysisStatus | None:
        db_entry = self.analyzer_db.get_entry(md5)
        if not db_entry:
            return None
        hooks = []
        for hook in db_entry.hooks:
            present = False
            for added in hooks:
                if hook.get("type") == added.type and hook.get("code") == added.code and hook.get(
                        "method") == added.name:
                    present = True
            if present:
                continue
            hooks.append(HookStatus(
                code=hook.get("code"),
                type=hook.get("type"),
                name=hook.get("method"),
                description=dynamic_info.get(hook.get("type", {}), {}).get(hook.get("method"))
            ))

        return AnalysisStatus(
            md5=db_entry.md5,
            found_emulator=db_entry.found_emulator,
            installed=db_entry.installed,
            started=db_entry.started,
            finished=db_entry.finished,
            manifest=ManifestStatus(
                pkn=db_entry.manifest.pkn,
                label=db_entry.manifest.label,
                permissions=[PermissionStatus(
                    name=perm,
                    code=permissions_danger.get(perm, 0),
                    description=permissions_descriptions.get(perm, "")
                ) for perm in db_entry.manifest.permissions]
            ),
            analysis=hooks
        )
