package com.biblioteca.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Servidor de descubrimiento del Proyecto
 * Biblioteca. Java Esencial, Volumen 3, Capitulo 3.
 * No se registra a si mismo como cliente (es el
 * unico nodo Eureka del volumen).
 */
@SpringBootApplication
@EnableEurekaServer
public class ServidorEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ServidorEurekaApplication.class, args);
    }
}
