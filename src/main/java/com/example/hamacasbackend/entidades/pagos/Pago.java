package com.example.hamacasbackend.entidades.pagos;

import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "idReserva", referencedColumnName = "idReserva")
    private Reserva reserva;

    private double cantidad;
    private String metodoPago;
    private boolean pagado;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaPago;

    private String detallesPago;

    private String tipoHamaca;
}
