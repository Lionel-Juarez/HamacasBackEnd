package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.hamacas.Hamaca;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.example.hamacasbackend.repositorios.HamacaRepositorio;
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
@RequestMapping("/api/hamacas")
public class HamacaController {

    private static final java.util.logging.Logger LOGGER = Logger.getLogger(ReservaController.class.getName());

    private final HamacaRepositorio hamacaRepositorio;
    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    public HamacaController(HamacaRepositorio hamacaRepositorio) {
        this.hamacaRepositorio = hamacaRepositorio;
    }

    @GetMapping("/hamacas")
    public ResponseEntity<List<Hamaca>> getAllHamacas() {
        LOGGER.info("Iniciando la carga de todas las hamacas.");
        List<Hamaca> hamacas = new ArrayList<>();
        for (Hamaca hamaca : hamacaRepositorio.findAll()) {
            hamacas.add(hamaca);
//            LOGGER.info("Hamaca:" + hamaca.getReservas());

        }

        if (hamacas.isEmpty()) {
            LOGGER.info("No se encontraron hamacas en la base de datos.");
            return ResponseEntity.noContent().build();
        } else {
            LOGGER.info("Se cargaron " + hamacas.size() + " hamacas.");
            return ResponseEntity.ok(hamacas);
        }
    }


    @PostMapping("/nuevaHamaca")
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
            hamaca.setOcupada(hamacaDetails.isOcupada());
            hamaca.setNumeroHamaca(hamacaDetails.getNumeroHamaca());
            if (hamacaDetails.getReservas() != null && !hamacaDetails.getReservas().isEmpty()) {
                List<Long> reservaIds = hamacaDetails.getReservas().stream().map(Reserva::getIdReserva).collect(Collectors.toList());
                List<Reserva> reservas = new ArrayList<>();
                for (Long reservaId : reservaIds) {
                    reservaRepositorio.findById(reservaId).ifPresent(reservas::add);
                }
                hamaca.setReservas(reservas);
                hamaca.setReservas(reservas);
            }
            return new ResponseEntity<>(hamacaRepositorio.save(hamaca), HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PatchMapping("/updateReservaHamaca/{id}")
    public ResponseEntity<Hamaca> updateHamacaReserva(@PathVariable("id") Long id, @RequestParam("idReserva") Long idReserva, @RequestParam("lado") String lado) {
        return hamacaRepositorio.findById(id).map(hamaca -> {
            Reserva reserva = reservaRepositorio.findById(idReserva)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva not found"));
            if (!hamaca.getReservas().contains(reserva)) {
                hamaca.getReservas().add(reserva);
                LOGGER.info("Reserva ID {} agregada a hamaca ID {}" + idReserva + id);

                hamaca.setLado(lado); // Establecer el lado de la hamaca con el valor proporcionado desde el frontend
            }
            Hamaca updatedHamaca = hamacaRepositorio.save(hamaca);
            return new ResponseEntity<>(updatedHamaca, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @Transactional
    @PatchMapping("/updatePrecioHamacas")
    public ResponseEntity<?> updatePrecioHamacas(@RequestBody Map<String, Object> updateRequest) {
        try {
            List<Integer> ids = (List<Integer>) updateRequest.get("ids");
            double precio = (Double) updateRequest.get("precio");
            List<Hamaca> updatedHamacas = new ArrayList<>();
            ids.forEach(id -> {
                hamacaRepositorio.findById(Long.valueOf(id)).ifPresent(hamaca -> {
                    hamaca.setPrecio(precio);
                    hamacaRepositorio.save(hamaca);
                    updatedHamacas.add(hamaca);
                });
            });
            return ResponseEntity.ok(updatedHamacas);
        } catch (ClassCastException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }
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