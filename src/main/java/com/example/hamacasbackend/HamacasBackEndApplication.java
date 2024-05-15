package com.example.hamacasbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.hamacasbackend")
public class HamacasBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(HamacasBackEndApplication.class, args);
    }
}
