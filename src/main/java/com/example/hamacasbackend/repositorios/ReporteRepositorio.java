package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.reportes.Reporte;
import org.springframework.data.repository.CrudRepository;

public interface ReporteRepositorio extends CrudRepository<Reporte,Long> /*Extendemos el repositorio CRUD por defecto de
Spring Data indicando la entidad a la que vamos a acceder y el tipo de dato de la clave primaria de esta entidad*/
{

}
