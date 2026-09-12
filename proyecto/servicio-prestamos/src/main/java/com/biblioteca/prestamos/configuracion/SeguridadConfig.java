package com.biblioteca.prestamos.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Recurso protegido con JWT (RSA, jwk-set-uri hacia
 * authorization-server, Capitulo 11). Todos los
 * endpoints exigen un JWT valido, sin distincion de
 * rol -- igual que "autenticado" en los prestamos
 * del monolito del Vol. 2 (informe SS3.4-27):
 * USUARIO y BIBLIOTECARIO pueden pedir y devolver
 * un prestamo por igual. InterceptorSeguridad
 * reenvia este mismo JWT en las llamadas Feign a
 * servicio-libros/servicio-usuarios.
 */
@Configuration
public class SeguridadConfig {

    @Bean
    public SecurityFilterChain cadenaSeguridad(
            HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(peticiones ->
                peticiones.anyRequest()
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
