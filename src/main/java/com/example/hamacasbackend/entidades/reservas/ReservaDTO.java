package com.example.hamacasbackend.entidades.reservas;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data // Esta anotación de Lombok genera todos los getters, setters, toString, equals y hashCode.
public class ReservaDTO {
    private Long idReserva; // Agregado para permitir fácil identificación y manipulación de reservas
    private List<Long> idSombrillas; // Lista de IDs de Sombrillas
    private Long idCliente;
    private String uid;
    private String estado;
    private boolean pagada;
    private String metodoPago;
    private String horaLlegada;
    private String nombreUsuario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaReserva; // Fecha y hora cuando la reserva está programada

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaPago; // Fecha y hora del pago de la reserva

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaReservaRealizada; //

}
