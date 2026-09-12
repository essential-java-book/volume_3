package com.biblioteca.notificaciones.evento;

/**
 * Copia local de los tipos de evento del topic
 * "prestamos-eventos" -- servicio-prestamos tiene la
 * suya propia. Cada consumidor mantiene su copia:
 * nunca se comparte un jar de eventos entre
 * microservicios (informe SS3.4-25).
 */
public enum TipoEvento {
    PRESTAMO_CREADO,
    PRESTAMO_DEVUELTO
}
