package com.example.practica1tema3.repositorios;

import com.example.practica1tema3.entidades.Libro;
import org.springframework.data.repository.CrudRepository;

public interface LibroRepositorio extends CrudRepository<Libro,Long> {
}
