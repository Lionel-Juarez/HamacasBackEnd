package com.example.hamacasbackend.entidades.hamacas;

import com.example.hamacasbackend.entidades.cliente.Cliente;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idHamaca")
public class Hamaca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHamaca;

    @ManyToMany(mappedBy = "hamacas")
    private List<Reserva> reservas;

    private String numeroHamaca;
    private double precio;
    private boolean ocupada;
}
