package com.biblioteca.usuarios.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Recurso protegido con JWT (RSA, jwk-set-uri hacia
 * authorization-server, Capitulo 11). A diferencia
 * de servicio-libros, aqui no hay lectura publica --
 * los datos de usuario no lo son -- pero si un
 * matiz: @PreAuthorize en UsuarioControlador exige
 * el rol BIBLIOTECARIO solo para
 * crear/actualizar/eliminar; cualquier usuario
 * autenticado (USUARIO o BIBLIOTECARIO) puede leer.
 *
 * "/actuator/health/**" tambien queda publico --
 * correccion del 15/9/2026, encontrada al verificar
 * el Capitulo 14: las sondas de Kubernetes
 * (kubelet) llaman a /actuator/health/liveness y
 * /actuator/health/readiness sin ningun JWT, y sin
 * este permitAll la cadena de seguridad las
 * rechazaba con 401, matando el pod en bucle. No
 * existia forma de arreglarlo desde fuera (ninguna
 * propiedad lo controla): hacia falta este cambio
 * en el propio filtro. No reabre la decision de
 * seguridad del Capitulo 11 -- los endpoints de
 * negocio siguen exigiendo JWT igual que antes --,
 * solo anade la excepcion estandar que recomienda la
 * documentacion de Spring Boot para sondas de salud
 * de Kubernetes.
 */
@Configuration
@EnableMethodSecurity
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain cadenaSeguridad(
            HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(peticiones ->
                peticiones
                    .requestMatchers(
                        "/actuator/health/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
            .csrf(csrf -> csrf.disable())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(
                        conversorAutoridades())));
        return http.build();
    }

    private JwtAuthenticationConverter
            conversorAutoridades() {
        JwtGrantedAuthoritiesConverter
            conversorRoles =
                new JwtGrantedAuthoritiesConverter();
        conversorRoles.setAuthoritiesClaimName(
            "roles");
        conversorRoles.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter conversor =
            new JwtAuthenticationConverter();
        conversor
            .setJwtGrantedAuthoritiesConverter(
                conversorRoles);
        return conversor;
    }
}
