import json

from dataclasses import dataclass


@dataclass
class Hook:
    name: str
    script: str


@dataclass
class HookEvent:
    type: str
    method: str
    code: int
    timestamp: int
    args: list | str | None
    return_value: str | None

    @classmethod
    def from_str(cls, data: str) -> 'HookEvent':
        data = json.loads(data)
        return cls(
            type=data['type'],
            method=data['method'],
            code=data['code'],
            timestamp=data['timestamp'],
            args=data.get('args', None),
            return_value=data.get('return_value', None),
        )

    def to_dict(self) -> dict:
        return {
            'type': self.type,
            'method': self.method,
            'code': self.code,
            'timestamp': self.timestamp,
            'args': self.args,
            'return_value': self.return_value
        }
