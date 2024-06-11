package com.example.hamacasbackend.controllers;
import com.example.hamacasbackend.entidades.cliente.Cliente;
import com.example.hamacasbackend.entidades.sombrillas.Sombrilla;import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.example.hamacasbackend.entidades.reservas.ReservaDTO;
import com.example.hamacasbackend.repositorios.ClienteRepositorio;
import com.example.hamacasbackend.repositorios.SombrillaRepositorio;
import com.example.hamacasbackend.repositorios.ReservaRepositorio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private SombrillaRepositorio sombrillaRepositorio;
    private static final Logger LOGGER = Logger.getLogger(ReservaController.class.getName());

    @GetMapping("/")
    public ResponseEntity<List<Reserva>> getAllReservas(@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fecha, @RequestParam(required = false) String nombre, @RequestParam(required = false) String estado) {
        List<Reserva> reservas;
        if (estado != null) {
            reservas = reservaRepositorio.findByEstado(estado);
        } else if (nombre != null) {
            reservas = reservaRepositorio.findByNombre(nombre);
        } else if (fecha != null) {
            LocalDateTime startOfDay = fecha.atStartOfDay();
            LocalDateTime endOfDay = fecha.plusDays(1).atStartOfDay();
            reservas = reservaRepositorio.findByFechaReserva(startOfDay, endOfDay);
        } else {
            Iterable<Reserva> result = reservaRepositorio.findAll();
            reservas = StreamSupport.stream(result.spliterator(), false)
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }


    @GetMapping("/fecha-estado")
    public ResponseEntity<List<Reserva>> getReservasByFechaAndEstado(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fecha,
            @RequestParam String estado) {
        LocalDateTime startOfDay = fecha.atStartOfDay();
        LocalDateTime endOfDay = fecha.plusDays(1).atStartOfDay();
        List<Reserva> reservas = reservaRepositorio.findByFechaReservaAndEstado(startOfDay, endOfDay, estado);
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @PostMapping("/nuevaReserva")
    @Transactional
    public ResponseEntity<?> createReserva(@RequestBody ReservaDTO reservaDTO) {
        try {
            Cliente cliente = clienteRepositorio.findById(reservaDTO.getIdCliente())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
            List<Sombrilla> sombrillas = StreamSupport.stream(sombrillaRepositorio.findAllById(reservaDTO.getIdSombrillas()).spliterator(), false)
                    .collect(Collectors.toList());

            if (sombrillas.isEmpty()) {
                LOGGER.info("No se encontraron sombrillas con los IDs proporcionados: {}" + reservaDTO.getIdSombrillas());
                return ResponseEntity.badRequest().body("No se encontraron sombrillas con los IDs proporcionados.");
            }

            Reserva reserva = new Reserva();
            reserva.setCliente(cliente);
            reserva.setEstado(reservaDTO.getEstado());
            reserva.setPagada(reservaDTO.isPagada());
            reserva.setMetodoPago(reservaDTO.getMetodoPago());
            reserva.setHoraLlegada(reservaDTO.getHoraLlegada());
            reserva.setFechaReserva(reservaDTO.getFechaReserva());
            reserva.setFechaReservaRealizada(reservaDTO.getFechaReservaRealizada());
            reserva.setFechaPago(reservaDTO.getFechaPago());
            reserva.setSombrillas(sombrillas);
            sombrillas.forEach(h -> h.getReservas().add(reserva));

            reservaRepositorio.save(reserva);
            sombrillaRepositorio.saveAll(sombrillas);

            LOGGER.info("Reserva creada y asociada con sombrillas exitosamente. ID de Reserva: {}" + reserva.getIdReserva());
            return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
        } catch (DateTimeParseException e) {
            LOGGER.info("Error de formato de fecha al crear reserva" + e);
            return ResponseEntity.badRequest().body("Formato de fecha incorrecto");
        } catch (ResponseStatusException e) {
            LOGGER.info("Error al crear la reserva: " + e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            LOGGER.info("Error al crear la reserva" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la reserva");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable("id") Long id) {
        Optional<Reserva> reservaData = reservaRepositorio.findById(id);
        return reservaData.map(reserva -> new ResponseEntity<>(reserva, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/actualizarReserva/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody ReservaDTO reservaDTO) {
        return reservaRepositorio.findById(id).map(existingReserva -> {
            if ("Ha llegado".equals(reservaDTO.getEstado()) || "Cancelada".equals(reservaDTO.getEstado()) ) {
                for (Sombrilla sombrilla : existingReserva.getSombrillas()) {
                    sombrilla.getReservas().remove(existingReserva);
                    sombrillaRepositorio.save(sombrilla);
                }
                existingReserva.getSombrillas().clear();
            }

            existingReserva.setPagada(reservaDTO.isPagada());
            existingReserva.setMetodoPago(reservaDTO.getMetodoPago());
            existingReserva.setFechaPago(reservaDTO.getFechaPago());
            existingReserva.setEstado(reservaDTO.getEstado());

            reservaRepositorio.save(existingReserva);
            return ResponseEntity.ok(existingReserva);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Reserva>> getReservasByIdCliente(@PathVariable Long idCliente) {
        List<Reserva> reservas = reservaRepositorio.findByClienteId(idCliente);
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @GetMapping("/countByCliente/{idCliente}")
    public ResponseEntity<Long> countReservasByCliente(@PathVariable Long idCliente) {
        long count = reservaRepositorio.countByClienteId(idCliente);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    @GetMapping("/countByClientePendientes/{idCliente}")
    public ResponseEntity<Long> countReservasByClientePendientes(@PathVariable Long idCliente) {
        long count = reservaRepositorio.countByClienteIdAndEstado(idCliente, "Pendiente");
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
