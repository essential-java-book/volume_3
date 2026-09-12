package com.biblioteca.prestamos.configuracion;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Reenvia el JWT (cabecera "Authorization") de la
 * peticion entrante en toda llamada Feign saliente,
 * junto al InterceptorCorrelacion del Capitulo 6 --
 * asi servicio-libros y servicio-usuarios reciben
 * el mismo token y aplican sus propias reglas de
 * @PreAuthorize sobre el mismo usuario autenticado
 * (Capitulo 11, informe SS3.4-27).
 */
@Component
public class InterceptorSeguridad
        implements RequestInterceptor {

    private static final String CABECERA =
        "Authorization";

    @Override
    public void apply(RequestTemplate plantilla) {
        var atributos = RequestContextHolder
            .getRequestAttributes();
        if (!(atributos instanceof
                ServletRequestAttributes sra)) {
            return;
        }
        String token = sra.getRequest()
            .getHeader(CABECERA);
        if (token != null) {
            plantilla.header(CABECERA, token);
        }
    }
}
