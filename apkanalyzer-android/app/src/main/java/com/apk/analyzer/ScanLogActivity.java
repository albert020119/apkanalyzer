package com.apk.analyzer;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
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
    private ListView listView;
    private ArrayAdapter<Scan> adapter;
    private List<Scan> scanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_log);

        listView = findViewById(R.id.scan_list);
        scanList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scanList);
        listView.setAdapter(adapter);

        new Thread(() -> {
            List<Scan> scans = HomeActivity.scansCache.getAll();
            runOnUiThread(() -> {
                scanList.addAll(scans);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}

