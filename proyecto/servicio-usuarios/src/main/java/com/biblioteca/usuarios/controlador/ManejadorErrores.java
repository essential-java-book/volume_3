package com.biblioteca.usuarios.controlador;

import com.biblioteca.usuarios.dominio.UsuarioNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de dominio a respuestas
 * HTTP con formato ProblemDetail (RFC 9457).
 */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ProblemDetail manejarNoEncontrado(
            UsuarioNoEncontradoException ex) {
        ProblemDetail problema = ProblemDetail
            .forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Usuario no encontrado");
        return problema;
    }
}
