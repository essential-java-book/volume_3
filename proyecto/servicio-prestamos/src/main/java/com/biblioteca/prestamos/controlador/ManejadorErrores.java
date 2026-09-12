package com.biblioteca.prestamos.controlador;

import com.biblioteca.prestamos.dominio.PrestamoNoEncontradoException;
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

    @ExceptionHandler(PrestamoNoEncontradoException.class)
    public ProblemDetail manejarNoEncontrado(
            PrestamoNoEncontradoException ex) {
        ProblemDetail problema = ProblemDetail
            .forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Prestamo no encontrado");
        return problema;
    }
}
