package com.biblioteca.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Puerta de enlace unica de la API del Proyecto
 * Biblioteca. Java Esencial, Volumen 3, Capitulo 4.
 * Enruta hacia servicio-libros, servicio-usuarios
 * y servicio-prestamos usando Eureka (lb://).
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ApiGatewayApplication.class, args);
    }
}
