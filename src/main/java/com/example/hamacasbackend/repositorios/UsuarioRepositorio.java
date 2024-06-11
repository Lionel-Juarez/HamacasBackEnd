package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Long> { }
