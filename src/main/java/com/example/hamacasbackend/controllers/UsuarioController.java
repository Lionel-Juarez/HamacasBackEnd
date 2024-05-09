package com.example.hamacasbackend.controllers;
import com.example.hamacasbackend.entidades.usuarios.Usuario;
import com.example.hamacasbackend.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios") // Cambiado para reflejar el enfoque en usuarios
public class UsuarioController {
    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public UsuarioController(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarioRepositorio.findAll().forEach(usuarios::add);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/nuevoUsuario")
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario createdUsuario = usuarioRepositorio.save(usuario);
            return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable("id") Long id) {
        Optional<Usuario> usuarioFound = usuarioRepositorio.findById(id);
        return usuarioFound.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizarUsuario/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuarioDetails) {
        return usuarioRepositorio.findById(id).map(usuario -> {
            usuario.setNombreUsuario(usuarioDetails.getNombreUsuario());
            usuario.setPassword(usuarioDetails.getPassword());
            usuario.setRol(usuarioDetails.getRol());
            Usuario updatedUsuario = usuarioRepositorio.save(usuario);
            return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminarUsuario/{id}")
    public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable("id") Long id) {
        try {
            usuarioRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}