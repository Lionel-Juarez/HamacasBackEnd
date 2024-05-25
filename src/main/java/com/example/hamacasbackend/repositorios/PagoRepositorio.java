package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.pagos.Pago;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface PagoRepositorio extends CrudRepository<Pago, Long> {
    @Query("SELECT p FROM Pago p WHERE FUNCTION('DATE', p.fechaPago) = :fecha")
    Page<Pago> findByFechaPago(@Param("fecha") LocalDate fecha, Pageable pageable);

    @Query("SELECT p FROM Pago p WHERE YEAR(p.fechaPago) = :year AND MONTH(p.fechaPago) = :month")
    List<Pago> findByMes(@Param("year") int year, @Param("month") int month);

    @Query("SELECT p FROM Pago p WHERE YEAR(p.fechaPago) = :year")
    List<Pago> findByAno(@Param("year") int year);

    @Query("SELECT p FROM Pago p WHERE p.metodoPago = :metodoPago")
    List<Pago> findByMetodoPago(@Param("metodoPago") String metodoPago);

    @Query("SELECT p FROM Pago p WHERE p.pagado = :pagado")
    List<Pago> findByPagado(@Param("pagado") boolean pagado);

    @Query("SELECT p FROM Pago p WHERE p.tipoHamaca = :tipoHamaca")
    List<Pago> findByTipoHamaca(@Param("tipoHamaca") String tipoHamaca);
}
