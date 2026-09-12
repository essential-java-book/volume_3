package com.biblioteca.prestamos.outbox;

import com.biblioteca.prestamos.evento.PrestamoEvento;
import com.biblioteca.prestamos.evento.PrestamoEventoPublicador;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Relay del patron Outbox (Capitulo 12): cada pocos
 * segundos revisa outbox_eventos en busca de filas
 * sin procesar, las envia a Kafka y las marca como
 * procesadas. Esto separa "guardar el hecho" (dentro
 * de la transaccion de negocio) de "enviarlo a
 * Kafka" (aqui, fuera de esa transaccion): si el
 * envio falla, la fila sigue con procesado=false y
 * se reintenta en la siguiente pasada -- entrega
 * "al menos una vez", nunca ninguna.
 */
@Component
public class OutboxRelay {

    private static final Logger LOG =
        LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxEventoRepositorio repositorio;
    private final KafkaTemplate<String, PrestamoEvento>
        kafkaTemplate;

    public OutboxRelay(
            OutboxEventoRepositorio repositorio,
            KafkaTemplate<String, PrestamoEvento>
                kafkaTemplate) {
        this.repositorio = repositorio;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString =
        "${biblioteca.outbox.intervalo-ms:2000}")
    public void publicarPendientes() {
        List<OutboxEvento> pendientes = repositorio
            .findByProcesadoFalseOrderByIdAsc();
        for (OutboxEvento fila : pendientes) {
            enviar(fila);
        }
    }

    private void enviar(OutboxEvento fila) {
        PrestamoEvento evento = new PrestamoEvento(
            fila.getPrestamoId(), fila.getLibroId(),
            fila.getUsuarioId(), fila.getTipo(),
            fila.getFechaEvento());
        kafkaTemplate.send(
            PrestamoEventoPublicador.TOPIC,
            String.valueOf(fila.getPrestamoId()),
            evento);
        fila.marcarProcesado();
        repositorio.save(fila);
        LOG.debug(
            "[OUTBOX] Evento {} del prestamo {} "
                + "enviado a Kafka",
            fila.getTipo(), fila.getPrestamoId());
    }
}
