package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.reportes.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReporteRepositorio extends JpaRepository<Reporte, Long>
{

}
