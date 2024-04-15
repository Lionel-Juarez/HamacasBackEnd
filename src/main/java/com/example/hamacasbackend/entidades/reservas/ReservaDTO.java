package com.example.hamacasbackend.entidades.reservas;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data // Esta anotación de Lombok genera todos los getters, setters, toString, equals y hashCode.
public class ReservaDTO {
    private Long idHamaca;
    private Long idCliente;
    private Long idUsuario;
    private String estado;
    private boolean pagada;
    private String metodoPago;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fechaReserva;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fechaPago;
}
