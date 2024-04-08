class HookHandler:

    def __init__(self, md5: str):
        self.md5 = md5

    def on_hook(self, message: str, data: str):
        print(message)
