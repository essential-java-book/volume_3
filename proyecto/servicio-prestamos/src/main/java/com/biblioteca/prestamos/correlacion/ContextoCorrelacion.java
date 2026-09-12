package com.biblioteca.prestamos.correlacion;

/**
 * Identificador de correlacion de la peticion HTTP
 * en curso (un hilo por peticion -- modelo clasico
 * de Spring MVC). Lo rellena FiltroCorrelacion al
 * entrar y lo lee InterceptorCorrelacion al llamar
 * a otro servicio por Feign.
 */
public final class ContextoCorrelacion {

    private static final ThreadLocal<String> ID =
        new ThreadLocal<>();

    private ContextoCorrelacion() {
    }

    public static void fijar(String id) {
        ID.set(id);
    }

    public static String obtener() {
        return ID.get();
    }

    public static void limpiar() {
        ID.remove();
    }
}
