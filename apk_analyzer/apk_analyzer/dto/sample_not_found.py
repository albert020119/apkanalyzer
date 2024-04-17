from dataclasses import dataclass


@dataclass
class SampleNotFound:
    message: str = "Sample could not be found"
