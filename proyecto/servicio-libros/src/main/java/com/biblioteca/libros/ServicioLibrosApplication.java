package com.biblioteca.libros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Proyecto Biblioteca -- microservicio de libros.
 * Java Esencial, Volumen 3, Capitulo 2 (JPA+H2);
 * se registra en Eureka desde el Capitulo 3.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServicioLibrosApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioLibrosApplication.class, args);
    }
}
