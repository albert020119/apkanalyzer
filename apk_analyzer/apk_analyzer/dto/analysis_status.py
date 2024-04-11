from dataclasses import dataclass


@dataclass
class HookStatus:
    type: str
    name: str
    code: int
    description: str


@dataclass
class PermissionStatus:
    name: str
    code: int
    description: str


@dataclass
class ManifestStatus:
    pkn: str
    permissions: list[PermissionStatus]


@dataclass
class AnalysisStatus:
    md5: str
    found_emulator: bool
    installed: bool
    started: bool
    finished: bool
    manifest: ManifestStatus
    analysis: list[HookStatus]
