package com.apk.analyzer.scanner;

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


    public interface ScanListener {
        void onScanCompleted(List<File> apkFiles);
    }

    public DeviceScanner(){
        this.total_nr_files_scanned = 0;
        this.last_nr_files_scanned = 0;
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

    private boolean isAPK(File file) {
        // Check if the file has .apk extension
        String fileName = file.getName();
        return fileName.toLowerCase().endsWith(".apk");
    }
}
