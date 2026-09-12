package com.biblioteca.notificaciones.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Recurso protegido con JWT (RSA, jwk-set-uri hacia
 * authorization-server, Capitulo 11). El unico
 * endpoint HTTP propio hoy ("/notificaciones/salud",
 * Capitulo 3) queda publico -- es solo una
 * comprobacion de vida --; cualquier endpoint
 * futuro exigira un JWT valido por defecto.
 */
@Configuration
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain cadenaSeguridad(
            HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(peticiones ->
                peticiones
                    .requestMatchers(
                        "/notificaciones/salud")
                    .permitAll()
                    .anyRequest().authenticated())
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
