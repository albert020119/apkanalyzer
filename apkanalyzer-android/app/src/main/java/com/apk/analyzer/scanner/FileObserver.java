package com.apk.analyzer.scanner;

import static com.apk.analyzer.HomeActivity.myHandler;
import static com.apk.analyzer.utils.Helpers.calculateMD5;

import android.util.Log;

import androidx.annotation.Nullable;

import com.apk.analyzer.HomeActivity;

import java.io.File;


public class FileObserver extends android.os.FileObserver {
    private static final String TAG = "analyzer-observer";
    private static File parentFile = null;

    public FileObserver(File path){
        super(path);
        FileObserver.parentFile = path;

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
                // HomeActivity.my
                Log.d(TAG, "File or directory " + path + " was created");
                String finalPath = FileObserver.parentFile + "/" + path;
                File file = new File(finalPath);
                String filehash = calculateMD5(file);
                Analysis analysis = new Analysis(file, filehash, myHandler);
                analysis.start(); // TODO cred ca calculeaza md5 gresit imi bag pula in ea
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
