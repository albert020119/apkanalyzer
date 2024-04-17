package com.apk.analyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apk.analyzer.scanner.FileObserver;

public class MainActivity extends AppCompatActivity {
    private FileObserver fileObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.initialize_ui();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initialize_ui() throws InterruptedException {
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.get_permission);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) & Environment.isExternalStorageManager()) {
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                this.startActivity(myIntent);
                System.out.println("Started intent");

            }
        }
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                MainActivity.this.request_perms();
            }
        });
    }


    private void request_perms(){
        System.out.println(Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) && !Environment.isExternalStorageManager()){
                // Permission not granted, request it

                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                System.out.println("Started intent");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageView).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) && (Environment.isExternalStorageManager())) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                    Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                    this.startActivity(myIntent);
                } else {
                    // Permission denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}