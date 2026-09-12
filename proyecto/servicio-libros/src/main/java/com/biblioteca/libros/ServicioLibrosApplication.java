package com.biblioteca.libros;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * Proyecto Biblioteca -- microservicio de libros.
 * Java Esencial, Volumen 3, Capitulo 2 (JPA+H2);
 * se registra en Eureka desde el Capitulo 3; recibe
 * "biblioteca.mensaje-bienvenida" del Config Server
 * desde el Capitulo 5 (o el valor por defecto si el
 * Config Server no esta arrancado).
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServicioLibrosApplication {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            ServicioLibrosApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioLibrosApplication.class, args);
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
