package com.biblioteca.usuarios.evento;

import java.time.LocalDateTime;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Productor de servicio-usuarios para el topic
 * "prestamos-eventos" (Capitulo 12): solo publica
 * PRESTAMO_CANCELADO, la compensacion de la saga de
 * coreografia cuando este servicio no puede
 * completar su parte (usuario inactivo). Sin patron
 * Outbox aqui -- reservado a servicio-prestamos
 * (informe SS3.4-26); el resto de servicios lo
 * recibira en el Capitulo 13, junto con Flyway.
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
