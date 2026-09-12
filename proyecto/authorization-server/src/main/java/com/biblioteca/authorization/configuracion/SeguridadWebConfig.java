package com.biblioteca.authorization.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Segunda cadena de seguridad (@Order(2)): todo lo
 * que no sean los endpoints propios del protocolo
 * OAuth2 (esos los cubre AuthorizationServerConfig,
 * @Order(1)) exige login -- el formulario que Spring
 * Security da por defecto sirve para autenticar a
 * "usuario1"/"bibliotecario1" cuando el navegador
 * llega a /oauth2/authorize.
 */
@Configuration
public class SeguridadWebConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain
            cadenaSeguridadWeb(HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(peticiones ->
                peticiones.anyRequest()
                    .authenticated())
            .formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Codificador delegante: entiende tanto
     * "{bcrypt}..." (credenciales de usuario1 y
     * bibliotecario1 -- ver data.sql) como
     * "{noop}..." (el secreto en claro del cliente
     * "biblioteca-web", solo valido para desarrollo).
     */
    @Bean
    public PasswordEncoder codificadorClaves() {
        return PasswordEncoderFactories
            .createDelegatingPasswordEncoder();
    }
}
