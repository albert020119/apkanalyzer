import os
from .hook import Hook


def get_hooks():
    hooks: list[Hook] = []
    path = "apk_analyzer/frida/hooks/scripts"
    for file in os.listdir(path):
        with open(os.path.join(path, file), "r") as f:
            contents = f.read()
            hook = Hook(
                name=f.name.split(".")[0],
                script=contents
            )
            hooks.append(hook)
    return hooks
