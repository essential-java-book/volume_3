package com.biblioteca.prestamos.repositorio;

import com.biblioteca.prestamos.dominio.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de "prestamos" con Spring Data
 * JPA.
 */
public interface PrestamoRepositorio
        extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);
}
