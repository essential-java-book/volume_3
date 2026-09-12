package com.biblioteca.prestamos.dominio;

/**
 * Estado de un prestamo. Enum de nivel superior,
 * completo desde el Capitulo 2 (decision canonica
 * de la coleccion, informe SS3.2-14): la saga del
 * Capitulo 12 usa estos valores, no los crea.
 */
public enum EstadoPrestamo {
    SOLICITADO,
    ACTIVO,
    DEVUELTO,
    VENCIDO,
    CANCELADO
}
