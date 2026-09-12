package com.biblioteca.prestamos.cliente;

/**
 * Copia local (solo lectura) del usuario que expone
 * servicio-usuarios por "/usuarios/{id}".
 */
public record UsuarioDto(
    Long id,
    String nombre,
    String email,
    String numeroCarne,
    boolean activo,
    int prestamosActivos) {
}
