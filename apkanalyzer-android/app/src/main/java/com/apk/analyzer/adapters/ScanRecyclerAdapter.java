package com.apk.analyzer.adapters;


import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;

import static com.apk.analyzer.utils.Helpers.getIcon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.apk.analyzer.AnalysisActivity;
import com.apk.analyzer.HomeActivity;
import com.apk.analyzer.R;
import com.apk.analyzer.scanner.AnalysisStatus;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ScanRecyclerAdapter extends RecyclerView.Adapter<ScanRecyclerAdapter.ViewHolder>{
    private List<AnalysisStatus> localDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView md5_textView;
        private final TextView pkn_textView;
        private final TextView app_label_textView;
        private final TextView analysis_in_progress_textview;
        private final ImageView apk_icon;
        private final ImageView analysis_finished_imageview;
        private final ProgressBar analysis_progress_bar;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            app_label_textView = (TextView) view.findViewById(R.id.app_label_textview);
            pkn_textView = (TextView) view.findViewById(R.id.pkn_textview);
            md5_textView = (TextView) view.findViewById(R.id.md5_textview);
            apk_icon = (ImageView) view.findViewById(R.id.apk_icon);
            analysis_progress_bar = (ProgressBar) view.findViewById(R.id.analysis_in_progress_progressbar);
            analysis_in_progress_textview = (TextView) view.findViewById(R.id.analysis_in_progress_textview);
            analysis_finished_imageview = (ImageView) view.findViewById(R.id.analysis_imaged_imageview);
        }

        public TextView get_md5_textView() {
            return md5_textView;
        }
        public TextView get_pkn_textView() {
            return pkn_textView;
        }
        public TextView get_app_label_textView() {
            return app_label_textView;
        }
        public ImageView get_apk_icon(){return apk_icon;}
        public TextView getAnalysis_in_progress_textview() {return analysis_in_progress_textview;}
        public ImageView getAnalysis_finished_imageview() {return analysis_finished_imageview;}
        public ProgressBar getAnalysis_progress_bar() {return analysis_progress_bar;}
    }

    public ScanRecyclerAdapter(List<AnalysisStatus> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.analysis_card_layout, viewGroup, false);
        this.context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.get_app_label_textView().setText(localDataSet.get(position).manifest.label);
        viewHolder.get_md5_textView().setText("md5 hash: " + localDataSet.get(position).md5);
        viewHolder.get_pkn_textView().setText(localDataSet.get(position).manifest.pkn);
        viewHolder.get_apk_icon().setImageDrawable(getIcon(localDataSet.get(position).path, context.getPackageManager()));
        if (localDataSet.get(position).finished){
            viewHolder.getAnalysis_in_progress_textview().setText("Analysis Finished");
            viewHolder.getAnalysis_progress_bar().setVisibility(View.INVISIBLE);
            viewHolder.getAnalysis_finished_imageview().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getAnalysis_in_progress_textview().setText("Analysis In Progress");
            viewHolder.getAnalysis_progress_bar().setVisibility(View.VISIBLE);
            viewHolder.getAnalysis_finished_imageview().setVisibility(View.INVISIBLE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AnalysisActivity.class);
                String content = new Gson().toJson(localDataSet.get(position));
                intent.putExtra("analysis_status", content);
                view.getContext().startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
