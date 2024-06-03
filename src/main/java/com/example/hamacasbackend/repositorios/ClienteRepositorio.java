package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.cliente.Cliente;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ClienteRepositorio extends CrudRepository<Cliente,Long> {
    Optional<Cliente> findByEmail(String email);
}
