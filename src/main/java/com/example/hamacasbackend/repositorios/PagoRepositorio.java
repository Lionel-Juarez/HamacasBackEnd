package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.Pago;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PagoRepositorio extends CrudRepository<Pago, Long> {
    @Query("SELECT p FROM Pago p WHERE FUNCTION('DATE', p.fechaPago) = :fecha ORDER BY p.fechaPago DESC")
    Page<Pago> findByFechaPago(@Param("fecha") LocalDate fecha, Pageable pageable);

    @Query("SELECT p FROM Pago p WHERE YEAR(p.fechaPago) = :year AND MONTH(p.fechaPago) = :month ORDER BY p.fechaPago DESC")
    List<Pago> findByMes(@Param("year") int year, @Param("month") int month);

    @Query("SELECT p FROM Pago p WHERE YEAR(p.fechaPago) = :year ORDER BY p.fechaPago DESC")
    List<Pago> findByAnio(@Param("year") int year);

    @Query("SELECT p FROM Pago p WHERE p.metodoPago = :metodoPago ORDER BY p.fechaPago DESC")
    List<Pago> findByMetodoPago(@Param("metodoPago") String metodoPago);

    @Query("SELECT p FROM Pago p WHERE p.pagado = :pagado ORDER BY p.fechaPago DESC")
    List<Pago> findByPagado(@Param("pagado") boolean pagado);

    @Query("SELECT p FROM Pago p WHERE p.tipoHamaca = :tipoHamaca ORDER BY p.fechaPago DESC")
    List<Pago> findByTipoHamaca(@Param("tipoHamaca") String tipoHamaca);

    @Query("SELECT p FROM Pago p WHERE p.fechaPago BETWEEN :start AND :end ORDER BY p.fechaPago DESC")
    List<Pago> findByFechaPagoBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
