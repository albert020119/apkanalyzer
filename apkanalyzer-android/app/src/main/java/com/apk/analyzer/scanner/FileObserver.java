package com.apk.analyzer.scanner;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;


public class FileObserver extends android.os.FileObserver {
    private static final String TAG = "analyzer-observer";

    public FileObserver(File path){
        super(path);

    }
    @Override
    public void onEvent(int event, String path) {
        switch (event) {
            case FileObserver.ACCESS:
                //Log.d(TAG, "File or directory " + path + " was accessed");
                break;
            case FileObserver.MODIFY:
                //Log.d(TAG, "File or directory " + path + " was modified");
                break;
            case FileObserver.DELETE:
                //Log.d(TAG, "File or directory " + path + " was deleted");
                break;
            case FileObserver.CREATE:
                //Log.d(TAG, "File or directory " + path + " was created");
                break;
            case FileObserver.CLOSE_WRITE:
                //Log.d(TAG, "File or directory " + path + " was closed for writing");
                break;
            case FileObserver.CLOSE_NOWRITE:
                //Log.d(TAG, "File or directory " + path + " was closed for reading");
                break;
            case FileObserver.OPEN:
                //Log.d(TAG, "File or directory " + path + " was opened");
                break;
            default:
                //Log.d(TAG, "Event occurred on " + path + ": " + event);
                break;
        }
    }
}
