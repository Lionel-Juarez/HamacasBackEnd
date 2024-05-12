package com.example.hamacasbackend.controllers;
import com.example.hamacasbackend.entidades.cliente.Cliente;
import com.example.hamacasbackend.entidades.sombrillas.Sombrilla;import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.example.hamacasbackend.entidades.reservas.ReservaDTO;
import com.example.hamacasbackend.entidades.usuarios.Usuario;
import com.example.hamacasbackend.repositorios.ClienteRepositorio;
import com.example.hamacasbackend.repositorios.SombrillaRepositorio;
import com.example.hamacasbackend.repositorios.ReservaRepositorio;
import com.example.hamacasbackend.repositorios.UsuarioRepositorio;
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
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private SombrillaRepositorio sombrillaRepositorio;
    private static final Logger LOGGER = Logger.getLogger(ReservaController.class.getName());

    @GetMapping("/")
    public ResponseEntity<List<Reserva>> getAllReservas(@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fecha, String nombre, String estado) {
        List<Reserva> reservas;
        if (estado != null){
            reservas = reservaRepositorio.findByEstado(estado);
        }
        else if (nombre != null) {
            reservas = reservaRepositorio.findByNombre(nombre);
        }
        else if (fecha != null) {
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
    @PostMapping("/nuevaReserva")
    @Transactional
    public ResponseEntity<?> createReserva(@RequestBody ReservaDTO reservaDTO) {
        try {
            Cliente cliente = clienteRepositorio.findById(reservaDTO.getIdCliente())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
            Usuario usuario = usuarioRepositorio.findById(reservaDTO.getIdUsuario())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            List<Sombrilla> sombrillas = StreamSupport.stream(sombrillaRepositorio.findAllById(reservaDTO.getIdSombrillas()).spliterator(), false)
                    .collect(Collectors.toList());

            if (sombrillas.isEmpty()) {
                LOGGER.info("No se encontraron sombrillas con los IDs proporcionados: {}" + reservaDTO.getIdSombrillas());
                return ResponseEntity.badRequest().body("No se encontraron sombrillas con los IDs proporcionados.");
            }

            Reserva reserva = new Reserva();
            reserva.setCliente(cliente);
            reserva.setCreadaPor(usuario);
            reserva.setEstado(reservaDTO.getEstado());
            reserva.setPagada(reservaDTO.isPagada());
            reserva.setMetodoPago(reservaDTO.getMetodoPago());
            reserva.setCantidadHamacas(reservaDTO.getCantidadHamacas());
            reserva.setHoraLlegada(reservaDTO.getHoraLlegada());
            reserva.setFechaReserva(reservaDTO.getFechaReserva());
            reserva.setFechaPago(reservaDTO.getFechaPago());
            reserva.setSombrillas(sombrillas);
            sombrillas.forEach(h -> h.getReservas().add(reserva));
            reservaRepositorio.save(reserva);
            sombrillaRepositorio.saveAll(sombrillas);


            reservaRepositorio.save(reserva);
            sombrillaRepositorio.saveAll(sombrillas);
            LOGGER.info("Reserva creada y asociada con sombrillas exitosamente. ID de Reserva: {}"+ reserva.getIdReserva());
            return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
        } catch (DateTimeParseException e) {
            LOGGER.info("Error de formato de fecha al crear reserva" +e);
            return ResponseEntity.badRequest().body("Formato de fecha incorrecto");
        } catch (Exception e) {
            LOGGER.info("Error al crear la reserva" +e);
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
            existingReserva.setCliente(clienteRepositorio.findById(reservaDTO.getIdCliente()).orElse(null));
            existingReserva.setCreadaPor(usuarioRepositorio.findById(reservaDTO.getIdUsuario()).orElse(null));
            existingReserva.setEstado(reservaDTO.getEstado());
            existingReserva.setPagada(reservaDTO.isPagada());
            existingReserva.setMetodoPago(reservaDTO.getMetodoPago());
            existingReserva.setFechaPago(reservaDTO.getFechaPago());
            existingReserva.setFechaReserva(reservaDTO.getFechaReserva());
            existingReserva.setFechaReservaRealizada(reservaDTO.getFechaReservaRealizada());


            List<Sombrilla> updatedSombrillas = StreamSupport.stream(sombrillaRepositorio.findAllById(reservaDTO.getIdSombrillas()).spliterator(), false)
                    .collect(Collectors.toList());
            existingReserva.getSombrillas().clear();
            existingReserva.setSombrillas(updatedSombrillas);
            updatedSombrillas.forEach(h -> h.getReservas().add(existingReserva));

            reservaRepositorio.save(existingReserva);
            return ResponseEntity.ok(existingReserva);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


//    //Añadir funcion eliminarReserva sin eliminar la sombrilla
//    @DeleteMapping("/eliminarReserva/{id}")
//    public ResponseEntity<HttpStatus> deleteReserva(@PathVariable("id") Long id) {
//        try {
//            Optional<Reserva> reserva = reservaRepositorio.findById(id);
//            if (reserva.isPresent()) {
//                Reserva reservaAEliminar = reserva.get();
//                // Desvincular las sombrillas asociadas
//                for (Sombrilla sombrilla : reservaAEliminar.getSombrillas()) {
//                    sombrilla.setReservas(null);
//                    sombrillaRepositorio.save(sombrilla);
//                }
//                // Ahora que las sombrillas están desvinculadas, eliminamos la reserva
//                reservaRepositorio.delete(reservaAEliminar);
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
