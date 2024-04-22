package com.example.hamacasbackend.entidades.hamacas;

import com.example.hamacasbackend.entidades.cliente.Cliente;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "idReserva", referencedColumnName = "idReserva")
    @JsonIgnore  // Evita la serialización de esta propiedad
    private Reserva idReserva;

    @Transient // Este campo no se almacena en la base de datos, solo se usa para la serialización
    private Long reservaId;

    public Long getReservaId() {
        return idReserva != null ? idReserva.getIdReserva() : null;
    }

    private double precio;
    private boolean reservada;
    private boolean ocupada;
    private int planoId;
}