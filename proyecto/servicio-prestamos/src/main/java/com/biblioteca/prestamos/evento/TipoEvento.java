package com.biblioteca.prestamos.evento;

/**
 * Tipos de evento del topic "prestamos-eventos".
 * Enum de nivel superior (informe SS3.4-25): desde
 * este Capitulo 8 solo estos dos valores; el
 * Capitulo 12 anade PRESTAMO_CANCELADO (compensacion
 * de la saga) sin perder estos -- nunca se redefine
 * desde cero.
 */
public enum TipoEvento {
    PRESTAMO_CREADO,
    PRESTAMO_DEVUELTO
}
