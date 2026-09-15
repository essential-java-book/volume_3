package com.biblioteca.libros.evento;

import java.time.LocalDateTime;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Productor de servicio-libros para el topic
 * "prestamos-eventos" (Capitulo 12): solo publica
 * PRESTAMO_CANCELADO, la compensacion de la saga de
 * coreografia cuando este servicio no puede
 * completar su parte (libro no disponible). A
 * diferencia de servicio-prestamos, aqui no hay
 * patron Outbox -- el informe de coherencia
 * (SS3.4-26) lo reserva para servicio-prestamos;
 * el resto de servicios lo recibira en el
 * Capitulo 13, junto con Flyway.
 */
@Component
public class PrestamoEventoPublicador {

    public static final String TOPIC =
        "prestamos-eventos";

    private final KafkaTemplate<String, PrestamoEvento>
        kafkaTemplate;

    public PrestamoEventoPublicador(
            KafkaTemplate<String, PrestamoEvento>
                kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarCancelado(Long prestamoId,
            Long libroId, Long usuarioId) {
        PrestamoEvento evento = new PrestamoEvento(
            prestamoId, libroId, usuarioId,
            TipoEvento.PRESTAMO_CANCELADO,
            LocalDateTime.now());
        kafkaTemplate.send(TOPIC,
            String.valueOf(prestamoId), evento);
    }
}
