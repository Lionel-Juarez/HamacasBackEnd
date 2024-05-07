package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.hamacas.Hamaca;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HamacaRepositorio extends CrudRepository<Hamaca, Long> {
}

