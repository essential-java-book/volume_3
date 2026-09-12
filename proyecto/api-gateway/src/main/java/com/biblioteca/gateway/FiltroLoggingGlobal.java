package com.biblioteca.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtro global: registra cada peticion que entra
 * por el gateway y marca de donde viene. Cap. 6 lo
 * usara como referencia para el interceptor de
 * correlacion en los clientes Feign.
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

        ServerHttpRequest modificada = peticion
            .mutate()
            .header("X-Gateway-Source",
                "api-gateway")
            .header("X-Procesado-Por",
                "api-gateway")
            .build();

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
