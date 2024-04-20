package com.apk.analyzer.scanner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceScanner {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public boolean last_scan_finished;
    private int total_nr_files_scanned;
    private int last_nr_files_scanned;
    private Context ctx;


    public interface ScanListener {
        void onScanCompleted(List<File> apkFiles);
    }

    public DeviceScanner(Context ctx){
        this.total_nr_files_scanned = 0;
        this.last_nr_files_scanned = 0;
        this.ctx = ctx;
    }

    public int getNrFilesScanned(){
        return this.last_nr_files_scanned;
    }

    public void scanForAPKsAsync(final String folderPath, final ScanListener listener) {
        this.last_nr_files_scanned = 0;
        this.last_scan_finished = false;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<File> apkFiles = scanForAPKs(folderPath);
                if (listener != null) {
                    listener.onScanCompleted(apkFiles);
                }
            }
        });

    }

    public List<File> scanForAPKs(String folderPath) {
        List<File> apkFiles = new ArrayList<>();
        File folder = new File(folderPath);
        scanFolderForAPKs(folder, apkFiles);
        scanInstalledAPKs(apkFiles);
        return apkFiles;
    }

    private void scanFolderForAPKs(File folder, List<File> apkFiles) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Recursively scan subdirectories
                        scanFolderForAPKs(file, apkFiles);
                    } else {
                        this.total_nr_files_scanned++;
                        this.last_nr_files_scanned++;
                        if (isAPK(file)) {
                            apkFiles.add(file);
                        }
                    }
                }
            }
        }
    }

    public void scanInstalledAPKs(List<File> apkFiles){
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ApplicationInfo> apps = ctx.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo info : apps) {
            System.out.println(info.publicSourceDir);
            File file = new File(info.publicSourceDir);
            if (((info.flags & ApplicationInfo.FLAG_SYSTEM) != 1) & (!info.packageName.equals(ctx.getPackageName()))){
                System.out.println("INSTALLED THIRD PARTY: " + info.publicSourceDir);
                apkFiles.add(file);
            }
        }
    }

    private boolean isAPK(File file) {
        // Check if the file has .apk extension
        String fileName = file.getName();
        return fileName.toLowerCase().endsWith(".apk");
    }
}
