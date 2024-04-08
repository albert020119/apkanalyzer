from dataclasses import dataclass


@dataclass
class AnalysisStatus:
    md5: str
    installed: bool
    started: bool
    manifest: dict
