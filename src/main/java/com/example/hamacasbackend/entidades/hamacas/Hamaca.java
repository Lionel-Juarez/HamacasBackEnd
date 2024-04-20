package com.example.hamacasbackend.entidades.hamacas;

import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hamaca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHamaca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idReserva", nullable = true)
    @JsonBackReference
    private Reserva reserva;

    private double precio;
    private boolean reservada;
    private boolean ocupada;
    private int planoId;
}
