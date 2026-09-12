package com.biblioteca.prestamos.dominio;

/**
 * El microservicio del que depende este prestamo
 * (libros o usuarios) no responde: el circuit
 * breaker ha abierto el circuito o se han agotado
 * los reintentos (Capitulo 7). Distinta de
 * "RecursoRelacionadoNoEncontradoException": aqui no
 * se sabe si el libro/usuario existe o no, solo que
 * su servicio no esta disponible ahora mismo.
 */
public class ServicioNoDisponibleException
        extends RuntimeException {

    public ServicioNoDisponibleException(
            String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
