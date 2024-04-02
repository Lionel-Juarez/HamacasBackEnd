package com.example.hamacasbackend.repositorios;

import com.example.hamacasbackend.entidades.reportes.Reporte;
import com.example.hamacasbackend.entidades.reservas.Reserva;
import org.springframework.data.repository.CrudRepository;

public interface ReservaRepositorio extends CrudRepository<Reserva,Long> /*Extendemos el repositorio CRUD por defecto de
Spring Data indicando la entidad a la que vamos a acceder y el tipo de dato de la clave primaria de esta entidad*/
{

}
