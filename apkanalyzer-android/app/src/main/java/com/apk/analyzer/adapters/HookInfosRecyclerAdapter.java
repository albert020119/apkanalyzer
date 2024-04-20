package com.apk.analyzer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apk.analyzer.R;
import com.apk.analyzer.scanner.Hook;

import java.util.List;

public class HookInfosRecyclerAdapter extends RecyclerView.Adapter<HookInfosRecyclerAdapter.ViewHolder>{
    private List<Hook> hookInfos;
    public HookInfosRecyclerAdapter(List<Hook> hooks){
        hookInfos = hooks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView methodName;
        private RelativeLayout relativeLayout;
        private ImageView imageView;
        private TextView descriptionContent;

        public ViewHolder(View view) {
            super(view);
            methodName = view.findViewById(R.id.method_name_variable);
            relativeLayout = view.findViewById(R.id.hook_card);
            imageView = view.findViewById(R.id.hook_sign);
            descriptionContent = view.findViewById(R.id.description_content);
        }

        public TextView getMethodName() {return methodName;}
        public RelativeLayout getRelativeLayout() {return relativeLayout;}
        public ImageView getImageView() {return imageView;}
        public TextView getDescriptionContent() {return descriptionContent;}
    }
    @NonNull
    @Override
    public HookInfosRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hook_card_view, parent, false);
        return new HookInfosRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HookInfosRecyclerAdapter.ViewHolder holder, int position) {
        holder.getMethodName().setText(hookInfos.get(position).name);
        holder.getDescriptionContent().setText(hookInfos.get(position).description);
        if (hookInfos.get(position).code == 3){
            holder.getRelativeLayout().setBackgroundColor(Color.RED);
            holder.getImageView().setImageResource(R.drawable.warning_logo);
        } else if (hookInfos.get(position).code == 2){
            holder.getRelativeLayout().setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public int getItemCount() {
        return hookInfos.size();
    }
}
