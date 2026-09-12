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
 */
@Configuration
@EnableMethodSecurity
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
