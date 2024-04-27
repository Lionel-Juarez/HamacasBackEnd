package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.reportes.Reporte;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepositorio extends CrudRepository<Reserva,Long> /*Extendemos el repositorio CRUD por defecto de
Spring Data indicando la entidad a la que vamos a acceder y el tipo de dato de la clave primaria de esta entidad*/
{
    @Query("select r from Reserva r where r.fechaReserva >= :start and r.fechaReserva < :end")
    List<Reserva> findByFechaReserva(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT r FROM Reserva r WHERE LOWER(r.cliente.nombreCompleto) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Reserva> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT r FROM Reserva r WHERE LOWER(r.estado) LIKE LOWER(CONCAT('%', :estado, '%'))")
    List<Reserva> findByEstado(@Param("estado") String estado);

}
