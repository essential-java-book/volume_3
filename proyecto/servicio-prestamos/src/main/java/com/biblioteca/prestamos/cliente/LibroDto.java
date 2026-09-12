package com.biblioteca.prestamos.cliente;

/**
 * Copia local (solo lectura) del libro que expone
 * servicio-libros por "/libros/{id}". Un microservicio
 * nunca comparte su entidad JPA con otro: cada uno
 * define su propia copia de lo que necesita.
 */
public record LibroDto(
    Long id,
    String titulo,
    String autor,
    String isbn,
    Integer anioPublicacion,
    boolean disponible) {
}
