package com.biblioteca.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Proyecto Biblioteca -- microservicio de usuarios.
 * Java Esencial, Volumen 3, Capitulo 2 (JPA+H2);
 * se registra en Eureka desde el Capitulo 3.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServicioUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioUsuariosApplication.class, args);
    }
}
