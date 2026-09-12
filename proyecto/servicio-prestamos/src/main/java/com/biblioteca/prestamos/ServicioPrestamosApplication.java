package com.biblioteca.prestamos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto Biblioteca -- microservicio de
 * prestamos. Java Esencial, Volumen 3, Capitulo 2.
 */
@SpringBootApplication
public class ServicioPrestamosApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioPrestamosApplication.class,
            args);
    }
}
