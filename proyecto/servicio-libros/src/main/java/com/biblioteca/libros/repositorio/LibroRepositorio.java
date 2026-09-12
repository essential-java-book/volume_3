package com.biblioteca.libros.repositorio;

import com.biblioteca.libros.dominio.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de "libros" con Spring Data JPA.
 * Metodos estandar: findAll, findById, save,
 * deleteById -- nada de HashMap en este volumen.
 */
public interface LibroRepositorio
        extends JpaRepository<Libro, Long> {
}
