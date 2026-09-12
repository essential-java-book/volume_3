package com.biblioteca.usuarios.evento;

import java.time.LocalDateTime;

/**
 * Copia local del evento que publica servicio-
 * prestamos. Los nombres de campo deben coincidir
 * para que Jackson lo deserialice bien, pero es una
 * clase totalmente independiente (Capitulo 12).
 */
public record PrestamoEvento(
    Long prestamoId,
    Long libroId,
    Long usuarioId,
    TipoEvento tipo,
    LocalDateTime fecha) {
}
