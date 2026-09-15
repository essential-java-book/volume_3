package com.biblioteca.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Servidor de configuracion centralizada del
 * Proyecto Biblioteca. Java Esencial, Volumen 3,
 * Capitulo 5. Perfil "native": sirve el contenido
 * de config-repo/ (en este volumen, un recurso del
 * propio jar; en produccion seria un repositorio
 * Git independiente).
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            ConfigServerApplication.class, args);
    }
}
