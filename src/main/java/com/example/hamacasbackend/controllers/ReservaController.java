package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.example.hamacasbackend.entidades.reservas.ReservaDTO;
import com.example.hamacasbackend.repositorios.ClienteRepositorio;
import com.example.hamacasbackend.repositorios.HamacaRepositorio;
import com.example.hamacasbackend.repositorios.ReservaRepositorio;
import com.example.hamacasbackend.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Reserva> createReserva(@RequestBody ReservaDTO reservaDTO) {
        try {
            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setHamaca(hamacaRepositorio.findById(reservaDTO.getIdHamaca()).orElse(null));
            nuevaReserva.setCliente(clienteRepositorio.findById(reservaDTO.getIdCliente()).orElse(null));
            nuevaReserva.setCreadaPor(usuarioRepositorio.findById(reservaDTO.getIdUsuario()).orElse(null));
            nuevaReserva.setEstado(reservaDTO.getEstado());
            nuevaReserva.setPagada(reservaDTO.isPagada());
            nuevaReserva.setMetodoPago(reservaDTO.getMetodoPago());
            nuevaReserva.setFechaReserva(reservaDTO.getFechaReserva());
            nuevaReserva.setFechaPago(reservaDTO.getFechaPago());

            Reserva savedReserva = reservaRepositorio.save(nuevaReserva);
            return new ResponseEntity<>(savedReserva, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable("id") Long id) {
        Optional<Reserva> reservaData = reservaRepositorio.findById(id);
        return reservaData.map(reserva -> new ResponseEntity<>(reserva, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/actualizarReserva/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable("id") Long id, @RequestBody Reserva reservaDetails) {
        Optional<Reserva> reservaData = reservaRepositorio.findById(id);

        if (reservaData.isPresent()) {
            Reserva updatedReserva = reservaData.get();
            updatedReserva.setHamaca(reservaDetails.getHamaca());
            updatedReserva.setCliente(reservaDetails.getCliente());
            updatedReserva.setEstado(reservaDetails.getEstado());
            updatedReserva.setPagada(reservaDetails.isPagada());
            updatedReserva.setMetodoPago(reservaDetails.getMetodoPago());
            updatedReserva.setFechaPago(reservaDetails.getFechaPago());
            return new ResponseEntity<>(reservaRepositorio.save(updatedReserva), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminarReserva/{id}")
    public ResponseEntity<HttpStatus> deleteReserva(@PathVariable("id") Long id) {
        try {
            reservaRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
