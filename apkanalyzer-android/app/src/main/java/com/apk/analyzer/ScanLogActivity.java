package com.apk.analyzer;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.apk.analyzer.cache.AppDatabase;
import com.apk.analyzer.cache.Scan;
import com.apk.analyzer.cache.ScansCache;
import com.apk.analyzer.scanner.AnalysisStatus;
import com.apk.analyzer.scanner.DeviceScanner;
import com.apk.analyzer.scanner.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanLogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_log);
        ListView lv = findViewById(R.id.scan_list);
        new Thread(() -> {
            List<Scan> scans = HomeActivity.scansCache.getAll();
            for (Scan scan : scans) {
                // TODO add to list view
            }
        }).start();

    }
}
