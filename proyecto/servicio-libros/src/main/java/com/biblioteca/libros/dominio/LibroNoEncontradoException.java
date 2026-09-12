package com.biblioteca.libros.dominio;

/**
 * Se lanza cuando se pide un libro por un id que
 * no existe en el catalogo de este servicio.
 */
public class LibroNoEncontradoException
        extends RuntimeException {

    public LibroNoEncontradoException(Long id) {
        super("No existe el libro con id " + id);
    }
}
