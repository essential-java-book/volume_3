package com.biblioteca.usuarios.dominio;

/**
 * Se lanza cuando se pide un usuario por un id
 * que no existe en este servicio.
 */
public class UsuarioNoEncontradoException
        extends RuntimeException {

    public UsuarioNoEncontradoException(Long id) {
        super("No existe el usuario con id " + id);
    }
}
