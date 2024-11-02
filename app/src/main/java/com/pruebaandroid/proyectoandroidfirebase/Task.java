package com.pruebaandroid.proyectoandroidfirebase;

import java.util.ArrayList;
import java.util.List;

public class Task {
    // Variable estática para generar IDs autoincrementales
    private static int idCounter = 1;

    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private List<String> etiquetas;
    private int prioridad; // Puede ser "Personal", "Trabajo", "Estudio", etc.
    private boolean completada; // Para marcar si la tarea está completada

    // Constructor principal
    public Task(String titulo, String descripcion, String fecha, int prioridad) {
        this.id = idCounter++; // Asigna el ID autoincremental y luego lo incrementa
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.etiquetas = new ArrayList<>();
        this.prioridad = prioridad;
        this.completada = false; // Por defecto, las tareas no están completadas
    }

    // Constructor vacío para Firebase
    public Task() {
        this.etiquetas = new ArrayList<>(); // Asegura que la lista de etiquetas no sea nula
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getFecha() { return fecha; }
    public List<String> getEtiquetas() { return etiquetas; }
    public int getPrioridad() { return prioridad; }
    public boolean isCompletada() { return completada; }

    // Setters
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setEtiquetas(List<String> etiquetas) { this.etiquetas = etiquetas; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    // Método adicional para agregar etiquetas de una en una
    public void addEtiqueta(String etiqueta) {
        if (!etiquetas.contains(etiqueta)) {
            etiquetas.add(etiqueta);
        }
    }
}
