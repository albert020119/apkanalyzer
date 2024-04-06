from dataclasses import dataclass


@dataclass
class Hook:
    name: str
    script: str
