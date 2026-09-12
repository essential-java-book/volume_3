package com.biblioteca.prestamos.evento;

import java.time.LocalDateTime;

/**
 * Evento de dominio publicado en el topic
 * "prestamos-eventos". Cada consumidor (por ahora,
 * solo servicio-notificaciones) mantiene su propia
 * copia local de esta clase y de TipoEvento -- nunca
 * se comparte un jar de eventos entre servicios.
 */
public record PrestamoEvento(
    Long prestamoId,
    Long libroId,
    Long usuarioId,
    TipoEvento tipo,
    LocalDateTime fecha) {
}
