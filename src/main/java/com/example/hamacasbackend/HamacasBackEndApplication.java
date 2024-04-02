package com.example.hamacasbackend;


import com.example.hamacasbackend.repositorios.ReporteRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class HamacasBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(HamacasBackEndApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ReporteRepositorio reportRepository) {
        return (args) -> {
        };
    }

}
