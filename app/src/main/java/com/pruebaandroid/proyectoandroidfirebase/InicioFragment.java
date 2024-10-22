package com.pruebaandroid.proyectoandroidfirebase;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InicioFragment extends Fragment {
    //declaracion de variables
    private ImageButton btn_filter, btn_ordenar, btn_crear, btn_menu;
    private RecyclerView recyclerViewTasks; //recycler (en donde se ven las tareas)
    private TaskAdapter taskAdapter; //adaptador
    private List<Task> taskList; //array

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        btn_filter = view.findViewById(R.id.btn_filter);
        btn_ordenar = view.findViewById(R.id.btn_ordenar);
        btn_crear = view.findViewById(R.id.btn_crear);
        recyclerViewTasks = view.findViewById(R.id.recycler_view_tasks);
        // Configurar el RecyclerView
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        recyclerViewTasks.setAdapter(taskAdapter);
        // Cargar tareas (esto puede venir de una base de datos, de momento estático)
        loadTasks();

        btn_crear.setOnClickListener(v -> {
            //brayan pone tu code para navegar a tu seccion de crear tarea
        });

        // Listener para el botón de filtrar
        btn_filter.setOnClickListener(v -> {
            // Opciones de filtro
            String[] filterOptions = {"Personal (Amarillo)", "Trabajo (Verde)", "Estudio (Azul)", "Otros (Rojo)"};

            // Crear un AlertDialog para desplegar las opciones
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleccionar Filtro")
                    .setItems(filterOptions, (dialog, which) -> {
                        // Lógica para aplicar el filtro seleccionado
                        String selectedFilter = filterOptions[which];
                        applyFilter(selectedFilter);
                    })
                    .show();
        });

        // Listener para el botón de ordenar
        btn_ordenar.setOnClickListener(v -> {
            // Opciones de ordenación
            String[] ordenOptions = {"A-Z", "Z-A"};

            // Crear un AlertDialog para desplegar las opciones
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Seleccionar Ordenación")
                    .setItems(ordenOptions, (dialog, which) -> {
                        // Lógica para aplicar la ordenación seleccionada
                        String selectedSort = ordenOptions[which];
                        applyOrden(selectedSort);
                    })
                    .show();
        });

        return view;
    }

    private void applyFilter(String filterOption) {
        List<Task> filteredTasks = new ArrayList<>();

        switch (filterOption) {
            case "Personal (Amarillo)":
                // Filtrar tareas personales (color amarillo)
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Personal") && task.getColor().equals("Amarillo")) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "Trabajo (Verde)":
                // Filtrar tareas de trabajo (color verde)
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Trabajo") && task.getColor().equals("Verde")) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "Estudio (Azul)":
                // Filtrar tareas de estudio (color azul)
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Estudio") && task.getColor().equals("Azul")) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "Otros (Rojo)":
                // Filtrar otras tareas (color rojo)
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Otros") && task.getColor().equals("Rojo")) {
                        filteredTasks.add(task);
                    }
                }
                break;
        }

        // Aquí puedes actualizar la vista del RecyclerView o ListView con las tareas filtradas
        updateTaskListView(filteredTasks);
    }

    private void updateTaskListView(List<Task> filteredTasks) {
        taskAdapter.updateTaskList(filteredTasks);
        taskAdapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }


    // Función para aplicar la ordenación seleccionada
    private void applyOrden(String ordenOption) {
        if (ordenOption.equals("A-Z")) {
            // Ordenar de A-Z (alfabéticamente)
            Collections.sort(taskList, (task1, task2) -> task1.getTitulo().compareToIgnoreCase(task2.getTitulo()));
        } else if (ordenOption.equals("Z-A")) {
            // Ordenar de Z-A (alfabéticamente, invertido)
            Collections.sort(taskList, (task1, task2) -> task2.getTitulo().compareToIgnoreCase(task1.getTitulo()));
        }

        // Actualizar la vista con la lista de tareas ordenadas
        updateTaskListView(taskList);
    }


    // Cargar algunas tareas de ejemplo
    private void loadTasks() {
        taskList.add(new Task("Comprar comida", "Ir al supermercado", "Personal", "amarillo"));
        taskList.add(new Task("Reunión de trabajo", "Zoom a las 10am", "trabajo", "verde"));
        taskAdapter.notifyDataSetChanged(); // Actualizar la lista en el RecyclerView
    }

}