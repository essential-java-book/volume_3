package com.biblioteca.prestamos.evento;

/**
 * Tipos de evento del topic "prestamos-eventos".
 * Enum de nivel superior (informe SS3.4-25): desde
 * el Capitulo 8, PRESTAMO_CREADO y PRESTAMO_
 * DEVUELTO; el Capitulo 12 anade PRESTAMO_CANCELADO
 * (compensacion de la saga de coreografia) sin
 * perder los anteriores -- nunca se redefine desde
 * cero.
 */
public enum TipoEvento {
    PRESTAMO_CREADO,
    PRESTAMO_DEVUELTO,
    PRESTAMO_CANCELADO
}
