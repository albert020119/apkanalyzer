package com.apk.analyzer.scanner;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

public class FileObserverService extends Service {
    private FileObserver fileObserver;
    private HandlerThread handlerThread;
    private Handler handler;
    private Runnable logRunnable;
    private static final int LOG_INTERVAL = 2000; // 2 seconds

    public FileObserverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        fileObserver = new FileObserver(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        fileObserver.startWatching();
        System.out.println("FileObserver started");
        handlerThread = new HandlerThread("FileObserverLogger");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        logRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("FileObserver is running");
                handler.postDelayed(this, LOG_INTERVAL);
            }
        };
        handler.post(logRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("FileObserver stopped");
        if (handler != null && logRunnable != null) {
            handler.removeCallbacks(logRunnable);
        }
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
