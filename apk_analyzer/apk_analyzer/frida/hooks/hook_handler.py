from .hook import HookEvent
from ...repositories import AnalyzerMongo


class HookHandler:

    def __init__(self, md5: str, logger, repo: AnalyzerMongo):
        self.md5 = md5
        self.logger = logger
        self.repo = repo

    def on_hook(self, message: dict, data: str):
        self.logger.info(message)
        payload = message.get('payload')
        if payload:
            he = HookEvent.from_str(payload)
            self.repo.add_event(md5=self.md5, hook_event=he)
            self.logger.info("wrote to db")
