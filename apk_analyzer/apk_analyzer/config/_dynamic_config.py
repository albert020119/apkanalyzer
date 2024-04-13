dynamic_info = {
    "accessibility": {
        "onServiceConnected": "The app asks for and uses accessibility permission. This grants total control of the device to potential malware."
    },
    "dex": {
        "dalvik.system.DexFile.init": "The app tried to load code dynamically. A tehnique sometimes used by malware creators to hide malicious activity"
    },
    "telephony": {
        "SmsManager.sendTextMessage": "The app was caught sending SMS messages without the users consent."
    },
    "network": {
        "webview.loadUrl": "The app loaded various URLs in a webview.",
        "HttpURLConnection.getInputStream": "The app communicated with URL.",
        "HttpURLConnection.getOutputStream": "The app communicated with URL."
    }
}
