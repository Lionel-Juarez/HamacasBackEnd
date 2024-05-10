package com.example.hamacasbackend.entidades.sombrillas;

import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idSombrilla")
public class Sombrilla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSombrilla;

    @ManyToMany(mappedBy = "sombrillas")
    private List<Reserva> reservas;

    private String numeroSombrilla;
    private double precio;
    private boolean ocupada;
    private int cantidadHamacas;

}
