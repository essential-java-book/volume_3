package com.biblioteca.usuarios.repositorio;

import com.biblioteca.usuarios.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de "usuarios" con Spring Data JPA.
 */
public interface UsuarioRepositorio
        extends JpaRepository<Usuario, Long> {
}
