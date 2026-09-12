package com.biblioteca.prestamos.proyeccion;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la proyeccion CQRS de lectura.
 */
public interface PrestamoProyeccionRepositorio
        extends JpaRepository<PrestamoProyeccion,
            Long> {
}
