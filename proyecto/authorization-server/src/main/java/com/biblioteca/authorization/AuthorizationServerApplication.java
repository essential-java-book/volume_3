package com.biblioteca.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Servidor de autorizacion OAuth2/JWT del proyecto
 * Biblioteca (Capitulo 11). Emite tokens firmados
 * con RSA para el cliente "biblioteca-web"; los
 * cuatro servicios de negocio y el gateway los
 * validan como resource servers (jwk-set-uri).
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            AuthorizationServerApplication.class,
            args);
    }
}
