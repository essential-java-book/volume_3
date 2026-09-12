package com.biblioteca.notificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Proyecto Biblioteca -- microservicio de
 * notificaciones. Esqueleto del Capitulo 3: solo
 * se registra en Eureka. El consumidor Kafka real
 * llega en el Capitulo 8 (informe de coherencia,
 * hallazgo 3-D-49: evita crearlo dos veces).
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServicioNotificacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioNotificacionesApplication.class,
            args);
    }
}
