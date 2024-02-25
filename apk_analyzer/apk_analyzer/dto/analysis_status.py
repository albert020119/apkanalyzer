from dataclasses import dataclass


@dataclass
class AnalysisStatus:
    filename: str
    md5: str
    installed: str
