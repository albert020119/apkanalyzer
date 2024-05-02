package com.apk.analyzer;

import static com.apk.analyzer.utils.Helpers.calculateMD5;
import static com.apk.analyzer.utils.Helpers.getIcon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apk.analyzer.adapters.ScanRecyclerAdapter;
import com.apk.analyzer.cache.AppDatabase;
import com.apk.analyzer.cache.ScansCache;
import com.apk.analyzer.scanner.Analysis;
import com.apk.analyzer.scanner.AnalysisStatus;
import com.apk.analyzer.scanner.DeviceScanner;
import com.apk.analyzer.scanner.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements DeviceScanner.ScanListener {
    public static FileObserver fileObserver;
    public static DeviceScanner deviceScanner;
    public static RecyclerView recyclerView;
    public static List<AnalysisStatus> scans;
    public static ScanRecyclerAdapter scan_adapter;
    public static ScansCache scansCache;
    public static Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            for (int i = 0; i < scans.size(); i++){
                System.out.println(scans.get(i).md5 + " " + ((AnalysisStatus)msg.obj).md5);
                if (scans.get(i).md5.equals(((AnalysisStatus)msg.obj).md5)){
                    scans.set(i, (AnalysisStatus) msg.obj);
                    scan_adapter.notifyItemChanged(i);
                    return true;
                }
            }
            scans.add((AnalysisStatus) msg.obj);
            scan_adapter.notifyDataSetChanged();
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        File sdcard = new File(Environment.getExternalStorageDirectory().getPath());
        fileObserver = new FileObserver(sdcard);
        fileObserver.startWatching();
        deviceScanner = new DeviceScanner(getApplicationContext());
        scans = new ArrayList<AnalysisStatus>();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "scans").build();
        scansCache = db.scansCache();
        initialize_ui();
    }

    private void initialize_ui(){
        setContentView(R.layout.activity_home);
        TextView scannedView = (TextView) findViewById(R.id.files_scanned);
        recyclerView = findViewById(R.id.scans_view);
        scan_adapter = new ScanRecyclerAdapter(this.scans);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(scan_adapter);
        recyclerView.setLayoutManager(llm);

        Button btn = (Button)findViewById(R.id.scan_all);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {
                HomeActivity.deviceScanner.scanForAPKsAsync(String.valueOf(Environment.getExternalStorageDirectory()), HomeActivity.this);
            }

        });

        Button btn2 = (Button)findViewById(R.id.past_scans_button);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, ScanLogActivity.class);
                startActivity(myIntent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            scannedView.setText("Number of files scanned: " + HomeActivity.deviceScanner.getNrFilesScanned());
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void onScanCompleted(List<File> apkFiles){
        deviceScanner.last_scan_finished = true;

        for (File file : apkFiles) {
            System.out.println("File Name: " + file.getName());
            String filehash = calculateMD5(file);
            System.out.println(filehash);
            Analysis analysis = new Analysis(file, filehash, myHandler);
            analysis.start();
        }
    }
}