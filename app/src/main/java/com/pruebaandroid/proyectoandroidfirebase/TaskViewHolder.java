package com.pruebaandroid.proyectoandroidfirebase;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {


    public TaskViewHolder(@NonNull View itemView, final TaskAdapter.OnTaskClickListener listener) {
        super(itemView);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onTaskLongClick(getAdapterPosition());
                }
                return true;
            }
        });
    }
}