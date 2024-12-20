import time
import random

from com.dtmilano.android.viewclient import View


class Jester:
    def __init__(self, emulator,  viewclient, apk, time_to_run: int):
        self.emulator = emulator
        self.viewclient = viewclient
        self.runtime = time_to_run
        self.apk = apk

    def start(self):
        time_start = time.time()

        def end_loop():
            time_running = time.time() - time_start
            print(time_running)
            if time_running > self.runtime:
                return True
            else:
                return False

        while True:
            if end_loop():
                return

            time.sleep(.5)
            try:
                dump = self.viewclient.dump()

                to_touch = self.identify_permission_screen(dump)
                if to_touch:
                    to_touch.touch()
                    self.emulator.send_random_sms()
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
            except Exception as e:
                pass

            if end_loop():
                return
        return

    def identify_special_permission_screen(self, dump: list[View]) -> View | None:  # TODO more special permissions
        try:
            apk_name = self.apk.get_app_name()
        except:
            apk_name = ""

        for view in dump:
            print(view)
            if "Accessibility" in view["content-desc"]:
                for view in dump:
                    if apk_name in view['text']:
                        return view
            if "Use " + apk_name in view['text']:
                return view
            if view['resource-id'] == 'com.android.settings:id/permission_enable_allow_button':
                return view

            if "Allow from this source" in view['text']:
                return view
        buttons = [view for view in dump if view['class'] == 'android.widget.Button']
        settings_button = [view for view in buttons if
                           view['text'] == 'Settings']
        install_button = [view for view in buttons if
                           view['text'] == 'Install']
        if len(settings_button):
            return settings_button[0]
        if len(install_button):
            return install_button[0]

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
