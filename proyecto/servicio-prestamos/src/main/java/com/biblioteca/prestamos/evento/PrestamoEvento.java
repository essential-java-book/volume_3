package com.biblioteca.prestamos.evento;

import java.time.LocalDateTime;

/**
 * Evento de dominio publicado en el topic
 * "prestamos-eventos". Cada consumidor mantiene su
 * propia copia local de esta clase y de TipoEvento
 * -- nunca se comparte un jar de eventos entre
 * servicios (informe SS3.4-25). Desde el Capitulo
 * 12 lo consumen servicio-notificaciones (como
 * antes), servicio-libros y servicio-usuarios
 * (participantes de la saga) y el propio servicio-
 * prestamos, en dos listeners internos: uno de
 * compensacion y otro de proyeccion CQRS.
 */
public record PrestamoEvento(
    Long prestamoId,
    Long libroId,
    Long usuarioId,
    TipoEvento tipo,
    LocalDateTime fecha) {
}
