package com.apk.analyzer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

        public ViewHolder(View view) {
            super(view);
            methodName = view.findViewById(R.id.method_name_variable);
        }

        public TextView getMethodName() {
            return methodName;
        }
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
        System.out.println("adding element to view !! ! ! !");
        holder.getMethodName().setText(hookInfos.get(position).name);
    }

    @Override
    public int getItemCount() {
        return hookInfos.size();
    }
}
