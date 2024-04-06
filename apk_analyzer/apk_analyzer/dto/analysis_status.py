from dataclasses import dataclass


@dataclass
class AnalysisStatus:
    filename: str
    md5: str
    found_emulator: bool
    installed: bool
    started: bool

    @staticmethod
    def get_initial(filename: str, md5: str):
        return AnalysisStatus(
            filename=filename,
            md5=md5,
            found_emulator=False,
            installed=False,
            started=False
        )
