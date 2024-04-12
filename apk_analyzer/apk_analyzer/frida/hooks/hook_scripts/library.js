    var System = Java.use('java.lang.System');
    var Runtime = Java.use('java.lang.Runtime');
    var VMStack = Java.use('dalvik.system.VMStack');

//    System.loadLibrary.implementation = function(library) {
//        var timestamp = new Date().getTime();
//        var hookEventData = {
//            type: 'library',
//            method: 'System.loadLibrary',
//            timestamp: timestamp,
//            args: [library],
//            return_value: null
//        };
//        send(JSON.stringify(hookEventData));
//        return this.loadLibrary(library);
//
//    };



