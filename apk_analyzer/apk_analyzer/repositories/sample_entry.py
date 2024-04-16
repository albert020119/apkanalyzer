from dataclasses import dataclass, field
from typing import Optional, List

from ..frida.hooks.hook import HookEvent


@dataclass
class ManifestInfo:
    pkn: str
    label: str
    permissions: List[str]

    @classmethod
    def from_dict(cls, data: dict) -> 'ManifestInfo':
        return cls(
            data.get('pkn'),
            data.get('label'),
            data.get('perms')
        )

    def to_dict(self) -> dict:
        return {
            'pkn': self.pkn,
            'label': self.label,
            'perms': self.permissions
        }


@dataclass
class SampleEntry:
    md5: str
    file_path: Optional[str] = None
    installed: Optional[bool] = False
    found_emulator: Optional[bool] = False
    started: Optional[bool] = False
    finished: Optional[bool] = False
    manifest: Optional[ManifestInfo | None] = None
    hooks: List = field(default_factory=lambda: [])

    def to_dict(self) -> dict:
        return {
            'md5': self.md5,
            'file_path': self.file_path,
            'installed': self.installed,
            'found_emulator': self.found_emulator,
            'started': self.started,
            'finished': self.finished,
            'manifest': {
                'pkn': self.manifest.pkn,
                'perms': self.manifest.permissions
            } if self.manifest else None,
            "hooks": [he.to_dict() for he in self.hooks] if self.hooks else []
        }

    @classmethod
    def from_dict(cls, data: dict) -> 'SampleEntry':
        return cls(
            md5=data['md5'],
            file_path=data.get('file_path'),
            installed=data.get('installed', False),
            found_emulator=data.get('found_emulator', False),
            started=data.get('started', False),
            finished=data.get('finished', False),
            manifest=ManifestInfo.from_dict(data.get('manifest')) if data.get('manifest') else None,
            hooks=data.get("hooks", [])
        )
