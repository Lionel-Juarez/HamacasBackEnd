package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.Libro;
import org.springframework.data.repository.CrudRepository;

public interface LibroRepositorio extends CrudRepository<Libro,Long> {
}
