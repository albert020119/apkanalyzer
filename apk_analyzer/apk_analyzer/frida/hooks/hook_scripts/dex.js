var memoryclassLoader = Java.use("dalvik.system.InMemoryDexClassLoader");
var DexFile = Java.use('dalvik.system.DexFile');
memoryclassLoader.$init.overload('java.nio.ByteBuffer', 'java.lang.ClassLoader').implementation = function(dexbuffer, loader) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.InMemoryDexClassLoader',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    var object = this.$init(dexbuffer, loader);
    return object;
}
memoryclassLoader.$init.overload('[Ljava.nio.ByteBuffer;', 'java.lang.ClassLoader').implementation = function(dexbuffer, loader) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.InMemoryDexClassLoader',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    var object = this.$init(dexbuffer, loader);
    return object;
}
DexFile.$init.overload('java.lang.String').implementation = function (file) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.DexFile.init',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    return this.$init(file);
};

DexFile.$init.overload('java.io.File').implementation = function (file) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.DexFile.init',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    return this.$init(file);
};

DexFile.$init.overload('java.io.File', 'java.lang.ClassLoader', '[Ldalvik.system.DexPathList$Element;').implementation = function(file, classLoader, elements) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.DexFile.init',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    return this.$init(file, classLoader, elements);
};

DexFile.$init.overload('java.lang.String', 'java.lang.ClassLoader', '[Ldalvik.system.DexPathList$Element;').implementation = function(fileName, classLoader, elements) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.DexFile.init',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    return this.$init(fileName, classLoader, elements);
};

DexFile.$init.overload('java.lang.String', 'java.lang.String', 'int', 'java.lang.ClassLoader', '[Ldalvik.system.DexPathList$Element;').implementation = function(fileName, optimizedDirectory, flags, classLoader, elements) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.DexFile.init',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    return this.$init(fileName, optimizedDirectory, flags, classLoader, elements);
};

DexFile.loadDex.overload('java.lang.String', 'java.lang.String', 'int').implementation = function(sourcePathName, outputPathName, flags) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.DexFile.loadDex',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    this.loadDex.call(sourcePathName, outputPathName, flags);
}

DexFile.loadClass.overload('java.lang.String', 'java.lang.ClassLoader').implementation = function(name, loader) {
    var timestamp = new Date().getTime();
    var hookEventData = {
        type: 'dex',
        method: 'dalvik.system.DexFile.loadClass',
        code: 2,
        timestamp: timestamp,
        args: [],
        return_value: null
    };
    send(JSON.stringify(hookEventData));
    this.loadClass.call(name, loader);
}