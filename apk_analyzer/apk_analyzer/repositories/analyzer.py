from ..config import DBConfig
from .sample_entry import SampleEntry, ManifestInfo
from pymongo import MongoClient
from pymongo.server_api import ServerApi

from ..frida.hooks.hook import HookEvent


class AnalyzerMongo:
    def __init__(self):
        self.client = MongoClient(DBConfig.uri, server_api=ServerApi('1'))
        self.db = self.client.get_database("analyzer")
        self.coll = self.db.get_collection("samples")

    def add_new(self, md5: str, path: str):
        to_insert = SampleEntry(
            md5=md5,
            file_path=path

        ).to_dict()
        self.coll.insert_one(to_insert)

    def get_entry(self, md5) -> SampleEntry | None:
        entry = self.coll.find_one({"md5": md5})
        if entry:
            return SampleEntry.from_dict(entry)
        else:
            return None

    def installed(self, md5: str):
        self.coll.update_one({"md5": md5}, {"$set": {"installed": True}})

    def started(self, md5: str):
        self.coll.update_one({"md5": md5}, {"$set": {"started": True}})

    def set_manifest_info(self, md5: str, manifest: ManifestInfo):
        self.coll.update_one({"md5": md5}, {"$set": {"manifest": manifest.to_dict()}})

    def add_event(self, md5: str, hook_event: HookEvent):
        self.coll.update_one({"md5": md5}, {"$set": {"hooks": hook_event.to_dict()}})
