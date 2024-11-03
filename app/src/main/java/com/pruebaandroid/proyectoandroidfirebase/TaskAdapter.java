package com.pruebaandroid.proyectoandroidfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnTaskClickListener listener;

    // Constructor
    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    // Método para actualizar la lista de tareas
    public void updateTaskList(List<Task> newTaskList) {
        this.tasks = newTaskList;
        notifyDataSetChanged(); // Actualizar la vista
    }

    // Método para eliminar tarea
    public void removeTask(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    // Interfaz para manejar clicks
    public interface OnTaskClickListener {
        void onTaskLongClick(int position);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task); // Usar el método bind para establecer los datos
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, descriptionTextView, dateTextView, priorityTextView, tagsTextView;

        public TaskViewHolder(@NonNull View itemView, OnTaskClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            descriptionTextView = itemView.findViewById(R.id.text_view_description);
            dateTextView = itemView.findViewById(R.id.text_view_date);
            priorityTextView = itemView.findViewById(R.id.text_view_priority);
            tagsTextView = itemView.findViewById(R.id.text_view_tags);

            // Manejar el evento de clic largo
            itemView.setOnLongClickListener(v -> {
                listener.onTaskLongClick(getAdapterPosition());
                return true;
            });
        }

        public void bind(Task task) {
            titleTextView.setText(task.getTitulo());
            descriptionTextView.setText(task.getDescripcion());
            dateTextView.setText(task.getFecha());
            priorityTextView.setText("Prioridad: " + task.getPrioridad());
            tagsTextView.setText("Etiquetas: " + String.join(", ", task.getEtiquetas()));

            // Cambiar estilo si la tarea está completada
            itemView.setAlpha(task.isCompletada() ? 0.5f : 1.0f);
        }
    }
}
