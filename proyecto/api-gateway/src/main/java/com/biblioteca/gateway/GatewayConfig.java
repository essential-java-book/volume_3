package com.biblioteca.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rutas del gateway. Nunca "/libros-service/..." ni
 * "/prestamos-service/..." -- solo "/libros/**",
 * "/usuarios/**", "/prestamos/**" (decision canonica,
 * informe SS3.3-19).
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator rutas(
            RouteLocatorBuilder builder) {
        return builder.routes()
            .route("libros", r -> r
                .path("/libros/**")
                .uri("lb://servicio-libros"))
            .route("usuarios", r -> r
                .path("/usuarios/**")
                .uri("lb://servicio-usuarios"))
            .route("prestamos", r -> r
                .path("/prestamos/**")
                .uri("lb://servicio-prestamos"))
            .build();
    }
}
