package com.example.hamacasbackend.entidades.reservas;

import com.example.hamacasbackend.entidades.cliente.Cliente;
import com.example.hamacasbackend.entidades.hamacas.Hamaca;
import com.example.hamacasbackend.entidades.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "id")
    private Usuario creadaPor;

    @ManyToOne
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    private Cliente cliente;

    private String estado;
    private boolean pagada;
    private String metodoPago;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fechaReserva;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fechaPago;

    @OneToMany(mappedBy = "idReserva")
    private List<Hamaca> hamacas;
}


