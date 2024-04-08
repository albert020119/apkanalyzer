from dataclasses import dataclass
from typing import Optional, List


@dataclass
class ManifestInfo:
    pkn: str
    permissions: List[str]

    @classmethod
    def from_dict(cls, data: dict) -> 'ManifestInfo':
        return cls(
            data.get('pkn'),
            data.get('perms')
        )

    def to_dict(self) -> dict:
        return {
            'pkn': self.pkn,
            'perms': self.permissions
        }


@dataclass
class SampleEntry:
    md5: str
    file_path: Optional[str] = None
    installed: Optional[bool] = False
    found_emulator: Optional[bool] = False
    started: Optional[bool] = False
    manifest: Optional[ManifestInfo | None] = None

    def to_dict(self) -> dict:
        return {
            'md5': self.md5,
            'file_path': self.file_path,
            'installed': self.installed,
            'found_emulator': self.found_emulator,
            'started': self.started,
            'manifest': {
                'pkn': self.manifest.pkn,
                'perms': self.manifest.permissions
            } if self.manifest else None
        }

    @classmethod
    def from_dict(cls, data: dict) -> 'SampleEntry':
        return cls(
            md5=data['md5'],
            file_path=data.get('file_path'),
            installed=data.get('installed', False),
            found_emulator=data.get('found_emulator', False),
            started=data.get('started', False),
            manifest=ManifestInfo.from_dict(data.get('manifest')) if data.get('manifest') else None
        )
