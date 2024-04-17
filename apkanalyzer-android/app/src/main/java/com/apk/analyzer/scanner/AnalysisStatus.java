package com.apk.analyzer.scanner;

import android.graphics.drawable.Drawable;

import java.util.List;

public class AnalysisStatus{
    public String md5;
    public String path;
    public boolean foundEmulator;
    public boolean installed;
    public boolean started;
    public boolean finished;
    public ManifestInfo manifest;
    public List<Hook> analysis;

    // Getters and setters
}

