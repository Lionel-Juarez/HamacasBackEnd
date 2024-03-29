package com.example.practica1tema3.entidades;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReporte;

    private Date fechaReporte;
    private Double totalIngresos;
    private Integer totalReservas;
    private String comentarios;
}
