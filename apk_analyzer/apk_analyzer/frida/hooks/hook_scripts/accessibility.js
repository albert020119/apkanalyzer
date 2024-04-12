var accessibilityservice = Java.use("android.accessibilityservice.AccessibilityService");
accessibilityservice.onServiceConnected.implementation = function() {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'accessibility',
        method: 'onServiceConnected',
        code: 3,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    return this.onServiceConnected();

};