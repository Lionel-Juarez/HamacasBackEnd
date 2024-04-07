package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.hamacas.Hamaca;
import com.example.hamacasbackend.entidades.reportes.Reporte;
import com.example.hamacasbackend.repositorios.HamacaRepositorio;
import com.example.hamacasbackend.repositorios.ReporteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hamacas")
public class HamacaController {
    private final HamacaRepositorio hamacaRepositorio;

    @Autowired
    public HamacaController(HamacaRepositorio hamacaRepositorio) {
        this.hamacaRepositorio = hamacaRepositorio;
    }

    @GetMapping("/")
    public ResponseEntity<List<Hamaca>> getAllHamacas(){
        List<Hamaca> hamacas = new ArrayList<>();
        hamacaRepositorio.findAll().forEach(hamacas::add);
        return ResponseEntity.ok(hamacas);
    }

    @PostMapping("/newHamaca")
    public ResponseEntity<Hamaca> createHamaca(@RequestBody Hamaca hamaca) {
        try {
            Hamaca createdHamaca = hamacaRepositorio.save(hamaca);
            return new ResponseEntity<>(createdHamaca, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getHamaca/{id}")
    public ResponseEntity<Hamaca> getHamacaById(@PathVariable("id") Long id) {
        Optional<Hamaca> hamacaFound = hamacaRepositorio.findById(id);
        return hamacaFound.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateHamaca/{id}")
    public ResponseEntity<Hamaca> updateHamaca(@PathVariable("id") Long id, @RequestBody Hamaca hamacaDetails) {
        return hamacaRepositorio.findById(id).map(hamaca -> {
            hamaca.setPrecio(hamacaDetails.getPrecio());
            hamaca.setReservada(hamacaDetails.isReservada());
            hamaca.setOcupada(hamacaDetails.isOcupada());
            Hamaca updatedHamaca = hamacaRepositorio.save(hamaca);
            return new ResponseEntity<>(updatedHamaca, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteHamaca/{id}")
    public ResponseEntity<HttpStatus> deleteHamaca(@PathVariable("id") Long id) {
        try {
            hamacaRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}