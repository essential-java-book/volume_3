package com.biblioteca.libros.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Recurso protegido con JWT (RSA, jwk-set-uri hacia
 * authorization-server, Capitulo 11). La lectura de
 * "GET /libros/**" queda publica; el resto exige un
 * JWT valido y, ademas, @PreAuthorize en
 * LibroControlador exige el rol BIBLIOTECARIO para
 * crear/actualizar/eliminar (antes ROLE_ADMIN en el
 * monolito del Vol. 2, informe SS3.4-27).
 */
@Configuration
@EnableMethodSecurity
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain cadenaSeguridad(
            HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(peticiones ->
                peticiones
                    .requestMatchers(HttpMethod.GET,
                        "/libros/**").permitAll()
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
