package com.biblioteca.prestamos.evento;

import java.time.LocalDateTime;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Unico productor del topic "prestamos-eventos"
 * (informe SS3.4-25). La clave del mensaje es el id
 * del prestamo: todos los eventos de un mismo
 * prestamo caen en la misma particion y llegan en
 * orden a cada consumidor.
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
        PrestamoEvento evento = new PrestamoEvento(
            prestamoId, libroId, usuarioId, tipo,
            LocalDateTime.now());
        kafkaTemplate.send(TOPIC,
            String.valueOf(prestamoId), evento);
    }
}
