package com.biblioteca.libros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto Biblioteca -- microservicio de libros.
 * Java Esencial, Volumen 3, Capitulo 2.
 */
@SpringBootApplication
public class ServicioLibrosApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioLibrosApplication.class, args);
    }
}
