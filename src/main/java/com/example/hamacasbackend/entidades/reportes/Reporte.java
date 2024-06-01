package com.example.hamacasbackend.entidades.reportes;

import com.example.hamacasbackend.entidades.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    private String titulo;
    private String estado;

    @Column(length = 200)
    private String comentarioCompleto;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime fechaCreacion;
    @ManyToOne
    @JoinColumn(name = "created_by_usuario_id", referencedColumnName = "id")
    private Usuario creadoPor;
}
