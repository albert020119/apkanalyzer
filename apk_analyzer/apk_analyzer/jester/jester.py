import time
import random

from com.dtmilano.android.viewclient import View


class Jester:
    def __init__(self, viewclient, apk, time_to_run: int):
        self.viewclient = viewclient
        self.runtime = time_to_run
        self.apk = apk

    def start(self):
        time_start = time.time()

        def end_loop():
            if time.time() - time_start > self.runtime:
                return True
            else:
                return False

        while True:
            if end_loop():
                break

            time.sleep(1)
            dump = self.viewclient.dump()

            to_touch = self.identify_permission_screen(dump)
            if to_touch:
                to_touch.touch()
                continue

            to_touch = self.identify_special_permission_screen(dump)
            if to_touch:
                to_touch.touch()
                continue

            if self.is_login_screen(dump):
                self.enter_creds(dump)
                continue

            to_touch = self.get_random_button(dump)
            if to_touch:
                to_touch.touch()
                continue

            if end_loop():
                break
        return

    def identify_special_permission_screen(self, dump: list[View]) -> View | None:  # TODO more special permissions
        for view in dump:
            if "Accessibility" in view["content-desc"]:
                for view in dump:
                    if self.apk.get_app_name() in view['text']:
                        return view
            if "Use " + self.apk.get_app_name() in view['text']:
                return view
            if view['resource-id'] == 'com.android.settings:id/permission_enable_allow_button':
                return view
        return None

    @staticmethod
    def identify_permission_screen(dump: list[View]) -> View | None:
        buttons = [view for view in dump if view['class'] == 'android.widget.Button']
        allow_button = [view for view in buttons if
                        view['resource-id'] == 'com.android.permissioncontroller:id/permission_allow_button']
        decline_button = [view for view in buttons if
                          view['resource-id'] == 'com.android.permissioncontroller:id/permission_deny_button']
        if allow_button and decline_button:
            return allow_button[0]
        return None

    def is_login_screen(self, dump: list[View]) -> bool:  # TODO
        pass

    def enter_creds(self, dump: list[View]):  # TODO
        pass

    @staticmethod
    def get_random_button(dump: list[View]) -> View | None:
        buttons = [view for view in dump if view['class'] == 'android.widget.Button']
        return random.choice(buttons) if buttons else None
