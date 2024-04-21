package com.apk.analyzer.scanner;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import com.apk.analyzer.network.Backend;
import com.google.gson.Gson;

import java.io.File;
import java.util.Map;

public class Analysis extends Thread{
    private String md5;
    private boolean finished;
    public Handler handler;
    public File file;

    public Analysis(File file, String md5, Handler handler){
        this.md5 = md5;
        this.finished = false;
        this.handler = handler;
        this.file = file;
    }
    @Override
    public void run(){
        String initial_resp = Backend.sendGetRequest("http://10.0.2.2:8000/status?md5=" + this.md5);
        Gson gson = new Gson();
        AnalysisStatus as = gson.fromJson(initial_resp, AnalysisStatus.class);
        if (!as.finished){
            System.out.println("starting sample analysis");
            Backend.sendPostRequest("http://10.0.2.2:8000/apk", this.file);
        }
        while(!this.finished){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String status = Backend.sendGetRequest("http://10.0.2.2:8000/status?md5=" + this.md5);
            gson = new Gson();
            as = gson.fromJson(status, AnalysisStatus.class);
            as.path = file.getPath();
            if (as.finished) {
                this.finished = true;
            }

            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.obj = as;
            handler.sendMessage(msg);
        }

    }
}
