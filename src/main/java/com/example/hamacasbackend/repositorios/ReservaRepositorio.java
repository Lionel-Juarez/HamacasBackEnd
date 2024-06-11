package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.reservas.Reserva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepositorio extends CrudRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva >= :start AND r.fechaReserva < :end")
    List<Reserva> findByFechaReserva(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

//    @Query("SELECT r FROM Reserva r JOIN r.sombrillas h WHERE h.idSombrilla = :idSombrilla")
//    List<Reserva> findBySombrillaId(@Param("idSombrilla") Long idSombrilla);

    @Query("SELECT r FROM Reserva r WHERE LOWER(r.cliente.nombreCompleto) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Reserva> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT r FROM Reserva r WHERE LOWER(r.estado) LIKE LOWER(CONCAT('%', :estado, '%'))")
    List<Reserva> findByEstado(@Param("estado") String estado);

    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva >= :start AND r.fechaReserva < :end AND LOWER(r.estado) = LOWER(:estado)")
    List<Reserva> findByFechaReservaAndEstado(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("estado") String estado);
    @Query("SELECT r FROM Reserva r WHERE r.cliente.idCliente = :idCliente")
    List<Reserva> findByClienteId(@Param("idCliente") Long idCliente);
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cliente.idCliente = :idCliente")
    long countByClienteId(@Param("idCliente") Long idCliente);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cliente.idCliente = :idCliente AND LOWER(r.estado) = LOWER(:estado)")
    long countByClienteIdAndEstado(@Param("idCliente") Long idCliente, @Param("estado") String estado);

}




