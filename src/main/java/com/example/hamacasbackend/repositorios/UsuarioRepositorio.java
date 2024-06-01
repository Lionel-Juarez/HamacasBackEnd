package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.usuarios.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByUid(String uid);
}
