package com.example.hamacasbackend.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro;
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "idAlumno")
    @JsonBackReference // Evita la serialización de la relación a JSON
    private Alumno alumno;

    public Libro() {

    }
    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public Libro(Long idLibro, String nombre, Alumno alumno) {
        this.idLibro = idLibro;
        this.nombre = nombre;
        this.alumno = alumno;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}
