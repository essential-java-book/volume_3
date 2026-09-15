package com.biblioteca.authorization.repositorio;

import com.biblioteca.authorization.dominio.Credencial;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de "credenciales" con Spring
 * Data JPA.
 */
public interface CredencialRepositorio
        extends JpaRepository<Credencial, Long> {

    Optional<Credencial> findByUsername(
        String username);
}
