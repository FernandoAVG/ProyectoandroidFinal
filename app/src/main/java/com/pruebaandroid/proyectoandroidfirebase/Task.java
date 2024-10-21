package com.pruebaandroid.proyectoandroidfirebase;

public class Task {
    //clase java para las tareas

    private int id; //ola brayan si puedes has el id autoincremental asi el usuario no se preocupa de poner id al crear su tarea
    private String titulo;
    private String descripcion;
    private String color;
    private String categoria; // Puede ser "Personal", "Trabajo", "Estudio", etc.

    // Constructor, getters y setters
    public Task(String titulo, String descripcion, String categoria, String color) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.color = color;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getColor() { return color; }
    public String getCategoria() { return categoria; }

}
