package com.apk.analyzer;

import static com.apk.analyzer.utils.Helpers.calculateMD5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apk.analyzer.network.Backend;
import com.apk.analyzer.scanner.DeviceScanner;
import com.apk.analyzer.scanner.FileObserver;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DeviceScanner.ScanListener {
    public static FileObserver fileObserver;
    public static DeviceScanner deviceScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        File sdcard = new File("/sdcard");
        fileObserver = new FileObserver(sdcard);
        fileObserver.startWatching();
        deviceScanner = new DeviceScanner();
        initialize_ui();
    }

    private void initialize_ui(){
        setContentView(R.layout.activity_home);
        Button btn = (Button)findViewById(R.id.scan_all);
        TextView scannedView = (TextView) findViewById(R.id.files_scanned);
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
            System.out.println(filehash);
            new Backend.SendPostRequest().execute(file);
//            String url = "http://192.168.56.1:8000/status?md5=" + filehash;
//            new Backend.SendGetRequest().execute(url);
        }
    }
}