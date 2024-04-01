package com.example.hamacasbackend.entidades;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReport;

    private String title;
    private String description;
    private String state;
    @Column(length = 1024) // Asumiendo que quieres permitir comentarios largos
    private String fullComment;
    private LocalDate creationDate; // Ajustado para usar LocalDate que es más adecuado para fechas sin tiempo
    private String createdBy; // Esto puede ser un ID de usuario, asegúrate de tener una entidad Usuario si es necesario

    // Considera agregar relaciones JPA si 'createdBy' se refiere a una entidad Usuario existente
    // Por ejemplo:
    // @ManyToOne
    // @JoinColumn(name = "created_by_user_id", referencedColumnName = "idUsuario")
    // private Usuario createdBy;

    // Getters, Setters, Constructors están cubiertos por Lombok
}
