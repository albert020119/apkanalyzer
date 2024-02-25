from dataclasses import dataclass


@dataclass
class StartAnalysis:
    filename: str
    md5: str
