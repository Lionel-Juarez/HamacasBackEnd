package com.example.hamacasbackend.controllers;
import com.example.hamacasbackend.entidades.cliente.Cliente;
import com.example.hamacasbackend.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteRepositorio clienteRepositorio;

    @Autowired
    public ClienteController(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @GetMapping("/")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clienteRepositorio.findAll().forEach(clientes::add);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/nuevoCliente")
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        try {
            Cliente createdCliente = clienteRepositorio.save(cliente);
            return new ResponseEntity<>(createdCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable("id") Long id) {
        Optional<Cliente> clienteFound = clienteRepositorio.findById(id);
        return clienteFound.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizarCliente/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable("id") Long id, @RequestBody Cliente clienteDetails) {
        return clienteRepositorio.findById(id).map(cliente -> {
            cliente.setNombreCompleto(clienteDetails.getNombreCompleto());
            cliente.setNumeroTelefono(clienteDetails.getNumeroTelefono());
            cliente.setEmail(clienteDetails.getEmail());
            Cliente updatedCliente = clienteRepositorio.save(cliente);
            return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminarCliente/{id}")
    public ResponseEntity<HttpStatus> deleteCliente(@PathVariable("id") Long id) {
        try {
            clienteRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
