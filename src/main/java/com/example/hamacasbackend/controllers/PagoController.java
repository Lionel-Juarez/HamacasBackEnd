package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.pagos.Pago;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.example.hamacasbackend.repositorios.PagoRepositorio;
import com.example.hamacasbackend.repositorios.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoRepositorio pagoRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @GetMapping
    public ResponseEntity<List<Pago>> getAllPagos(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String metodoPago,
            @RequestParam(required = false) Boolean pagado,
            @RequestParam(required = false) String tipoHamaca,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Pago> pagos;

        if (fecha != null) {
            Page<Pago> pagoPage = pagoRepositorio.findByFechaPago(fecha, pageable);
            pagos = pagoPage.getContent();
        } else if (mes != null && ano != null) {
            pagos = pagoRepositorio.findByMes(ano, mes); // Paginate this as well if needed
        } else if (ano != null) {
            pagos = pagoRepositorio.findByAno(ano); // Paginate this as well if needed
        } else if (metodoPago != null) {
            pagos = pagoRepositorio.findByMetodoPago(metodoPago); // Paginate this as well if needed
        } else if (pagado != null) {
            pagos = pagoRepositorio.findByPagado(pagado); // Paginate this as well if needed
        } else if (tipoHamaca != null) {
            pagos = pagoRepositorio.findByTipoHamaca(tipoHamaca); // Paginate this as well if needed
        } else {
            Iterable<Pago> result = pagoRepositorio.findAll();
            pagos = StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList()); // Paginate this as well if needed
        }
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    @PostMapping("/nuevoPago")
    public ResponseEntity<?> createPago(@RequestBody Pago pago) {
        try {

            Reserva reserva = reservaRepositorio.findById(pago.getReserva().getIdReserva())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

            pago.setReserva(reserva);
            Pago nuevoPago = pagoRepositorio.save(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el pago: " + e.getMessage());
        }
    }

}
