package com.biblioteca.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtro global: registra cada peticion que entra
 * por el gateway y marca de donde viene. Cap. 6 lo
 * usara como referencia para el interceptor de
 * correlacion en los clientes Feign.
 *
 * Correccion (15/9/2026, verificando el Capitulo
 * 14 -- primera vez que una peticion real atraviesa
 * el gateway con Spring Security ya activo, cap.
 * 11): "peticion.mutate().header(...).build()"
 * lanzaba UnsupportedOperationException en
 * ReadOnlyHttpHeaders.put. Spring Security envuelve
 * las cabeceras de la peticion como de solo lectura
 * al pasar por su cadena de filtros reactiva (bug
 * conocido, varios issues abiertos en spring-
 * security y spring-cloud-gateway), y el "builder"
 * de ServerHttpRequest reutiliza esa misma instancia
 * en vez de copiarla. Con GET /libros sin JWT (ruta
 * publica) nunca se habia ejercitado antes este
 * filtro con Spring Security realmente en medio.
 * Corregido sin pasar por "mutate().header(...)":
 * un ServerHttpRequestDecorator con getHeaders()
 * propio devuelve una copia nueva e independiente,
 * sin tocar en ningun momento el objeto de solo
 * lectura original.
 */
@Component
public class FiltroLoggingGlobal
        implements GlobalFilter, Ordered {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            FiltroLoggingGlobal.class);

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain cadena) {
        ServerHttpRequest peticion =
            exchange.getRequest();
        LOG.info("[GATEWAY] {} {}",
            peticion.getMethod(),
            peticion.getURI());

        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.addAll(peticion.getHeaders());
        cabeceras.set("X-Gateway-Source",
            "api-gateway");
        cabeceras.set("X-Procesado-Por",
            "api-gateway");

        ServerHttpRequest modificada =
            new ServerHttpRequestDecorator(
                    peticion) {
                @Override
                public HttpHeaders getHeaders() {
                    return cabeceras;
                }
            };

        return cadena.filter(
            exchange.mutate()
                .request(modificada)
                .build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
