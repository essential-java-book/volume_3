package com.biblioteca.notificaciones.evento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor real del topic "prestamos-eventos"
 * (el esqueleto del Capitulo 3 solo se registraba en
 * Eureka -- el manuscrito creaba este consumidor dos
 * veces, en el cap. 3 y en el cap. 8; hallazgo
 * 3-D-49). Por ahora solo registra en el log la
 * notificacion que enviaria (email/SMS quedan fuera
 * del alcance de este volumen).
 */
@Component
public class NotificacionListener {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            NotificacionListener.class);

    @KafkaListener(topics = "prestamos-eventos",
        groupId = "servicio-notificaciones")
    public void escuchar(PrestamoEvento evento) {
        switch (evento.tipo()) {
            case PRESTAMO_CREADO -> LOG.info(
                "[NOTIFICACION] Prestamo {} creado "
                    + "(libro {}, usuario {})",
                evento.prestamoId(), evento.libroId(),
                evento.usuarioId());
            case PRESTAMO_DEVUELTO -> LOG.info(
                "[NOTIFICACION] Prestamo {} "
                    + "devuelto (libro {})",
                evento.prestamoId(), evento.libroId());
        }
    }
}
