package com.biblioteca.prestamos.outbox;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a datos de la tabla outbox_eventos.
 */
public interface OutboxEventoRepositorio
        extends JpaRepository<OutboxEvento, Long> {

    List<OutboxEvento>
        findByProcesadoFalseOrderByIdAsc();
}
