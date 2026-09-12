package com.biblioteca.libros.controlador;

import com.biblioteca.libros.dominio.LibroNoEncontradoException;
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

    @ExceptionHandler(LibroNoEncontradoException.class)
    public ProblemDetail manejarNoEncontrado(
            LibroNoEncontradoException ex) {
        ProblemDetail problema = ProblemDetail
            .forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Libro no encontrado");
        return problema;
    }
}
