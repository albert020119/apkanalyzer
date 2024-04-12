// Hooking Android's sendTextMessage method


    var SmsManager = Java.use("android.telephony.SmsManager");

    // Hooking the sendTextMessage method
    SmsManager.sendTextMessage.overload('java.lang.String', 'java.lang.String', 'java.lang.String', 'android.app.PendingIntent', 'android.app.PendingIntent').implementation = function(destAddr, scAddr, text, sentIntent, deliveryIntent) {
        var timestamp = new Date().getTime();
        var hookEventData = {
            type: 'telephony',
            method: 'SmsManager.sendTextMessage',
            code: 3,
            timestamp: timestamp,
            args: [destAddr, text],
            return_value: null
        };
        send(JSON.stringify(hookEventData));
        return this.sendTextMessage.apply(this, arguments);
    };



