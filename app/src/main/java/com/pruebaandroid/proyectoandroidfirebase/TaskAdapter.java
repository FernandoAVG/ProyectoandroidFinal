package com.pruebaandroid.proyectoandroidfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // clase java adaptador para tareas

    //task adapter para list del viewholder, donde se ven las tareas.
    private List<Task> tasks;

    //cpnstructos
    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void updateTaskList(List<Task> newTaskList) {
        this.tasks = newTaskList;
        notifyDataSetChanged(); // Notificar cambios para actualizar la vista
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
        // lol
        holder.itemView.setOnClickListener(v -> {
            task.setCompletada(!task.isCompletada());
            notifyItemChanged(position); // Actualiza la vista solo de la tarea modificada

            // lo de abajo es para actualizar en la base de datos de Firebase si lo ven necesario (lo dejo como comentario)
            // updateTaskInDatabase(task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, descriptionTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            descriptionTextView = itemView.findViewById(R.id.text_view_description);
        }

        public void bind(Task task) {
            titleTextView.setText(task.getTitulo());
            descriptionTextView.setText(task.getDescripcion());
            // Cambiar el estilo si la tarea está completada
            itemView.setAlpha(task.isCompletada() ? 0.5f : 1.0f); // Ejemplo de opacidad
        }
    }
}