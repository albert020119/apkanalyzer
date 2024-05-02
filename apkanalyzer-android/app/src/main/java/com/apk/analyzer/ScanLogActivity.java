package com.apk.analyzer;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.apk.analyzer.cache.AppDatabase;
import com.apk.analyzer.scanner.AnalysisStatus;
import com.apk.analyzer.scanner.DeviceScanner;
import com.apk.analyzer.scanner.FileObserver;

import java.io.File;
import java.util.ArrayList;

public class ScanLogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
