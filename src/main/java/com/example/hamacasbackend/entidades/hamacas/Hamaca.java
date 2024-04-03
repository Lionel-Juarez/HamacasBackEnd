package com.example.hamacasbackend.entidades.hamacas;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Marca la clase como una entidad JPA
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hamaca {
    @Id // Designa este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del valor
    private Long idHamaca;

    @Column // Es opcional si el nombre del campo coincide con el nombre de la columna en la DB
    private double precio;

    @Column
    private boolean reservada;

    @Column
    private boolean ocupada;
}
