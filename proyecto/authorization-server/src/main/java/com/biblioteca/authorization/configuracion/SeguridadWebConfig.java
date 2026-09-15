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
 *
 * "/actuator/health/**" tambien queda publico --
 * correccion del 15/9/2026, encontrada al verificar
 * el Capitulo 14: las sondas de Kubernetes
 * (kubelet) llaman a /actuator/health/liveness y
 * /actuator/health/readiness sin sesion ninguna, y
 * el matcher de @Order(1) solo cubre los endpoints
 * propios del protocolo OAuth2 -- estas peticiones
 * caian aqui y esta cadena las mandaba al formulario
 * de login, que un kubelet nunca completa (401/302
 * segun el caso), matando el pod en bucle. No existia
 * forma de arreglarlo desde fuera (ninguna propiedad
 * lo controla): hacia falta este cambio en el propio
 * filtro. No reabre ninguna decision del Capitulo 11
 * -- el resto sigue exigiendo login igual que antes
 * --, solo anade la excepcion estandar que recomienda
 * la documentacion de Spring Boot para sondas de
 * salud de Kubernetes.
 */
@Configuration
public class SeguridadWebConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain
            cadenaSeguridadWeb(HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(peticiones ->
                peticiones
                    .requestMatchers(
                        "/actuator/health/**")
                    .permitAll()
                    .anyRequest()
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
