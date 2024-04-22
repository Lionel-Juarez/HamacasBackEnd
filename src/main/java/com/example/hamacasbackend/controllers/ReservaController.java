package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.cliente.Cliente;
import com.example.hamacasbackend.entidades.hamacas.Hamaca;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.example.hamacasbackend.entidades.reservas.ReservaDTO;
import com.example.hamacasbackend.entidades.usuarios.Usuario;
import com.example.hamacasbackend.repositorios.ClienteRepositorio;
import com.example.hamacasbackend.repositorios.HamacaRepositorio;
import com.example.hamacasbackend.repositorios.ReservaRepositorio;
import com.example.hamacasbackend.repositorios.UsuarioRepositorio;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

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
    private HamacaRepositorio hamacaRepositorio;
    private static final Logger LOGGER = Logger.getLogger(ReservaController.class.getName());


    @Autowired
    public ReservaController(ReservaRepositorio reservaRepositorio) {
        this.reservaRepositorio = reservaRepositorio;
    }

    @GetMapping("/")
    public ResponseEntity<List<Reserva>> getAllReservas() {
        List<Reserva> reservas = new ArrayList<>();
        reservaRepositorio.findAll().forEach(reservas::add); // Convierte Iterable a List
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }


    @PostMapping("/nuevaReserva")
    @Transactional
    public ResponseEntity<?> createReserva(@RequestBody ReservaDTO reservaDTO) {
        if (reservaDTO.getIdCliente() == null || reservaDTO.getIdUsuario() == null || reservaDTO.getIdHamacas() == null || reservaDTO.getIdHamacas().isEmpty()) {
            LOGGER.warning("Solicitud de reserva recibida con uno o más IDs nulos.");
            return ResponseEntity.badRequest().body("Solicitud inválida: 'idCliente', 'idUsuario' y 'idHamacas' son requeridos y no deben ser nulos.");
        }

        try {
            List<Hamaca> hamacas = (List<Hamaca>) hamacaRepositorio.findAllById(reservaDTO.getIdHamacas());
            if (hamacas.isEmpty()) {
                LOGGER.warning("No se encontraron hamacas con los IDs proporcionados.");
                return ResponseEntity.badRequest().body("No se encontraron hamacas con los IDs proporcionados.");
            }

            Cliente cliente = clienteRepositorio.findById(reservaDTO.getIdCliente())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + reservaDTO.getIdCliente()));
            Usuario usuario = usuarioRepositorio.findById(reservaDTO.getIdUsuario())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + reservaDTO.getIdUsuario()));

            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setHamacas(hamacas);
            nuevaReserva.setCliente(cliente);
            nuevaReserva.setCreadaPor(usuario);
            nuevaReserva.setEstado(reservaDTO.getEstado());
            nuevaReserva.setPagada(reservaDTO.isPagada());
            nuevaReserva.setMetodoPago(reservaDTO.getMetodoPago());
            nuevaReserva.setFechaReserva(reservaDTO.getFechaReserva());
            nuevaReserva.setFechaPago(reservaDTO.getFechaPago());

            hamacas.forEach(hamaca -> hamaca.setIdReserva(nuevaReserva));

            Reserva savedReserva = reservaRepositorio.save(nuevaReserva);
            LOGGER.info("Reserva creada con éxito: " + savedReserva.getIdReserva());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReserva);
        } catch (DataAccessException dae) {
            LOGGER.severe("Error de acceso a datos al crear reserva: " + dae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al acceder a los datos para crear la reserva.");
        } catch (Exception e) {
            LOGGER.severe("Error al crear reserva: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al crear la reserva.");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable("id") Long id) {
        Optional<Reserva> reservaData = reservaRepositorio.findById(id);
        return reservaData.map(reserva -> new ResponseEntity<>(reserva, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/actualizarReserva/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable("id") Long id, @RequestBody ReservaDTO reservaDTO) {
        Optional<Reserva> reservaData = reservaRepositorio.findById(id);

        if (reservaData.isPresent()) {
            Reserva updatedReserva = reservaData.get();
            List<Hamaca> hamacas = (List<Hamaca>) hamacaRepositorio.findAllById(reservaDTO.getIdHamacas());
            updatedReserva.getHamacas().forEach(h -> h.setIdReserva(null)); // Desasocia la reserva anterior
            updatedReserva.setHamacas(hamacas);
            for (Hamaca hamaca : hamacas) {
                hamaca.setIdReserva(updatedReserva); // Asocia la nueva reserva
            }

            updatedReserva.setCliente(clienteRepositorio.findById(reservaDTO.getIdCliente()).orElse(null));
            updatedReserva.setCreadaPor(usuarioRepositorio.findById(reservaDTO.getIdUsuario()).orElse(null));
            updatedReserva.setEstado(reservaDTO.getEstado());
            updatedReserva.setPagada(reservaDTO.isPagada());
            updatedReserva.setMetodoPago(reservaDTO.getMetodoPago());
            updatedReserva.setFechaPago(reservaDTO.getFechaPago());
            updatedReserva.setFechaReserva(reservaDTO.getFechaReserva());
            return new ResponseEntity<>(reservaRepositorio.save(updatedReserva), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Añadir funcion eliminarReserva sin eliminar la hamaca
    @DeleteMapping("/eliminarReserva/{id}")
    public ResponseEntity<HttpStatus> deleteReserva(@PathVariable("id") Long id) {
        try {
            Optional<Reserva> reserva = reservaRepositorio.findById(id);
            if (reserva.isPresent()) {
                Reserva reservaAEliminar = reserva.get();
                // Desvincular las hamacas asociadas
                for (Hamaca hamaca : reservaAEliminar.getHamacas()) {
                    hamaca.setIdReserva(null);
                    hamacaRepositorio.save(hamaca);
                }
                // Ahora que las hamacas están desvinculadas, eliminamos la reserva
                reservaRepositorio.delete(reservaAEliminar);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



//    @Transactional
//    @DeleteMapping("/eliminarReserva/{id}")
//    public ResponseEntity<HttpStatus> deleteReserva(@PathVariable("id") Long id) {
//        try {
//            Optional<Reserva> reservaOptional = reservaRepositorio.findById(id);
//            if (reservaOptional.isPresent()) {
//                Reserva reserva = reservaOptional.get();
//                List<Hamaca> hamacas = reserva.getHamacas();
//                if (hamacas != null && !hamacas.isEmpty()) {
//                    for (Hamaca hamaca : hamacas) {
//                        hamaca.setReserva(null);
//                        hamacaRepositorio.save(hamaca);
//                    }
//                    // Línea adicional para asegurarse de que se refresca el estado de la entidad antes de eliminar
//                    reservaRepositorio.flush();
//                }
//                reservaRepositorio.delete(reserva);
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
