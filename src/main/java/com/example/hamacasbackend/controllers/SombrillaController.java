package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.sombrillas.Sombrilla;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.example.hamacasbackend.repositorios.SombrillaRepositorio;
import com.example.hamacasbackend.repositorios.ReservaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sombrillas")
public class SombrillaController {

    private static final java.util.logging.Logger LOGGER = Logger.getLogger(ReservaController.class.getName());

    private final SombrillaRepositorio sombrillaRepositorio;
    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    public SombrillaController(SombrillaRepositorio sombrillaRepositorio) {
        this.sombrillaRepositorio = sombrillaRepositorio;
    }

    @GetMapping("/sombrillas")
    public ResponseEntity<List<Sombrilla>> getAllSombrillas() {
        LOGGER.info("Iniciando la carga de todas las sombrillas.");
        List<Sombrilla> sombrillas = new ArrayList<>();
        for (Sombrilla sombrilla : sombrillaRepositorio.findAll()) {
            sombrillas.add(sombrilla);
        }

        if (sombrillas.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(sombrillas);
        }
    }


    @PostMapping("/nuevaSombrilla")
    public ResponseEntity<Sombrilla> createSombrilla(@RequestBody Sombrilla sombrilla) {
        try {
            Sombrilla createdSombrilla = sombrillaRepositorio.save(sombrilla);
            return new ResponseEntity<>(createdSombrilla, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/getSombrilla/{id}")
    public ResponseEntity<Sombrilla> getSombrillaById(@PathVariable("id") Long id) {
        Optional<Sombrilla> sombrillaFound = sombrillaRepositorio.findById(id);
        return sombrillaFound.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/updateSombrilla/{id}")
    public ResponseEntity<Sombrilla> updateSombrilla(@PathVariable("id") Long id, @RequestBody Sombrilla sombrillaDetails) {
        return sombrillaRepositorio.findById(id).map(sombrilla -> {
            sombrilla.setPrecio(sombrillaDetails.getPrecio());
            sombrilla.setOcupada(sombrillaDetails.isOcupada());
            sombrilla.setNumeroSombrilla(sombrillaDetails.getNumeroSombrilla());
            if (sombrillaDetails.getReservas() != null && !sombrillaDetails.getReservas().isEmpty()) {
                List<Long> reservaIds = sombrillaDetails.getReservas().stream().map(Reserva::getIdReserva).collect(Collectors.toList());
                List<Reserva> reservas = new ArrayList<>();
                for (Long reservaId : reservaIds) {
                    reservaRepositorio.findById(reservaId).ifPresent(reservas::add);
                }
                sombrilla.setReservas(reservas);
                sombrilla.setReservas(reservas);
            }
            return new ResponseEntity<>(sombrillaRepositorio.save(sombrilla), HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PatchMapping("/updateReservaSombrilla/{id}")
    public ResponseEntity<?> updateSombrillaReserva(@PathVariable("id") Long id, @RequestParam("idReserva") Long idReserva, @RequestParam("cantidadHamacas") int cantidadHamacas) {
        LOGGER.info("Intentando actualizar sombrilla ID " + id + " con reserva ID " + idReserva + " y cantidadHamacas " + cantidadHamacas);
        return sombrillaRepositorio.findById(id).map(sombrilla -> {
            Reserva reserva = reservaRepositorio.findById(idReserva)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva not found"));
            if (!sombrilla.getReservas().contains(reserva)) {
                sombrilla.getReservas().add(reserva);
                LOGGER.info("Reserva ID " + idReserva + " agregada a sombrilla ID " + id);
            }
            sombrilla.setCantidadHamacas(cantidadHamacas);
            sombrillaRepositorio.save(sombrilla);
            LOGGER.info("Sombrilla ID " + id + " actualizada con el cantidadHamacas " + cantidadHamacas);
            return ResponseEntity.ok(sombrilla);
        }).orElseGet(() -> {
            LOGGER.info("No se encontró sombrilla con ID " + id);
            return ResponseEntity.notFound().build();
        });
    }





    @Transactional
    @PatchMapping("/updatePrecioSombrillas")
    public ResponseEntity<?> updatePrecioSombrillas(@RequestBody Map<String, Object> updateRequest) {
        try {
            List<Integer> ids = (List<Integer>) updateRequest.get("ids");
            double precio = (Double) updateRequest.get("precio");
            List<Sombrilla> updatedSombrillas = new ArrayList<>();
            ids.forEach(id -> {
                sombrillaRepositorio.findById(Long.valueOf(id)).ifPresent(sombrilla -> {
                    sombrilla.setPrecio(precio);
                    sombrillaRepositorio.save(sombrilla);
                    updatedSombrillas.add(sombrilla);
                });
            });
            return ResponseEntity.ok(updatedSombrillas);
        } catch (ClassCastException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }
    }

    @DeleteMapping("/deleteSombrilla/{id}")
    public ResponseEntity<HttpStatus> deleteSombrilla(@PathVariable("id") Long id) {
        try {
            sombrillaRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}