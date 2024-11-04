package com.pruebaandroid.proyectoandroidfirebase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;


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

        // Inicializar la lista de tareas
        taskList = new ArrayList<>();

        // Referencias de botones y RecyclerView
        btn_filter = view.findViewById(R.id.btn_filter);
        btn_ordenar = view.findViewById(R.id.btn_ordenar);
        btn_crear = view.findViewById(R.id.btn_crear);
        recyclerViewTasks = view.findViewById(R.id.recycler_view_tasks);

        // Configurar el RecyclerView
        taskAdapter = new TaskAdapter(taskList);
        recyclerViewTasks.setAdapter(taskAdapter);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cargar tareas desde Firebase
        cargarTareasDesdeFirebase();

        // Botón para crear nueva tarea
        btn_crear.setOnClickListener(v -> replaceFragment(new crearTarea()));

        // Botones de filtrar y ordenar (a implementar según tu lógica)
        btn_filter.setOnClickListener(v -> mostrarPopupFiltro());
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

    // Método para mostrar el popup de filtrado
    private void mostrarPopupFiltro() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.popup_filtro);
        dialog.setCancelable(true);

        // Inicializar componentes del popup
        Spinner spinnerPriority = dialog.findViewById(R.id.spinner_priority);
        RadioGroup radioGroupStatus = dialog.findViewById(R.id.radio_group_status);
        Button btn_apply_filter = dialog.findViewById(R.id.btn_apply_filter);

        btn_apply_filter.setOnClickListener(v -> {
            String selectedPriority = spinnerPriority.getSelectedItem().toString();
            int selectedStatusId = radioGroupStatus.getCheckedRadioButtonId();
            String selectedStatus = "todos";

            if (selectedStatusId == R.id.radio_completed) {
                selectedStatus = "completadas";
            } else if (selectedStatusId == R.id.radio_pending) {
                selectedStatus = "pendientes";
            }

            // Filtrar la lista de tareas
            List<Task> filteredTasks = filtrarTareas(selectedPriority, selectedStatus);
            updateTaskListView(filteredTasks); // Actualiza el RecyclerView

            dialog.dismiss(); // Cerrar el popup después de aplicar el filtro
        });

        dialog.show();
    }

    // Filtrar tareas
    private List<Task> filtrarTareas(String priority, String status) {
        List<Task> filteredList = new ArrayList<>();

        for (Task task : taskList) {
            boolean matchesPriority = priority.equals("Todas") || task.getPrioridad() == Integer.parseInt(priority);
            boolean matchesStatus;

            if (status.equals("completadas")) {
                matchesStatus = task.isCompletada();
            } else if (status.equals("pendientes")) {
                matchesStatus = !task.isCompletada();
            } else {
                matchesStatus = true; // Incluye todas si no se selecciona un estado
            }

            if (matchesPriority && matchesStatus) {
                filteredList.add(task);
            }
        }

        return filteredList;
    }


    // Ordenar tareas
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
            Collections.sort(taskList, (task1, task2) -> task1.getTitulo().compareToIgnoreCase(task2.getTitulo()));
        } else if (ordenOption.equals("Z-A")) {
            Collections.sort(taskList, (task1, task2) -> task2.getTitulo().compareToIgnoreCase(task1.getTitulo()));
        }
        updateTaskListView(taskList); // Actualiza la vista con la lista de tareas ordenadas
    }

    private void updateTaskListView(List<Task> filteredTasks) {
        taskAdapter.updateTaskList(filteredTasks);
        taskAdapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}