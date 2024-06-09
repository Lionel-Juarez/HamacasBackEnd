package com.example.hamacasbackend.entidades.reservas;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data // Esta anotación de Lombok genera todos los getters, setters, toString, equals y hashCode.
public class ReservaDTO {
    private Long idReserva;
    private List<Long> idSombrillas;
    private Long idCliente;
    private String estado;
    private boolean pagada;
    private String metodoPago;
    private String horaLlegada;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaReserva;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaPago;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaReservaRealizada;

}
