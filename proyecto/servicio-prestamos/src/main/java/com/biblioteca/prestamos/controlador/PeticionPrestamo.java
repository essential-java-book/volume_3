package com.biblioteca.prestamos.controlador;

import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo de la peticion para crear un prestamo.
 */
public record PeticionPrestamo(
    @NotNull Long libroId,
    @NotNull Long usuarioId) {
}
