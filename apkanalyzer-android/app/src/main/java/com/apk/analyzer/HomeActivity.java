package com.apk.analyzer;

import static com.apk.analyzer.utils.Helpers.calculateMD5;
import static com.apk.analyzer.utils.Helpers.getIcon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apk.analyzer.adapters.ScanRecyclerAdapter;
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
    public List<AnalysisStatus> scans;
    public ScanRecyclerAdapter scan_adapter;
    Map<String, Drawable> md5_icons;

    public Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            System.out.println("din main thread:" + msg);
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
        File sdcard = new File("/sdcard");
        fileObserver = new FileObserver(sdcard);
        fileObserver.startWatching();
        deviceScanner = new DeviceScanner();
        scans = new ArrayList<AnalysisStatus>();
        md5_icons = new HashMap<String, Drawable>();
        initialize_ui();
    }

    private void initialize_ui(){
        setContentView(R.layout.activity_home);
        Button btn = (Button)findViewById(R.id.scan_all);
        TextView scannedView = (TextView) findViewById(R.id.files_scanned);
        recyclerView = findViewById(R.id.scans_view);
        scan_adapter = new ScanRecyclerAdapter(this.scans);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(scan_adapter);
        recyclerView.setLayoutManager(llm);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {
                HomeActivity.deviceScanner.scanForAPKsAsync(String.valueOf(Environment.getExternalStorageDirectory()), HomeActivity.this);
            }

        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Update the UI on the main thread
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            // Update the TextView with the number of files scanned
                            scannedView.setText("Number of files scanned: " + HomeActivity.deviceScanner.getNrFilesScanned());
                        }
                    });

                    try {
                        // Add a delay to avoid updating the UI too frequently
                        Thread.sleep(1000); // Update every 1 second (adjust as needed)
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
            Drawable icon = getIcon(file, getPackageManager());
            md5_icons.put(filehash, icon);
            System.out.println(filehash);
            Analysis analysis = new Analysis(file, filehash, myHandler, md5_icons);
            analysis.start();
        }
    }
}