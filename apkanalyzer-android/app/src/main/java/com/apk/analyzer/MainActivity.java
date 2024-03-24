package com.apk.analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.apk.analyzer.network.Backend;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button my_button = (Button) findViewById(R.id.button_test) ;
        my_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Backend.SendGetRequest().execute("http://10.0.2.2:8000");
            }
        });
    }
}