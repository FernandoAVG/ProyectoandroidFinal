package com.pruebaandroid.proyectoandroidfirebase;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.DatePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class crearTarea extends Fragment {

    private EditText editTitulo, editDescripcion, etFecha;
    private Button buttonCrear, buttonVolver;
    private Calendar calendar;
    private ChipGroup chipGroup; // Añadir ChipGroup

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_crear, container, false);

        // Referencias a los campos de texto y el botón
        editTitulo = view.findViewById(R.id.edit_Titulo);
        editDescripcion = view.findViewById(R.id.miEditText);
        buttonCrear = view.findViewById(R.id.bt_crearfrag);
        buttonVolver = view.findViewById(R.id.bt_volver);
        etFecha = view.findViewById(R.id.etFecha);
        chipGroup = view.findViewById(R.id.chip_group); // Referencia al ChipGroup
        calendar = Calendar.getInstance();
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.iconcalendar);

        // Ajustar el tamaño del ícono de calendario
        int ancho = 80;
        int alto = 80;
        drawable.setBounds(0, 0, ancho, alto);
        etFecha.setCompoundDrawables(null, null, drawable, null);

        // Configurar el selector de fecha
        etFecha.setOnClickListener(v -> showDatePicker());

        // Obtener el usuario actual de FirebaseAuth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Configurar el botón de "Crear"
        buttonCrear.setOnClickListener(v -> {
            if (user != null) {
                String userId = user.getUid();
                String titulo = editTitulo.getText().toString();
                String descripcion = editDescripcion.getText().toString();
                String fecha = etFecha.getText().toString();
                List<String> etiquetasSeleccionadas = obtenerEtiquetasSeleccionadas();

                if (!titulo.isEmpty() && !descripcion.isEmpty() && !fecha.isEmpty()) {
                    // Llamar a la función para guardar los datos en Firebase
                    guardarTareaEnFirebase(userId, titulo, descripcion, fecha, etiquetasSeleccionadas);
                } else {
                    Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón de "Volver"
        buttonVolver.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.main);
            navController.navigate(R.id.action_to_volver);
        });

        return view;
    }

    // Método para obtener las etiquetas seleccionadas
    private List<String> obtenerEtiquetasSeleccionadas() {
        List<String> etiquetas = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                etiquetas.add(chip.getText().toString());
            }
        }
        return etiquetas;
    }

    // Método para guardar los datos en Firebase
    private void guardarTareaEnFirebase(String userId, String titulo, String descripcion, String fecha, List<String> etiquetas) {
        // Obtener la referencia de Firebase Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Crear un objeto con los datos de la tarea
        Map<String, Object> tareaData = new HashMap<>();
        tareaData.put("titulo", titulo);
        tareaData.put("descripcion", descripcion);
        tareaData.put("fecha", fecha);
        tareaData.put("etiquetas", etiquetas);

        // Guardar los datos bajo el nodo "tareas/{userId}/{tareaId}"
        String tareaId = database.child("tareas").child(userId).push().getKey();
        database.child("tareas").child(userId).child(tareaId).setValue(tareaData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Mostrar mensaje de éxito
                        Toast.makeText(getContext(), "Tarea guardada correctamente", Toast.LENGTH_SHORT).show();
                        limpiarFormulario();
                    } else {
                        // Mostrar mensaje de error
                        Toast.makeText(getContext(), "Error al guardar tarea", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void limpiarFormulario() {
        editTitulo.setText("");  // Limpiar el campo del título
        editDescripcion.setText("");  // Limpiar el campo de la descripción
        etFecha.setText("");  // Limpiar el campo de la fecha

        // Limpiar las etiquetas seleccionadas
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setChecked(false);  // Desmarcar los chips seleccionados
        }
    }

    // Método para mostrar el selector de fecha
    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Actualizar el EditText con la fecha seleccionada
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etFecha.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
