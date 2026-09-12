package com.biblioteca.prestamos.configuracion;

import com.biblioteca.prestamos.correlacion.ContextoCorrelacion;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Reenvia el identificador de correlacion de la
 * peticion actual (FiltroCorrelacion lo rellena) en
 * toda llamada Feign saliente a otro microservicio.
 * El Capitulo 11 anadira aqui, junto a este, el
 * InterceptorSeguridad (propaga el JWT).
 */
@Component
public class InterceptorCorrelacion
        implements RequestInterceptor {

    private static final String CABECERA =
        "X-Correlation-Id";

    @Override
    public void apply(RequestTemplate plantilla) {
        String id = ContextoCorrelacion.obtener();
        if (id != null) {
            plantilla.header(CABECERA, id);
        }
    }
}
