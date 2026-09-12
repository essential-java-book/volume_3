package com.biblioteca.prestamos.correlacion;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Cada peticion entrante recibe un identificador de
 * correlacion: el de la cabecera "X-Correlation-Id"
 * si ya viene (por ejemplo, del gateway u otro
 * servicio), o uno nuevo si no. El gateway del
 * Capitulo 4 no la anade (informe de coherencia,
 * hallazgo 3-D-10) -- nace aqui, en el Capitulo 6.
 *
 * Desde el Capitulo 10 tambien se copia al MDC de
 * SLF4J: el logging estructurado en JSON incluye el
 * MDC completo, asi que "correlationId" queda junto
 * al traceId/spanId de Micrometer Tracing (Cap. 9)
 * en cada linea de log de este servicio.
 */
@Component
public class FiltroCorrelacion implements Filter {

    private static final String CABECERA =
        "X-Correlation-Id";

    private static final String CLAVE_MDC =
        "correlationId";

    @Override
    public void doFilter(ServletRequest peticion,
            ServletResponse respuesta,
            FilterChain cadena)
            throws IOException, ServletException {
        HttpServletRequest http =
            (HttpServletRequest) peticion;
        HttpServletResponse httpResp =
            (HttpServletResponse) respuesta;

        String id = http.getHeader(CABECERA);
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        ContextoCorrelacion.fijar(id);
        MDC.put(CLAVE_MDC, id);
        httpResp.setHeader(CABECERA, id);

        try {
            cadena.doFilter(peticion, respuesta);
        } finally {
            ContextoCorrelacion.limpiar();
            MDC.remove(CLAVE_MDC);
        }
    }
}
