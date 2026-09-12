package com.biblioteca.prestamos.evento;

import com.biblioteca.prestamos.outbox.OutboxEvento;
import com.biblioteca.prestamos.outbox.OutboxEventoRepositorio;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Unico productor logico del topic "prestamos-
 * eventos" (informe SS3.4-25). Desde el Capitulo 12
 * ya NO publica directamente en Kafka: escribe una
 * fila en la tabla outbox_eventos, en la misma
 * transaccion que el guardado del Prestamo (patron
 * Outbox -- informe SS3.4-26). El envio real a
 * Kafka lo hace OutboxRelay, en un proceso aparte,
 * por lo que el mensaje puede tardar un poco en
 * llegar pero nunca se pierde si Kafka no responde
 * en el instante de guardar.
 */
@Component
public class PrestamoEventoPublicador {

    public static final String TOPIC =
        "prestamos-eventos";

    private final OutboxEventoRepositorio
        outboxRepositorio;

    public PrestamoEventoPublicador(
            OutboxEventoRepositorio
                outboxRepositorio) {
        this.outboxRepositorio = outboxRepositorio;
    }

    public void publicarCreado(Long prestamoId,
            Long libroId, Long usuarioId) {
        publicar(prestamoId, libroId, usuarioId,
            TipoEvento.PRESTAMO_CREADO);
    }

    public void publicarDevuelto(Long prestamoId,
            Long libroId, Long usuarioId) {
        publicar(prestamoId, libroId, usuarioId,
            TipoEvento.PRESTAMO_DEVUELTO);
    }

    private void publicar(Long prestamoId,
            Long libroId, Long usuarioId,
            TipoEvento tipo) {
        OutboxEvento outbox = new OutboxEvento(
            prestamoId, libroId, usuarioId, tipo,
            LocalDateTime.now());
        outboxRepositorio.save(outbox);
    }
}
