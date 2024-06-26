package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.Sombrilla;
import com.example.hamacasbackend.entidades.Reserva;
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
            LOGGER.info("No se encontraron sombrillas disponibles.");
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

    @PatchMapping("/updateSombrilla/{id}")
    public ResponseEntity<Sombrilla> partialUpdateSombrilla(@PathVariable("id") Long id, @RequestBody Map<String, Object> updates) {
        return sombrillaRepositorio.findById(id).map(sombrilla -> {
            if (updates.containsKey("reservada")) {
                sombrilla.setReservada((Boolean) updates.get("reservada"));
            }
            if (updates.containsKey("ocupada")) {
                sombrilla.setOcupada((Boolean) updates.get("ocupada"));
            }
            if (updates.containsKey("cantidadHamacas")) {
                sombrilla.setCantidadHamacas((String) updates.get("cantidadHamacas"));
            }
            if (updates.containsKey("pagada")) {
                sombrilla.setPagada((Boolean) updates.get("pagada"));
            }
            LOGGER.info("Actualizando sombrilla con ID " + id + " con datos: " + updates);
            return new ResponseEntity<>(sombrillaRepositorio.save(sombrilla), HttpStatus.OK);
        }).orElseGet(() -> {
            LOGGER.info("No se encontró sombrilla con ID " + id);
            return ResponseEntity.notFound().build();
        });
    }

    @PatchMapping("/updateReservaSombrilla/{id}")
    public ResponseEntity<?> updateSombrillaReserva(@PathVariable("id") Long id, @RequestParam("idReserva") Long idReserva, @RequestParam("cantidadHamacas") String cantidadHamacas) {
        LOGGER.info("Intentando actualizar sombrilla ID " + id + " con reserva ID " + idReserva + " y cantidadHamacas " + cantidadHamacas);
        return sombrillaRepositorio.findById(id).map(sombrilla -> {
            Reserva reserva = reservaRepositorio.findById(idReserva)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva not found"));
            if (!sombrilla.getReservas().contains(reserva)) {
                sombrilla.getReservas().add(reserva);
                LOGGER.info("Reserva ID " + idReserva + " agregada a sombrilla ID " + id);
            }
            sombrilla.setCantidadHamacas(cantidadHamacas);
            sombrilla.setReservada(true);
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
            List<Integer> ids = convertToListOfIntegers(updateRequest.get("ids"));
            double precio = (Double) updateRequest.get("precio");
            List<Sombrilla> updatedSombrillas = new ArrayList<>();
            ids.forEach(id -> sombrillaRepositorio.findById(Long.valueOf(id)).ifPresent(sombrilla -> {
                sombrilla.setPrecio(precio);
                sombrillaRepositorio.save(sombrilla);
                updatedSombrillas.add(sombrilla);
            }));
            return ResponseEntity.ok(updatedSombrillas);
        } catch (ClassCastException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }
    }

    @PatchMapping("/updatePagoSombrilla/{id}")
    public ResponseEntity<Sombrilla> updatePagoSombrilla(@PathVariable("id") Long id, @RequestParam("pagada") boolean pagada) {
        return sombrillaRepositorio.findById(id).map(sombrilla -> {
            sombrilla.setPagada(pagada);
            return new ResponseEntity<>(sombrillaRepositorio.save(sombrilla), HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private List<Integer> convertToListOfIntegers(Object obj) {
        if (obj instanceof List<?> list) {
            List<Integer> result = new ArrayList<>();
            for (Object element : list) {
                if (element instanceof Integer) {
                    result.add((Integer) element);
                } else {
                    throw new ClassCastException("Expected a list of integers");
                }
            }
            return result;
        } else {
            throw new ClassCastException("Expected a list");
        }
    }
}