package com.apk.analyzer;

import static com.apk.analyzer.utils.Helpers.getIcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apk.analyzer.adapters.HookInfosRecyclerAdapter;
import com.apk.analyzer.scanner.Analysis;
import com.apk.analyzer.scanner.AnalysisStatus;
import com.apk.analyzer.scanner.Hook;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {
    private ImageView apk_icon;
    private TextView apk_label;
    private TextView apk_pkg;
    private TextView md5;
    private RecyclerView recyclerView;
    private HookInfosRecyclerAdapter hookInfosRecyclerAdapter;
    private List<Hook> hookInfos;

    public Handler myHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message message) {
            System.out.println("analysis activity:" + message.obj);
            System.out.println("items in recycler: " + hookInfosRecyclerAdapter.getItemCount());
            AnalysisStatus as = (AnalysisStatus) message.obj;
            hookInfos = as.analysis;
            hookInfosRecyclerAdapter.notifyDataSetChanged();
            System.out.println("items in recycler: " + hookInfosRecyclerAdapter.getItemCount());
            return true;
        }
        });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Bundle b = getIntent().getExtras();
        String content = b.getString("analysis_status");
        AnalysisStatus as = new Gson().fromJson(content, AnalysisStatus.class);
        initialize_ui(as);
        new Analysis(new File(as.path), as.md5, myHandler).start();
    }

    private void initialize_ui(AnalysisStatus as){
        apk_icon = findViewById(R.id.analysis_activity_apk_icon);
        apk_icon.setImageDrawable(getIcon(as.path, getPackageManager()));
        apk_label = findViewById(R.id.analysis_activity_apk_label);
        apk_label.setText(as.manifest.label);
        apk_pkg = findViewById(R.id.analysis_activity_pkg_variable);
        apk_pkg.setText(as.manifest.pkn);
        md5 = findViewById(R.id.md5_hash_variable_textview);
        md5.setText(as.md5);
        recyclerView = findViewById(R.id.HookInfos);
        hookInfosRecyclerAdapter = new HookInfosRecyclerAdapter(as.analysis);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(hookInfosRecyclerAdapter);
        recyclerView.setLayoutManager(llm);

    }
}