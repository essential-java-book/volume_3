package com.biblioteca.notificaciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * Proyecto Biblioteca -- microservicio de
 * notificaciones. Esqueleto del Capitulo 3: solo
 * se registra en Eureka. El consumidor Kafka real
 * llega en el Capitulo 8 (informe de coherencia,
 * hallazgo 3-D-49: evita crearlo dos veces). Recibe
 * la propiedad de bienvenida del Config Server
 * desde el Capitulo 5.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServicioNotificacionesApplication {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            ServicioNotificacionesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioNotificacionesApplication.class,
            args);
    }

    @Bean
    public CommandLineRunner mostrarBienvenida(
            @Value("${biblioteca.mensaje-bienvenida:"
                + "Config Server no disponible}")
            String mensaje) {
        return args -> LOG.info(
            "[CONFIG] {}", mensaje);
    }
}
