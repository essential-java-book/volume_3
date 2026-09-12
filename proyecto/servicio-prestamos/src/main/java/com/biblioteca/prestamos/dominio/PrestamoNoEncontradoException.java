package com.biblioteca.prestamos.dominio;

/**
 * Se lanza cuando se pide un prestamo por un id
 * que no existe en este servicio.
 */
public class PrestamoNoEncontradoException
        extends RuntimeException {

    public PrestamoNoEncontradoException(Long id) {
        super("No existe el prestamo con id " + id);
    }
}
