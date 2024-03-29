package com.example.practica1tema3;

import com.example.practica1tema3.repositorios.AlumnoRepositorio;
import com.example.practica1tema3.repositorios.LibroRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class Practica1Tema3Application {

    public static void main(String[] args) {
        SpringApplication.run(Practica1Tema3Application.class, args);
    }

    @Bean
    public CommandLineRunner run(AlumnoRepositorio alumnoRepositorio ,
                                 LibroRepositorio libroRepositorio) {
        return (args) -> {
        };
    }

}
