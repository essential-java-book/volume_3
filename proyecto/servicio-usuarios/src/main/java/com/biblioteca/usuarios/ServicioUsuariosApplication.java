package com.biblioteca.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto Biblioteca -- microservicio de usuarios.
 * Java Esencial, Volumen 3, Capitulo 2.
 */
@SpringBootApplication
public class ServicioUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioUsuariosApplication.class, args);
    }
}
