package com.example.hamacasbackend;

import com.example.hamacasbackend.repositorios.AlumnoRepositorio;
import com.example.hamacasbackend.repositorios.LibroRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class HamacasBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.example.hamacasbackend.HamacasBackEndApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(AlumnoRepositorio alumnoRepositorio ,
                                 LibroRepositorio libroRepositorio) {
        return (args) -> {
        };
    }

}
