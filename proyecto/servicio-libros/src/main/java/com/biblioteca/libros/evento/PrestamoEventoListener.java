package com.biblioteca.libros.evento;

import com.biblioteca.libros.servicio.LibroServicio;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Participante de la saga de coreografia (Capitulo
 * 12): al recibir PRESTAMO_CREADO intenta reservar
 * el libro; si no puede (ya no disponible), publica
 * PRESTAMO_CANCELADO para que los demas participantes
 * (y el propio servicio-prestamos) deshagan su parte.
 * Al recibir PRESTAMO_CANCELADO o PRESTAMO_DEVUELTO,
 * libera la reserva -- en ambos casos el libro debe
 * volver a estar disponible.
 */
@Component
public class PrestamoEventoListener {

    private final LibroServicio servicio;
    private final PrestamoEventoPublicador publicador;

    public PrestamoEventoListener(
            LibroServicio servicio,
            PrestamoEventoPublicador publicador) {
        this.servicio = servicio;
        this.publicador = publicador;
    }

    @KafkaListener(
        topics = PrestamoEventoPublicador.TOPIC,
        groupId = "servicio-libros")
    public void escuchar(PrestamoEvento evento) {
        switch (evento.tipo()) {
            case PRESTAMO_CREADO ->
                manejarCreado(evento);
            case PRESTAMO_DEVUELTO, PRESTAMO_CANCELADO ->
                servicio.liberarReserva(
                    evento.libroId(),
                    evento.prestamoId());
        }
    }

    private void manejarCreado(
            PrestamoEvento evento) {
        boolean exito = servicio.reservar(
            evento.libroId(), evento.prestamoId());
        if (!exito) {
            publicador.publicarCancelado(
                evento.prestamoId(),
                evento.libroId(),
                evento.usuarioId());
        }
    }
}
