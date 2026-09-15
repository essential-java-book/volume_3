package com.biblioteca.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * El gateway tambien es resource server (JWT RSA,
 * jwk-set-uri hacia authorization-server, Capitulo
 * 11) -- pero solo autentica en el borde; deja el
 * detalle de rol (@PreAuthorize) a cada
 * microservicio de destino. "GET /libros/**" queda
 * publico, igual que en servicio-libros; el resto
 * exige un JWT valido.
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
@EnableWebFluxSecurity
public class SeguridadConfig {

    @Bean
    public SecurityWebFilterChain cadenaSeguridad(
            ServerHttpSecurity http) {
        http.csrf(
                ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(intercambios ->
                intercambios
                    .pathMatchers(
                        "/actuator/health/**")
                    .permitAll()
                    .pathMatchers(HttpMethod.GET,
                        "/libros/**").permitAll()
                    .anyExchange().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(
                        conversorAutoridades())));
        return http.build();
    }

    private ReactiveJwtAuthenticationConverterAdapter
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
        return new
            ReactiveJwtAuthenticationConverterAdapter(
                conversor);
    }
}
