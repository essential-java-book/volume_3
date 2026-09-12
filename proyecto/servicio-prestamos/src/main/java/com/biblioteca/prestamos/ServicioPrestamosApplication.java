package com.biblioteca.prestamos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * Proyecto Biblioteca -- microservicio de
 * prestamos. Java Esencial, Volumen 3, Capitulo 2
 * (JPA+H2); se registra en Eureka desde el
 * Capitulo 3; recibe la propiedad de bienvenida
 * del Config Server desde el Capitulo 5; usa
 * clientes Feign (LibroCliente, UsuarioCliente)
 * desde el Capitulo 6 para validar el libro y el
 * usuario al crear un prestamo.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ServicioPrestamosApplication {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            ServicioPrestamosApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(
            ServicioPrestamosApplication.class,
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
