package com.biblioteca.prestamos.dominio;

/**
 * El libro o el usuario referenciado en un prestamo
 * no existe en su propio microservicio (respuesta
 * 404 al llamar a LibroCliente/UsuarioCliente,
 * Capitulo 6).
 */
public class RecursoRelacionadoNoEncontradoException
        extends RuntimeException {

    public RecursoRelacionadoNoEncontradoException(
            String mensaje) {
        super(mensaje);
    }
}
