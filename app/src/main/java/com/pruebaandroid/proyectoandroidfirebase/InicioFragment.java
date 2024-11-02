package com.pruebaandroid.proyectoandroidfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InicioFragment extends Fragment {
    private ImageButton btn_filter, btn_ordenar, btn_crear;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Referencias de botones
        btn_filter = view.findViewById(R.id.btn_filter);
        btn_ordenar = view.findViewById(R.id.btn_ordenar);
        btn_crear = view.findViewById(R.id.btn_crear);
        recyclerViewTasks = view.findViewById(R.id.recycler_view_tasks);

        // Configurar el RecyclerView
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        recyclerViewTasks.setAdapter(taskAdapter);

        // Cargar tareas desde Firebase
        cargarTareasDesdeFirebase();

        // Botón para crear nueva tarea
        btn_crear.setOnClickListener(v -> replaceFragment(new crearTarea()));

        // Botones de filtrar y ordenar (a implementar según tu lógica)
        btn_filter.setOnClickListener(v -> showFilterDialog());
        btn_ordenar.setOnClickListener(v -> showSortDialog());

        return view;
    }

    // Método para cargar tareas desde Firebase
    private void cargarTareasDesdeFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tareas").child(userId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    taskList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Task tarea = snapshot.getValue(Task.class);
                        taskList.add(tarea);
                    }
                    taskAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    //---------------FILTRAR TAREA-----------------

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.popup_filtro, null);
        builder.setView(dialogView)
                .setTitle("Filtrar Tareas")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Aplicar", (dialog, which) -> {
                    RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group_status);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    String selectedFilter = "";

                    // Obtener el texto del RadioButton seleccionado
                    if (selectedId != -1) { // Asegúrate de que hay una opción seleccionada
                        RadioButton selectedRadioButton = dialogView.findViewById(selectedId);
                        selectedFilter = selectedRadioButton.getText().toString();
                    }

                    // Llama a applyFilter con el filtro seleccionado
                    //applyFilter(selectedFilter);(Esto igual)
                });
        builder.create().show();
    }
// Lo puse asi porque me daba problmitas con lo que estoy haciendo ahora :V
    /*private void applyFilter(String filterOption) {
        List<Task> filteredTasks = new ArrayList<>();

        switch (filterOption) {
            case "Personal (Amarillo)":
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Personal") && task.getColor().equals("Amarillo")) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "Trabajo (Verde)":
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Trabajo") && task.getColor().equals("Verde")) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "Estudio (Azul)":
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Estudio") && task.getColor().equals("Azul")) {
                        filteredTasks.add(task);
                    }
                }
                break;

            case "Otros (Rojo)":
                for (Task task : taskList) {
                    if (task.getCategoria().equals("Otros") && task.getColor().equals("Rojo")) {
                        filteredTasks.add(task);
                    }
                }
                break;
        }

        // Aquí puedes actualizar la vista del RecyclerView o ListView con las tareas filtradas
        updateTaskListView(filteredTasks);
    }*/

    //------------ORDENAR TAREA----------------

    private void showSortDialog() {
        String[] ordenOptions = {"A-Z", "Z-A"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleccionar Ordenación")
                .setItems(ordenOptions, (dialog, which) -> {
                    String selectedSort = ordenOptions[which];
                    applyOrden(selectedSort);
                })
                .show();
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

    //-------------------------------------------------------

    // Cargar algunas tareas de ejemplo esto tambien :V
    /*private void loadTasks() {
        taskList.add(new Task("Comprar comida", "Ir al supermercado", "Personal", "amarillo"));
        taskList.add(new Task("Reunión de trabajo", "Zoom a las 10am", "trabajo", "verde"));
        taskAdapter.notifyDataSetChanged(); // Actualizar la lista en el RecyclerView
    }*/

    private void updateTaskListView(List<Task> filteredTasks) {
        taskAdapter.updateTaskList(filteredTasks);
        taskAdapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager(); // Usa getParentFragmentManager si el fragmento está anidado
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment); // Asegúrate de que este ID coincida con tu contenedor en el layout de la actividad
        fragmentTransaction.addToBackStack(null); // Para permitir volver al fragmento anterior
        fragmentTransaction.commit();
    }
}