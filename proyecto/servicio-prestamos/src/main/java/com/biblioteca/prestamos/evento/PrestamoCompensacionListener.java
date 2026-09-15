package com.biblioteca.prestamos.evento;

import com.biblioteca.prestamos.servicio.PrestamoServicio;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Lado de servicio-prestamos en la saga de
 * coreografia (Capitulo 12): escucha su propio
 * topic "prestamos-eventos" y reacciona solo a
 * PRESTAMO_CANCELADO -- el evento de compensacion
 * que publica servicio-libros o servicio-usuarios
 * cuando no puede completar su parte (libro no
 * disponible, usuario inactivo). PRESTAMO_CREADO y
 * PRESTAMO_DEVUELTO se ignoran aqui: este servicio
 * ya conoce esos hechos porque los publica el
 * mismo -- este listener existe solo para la
 * compensacion, nunca para el camino feliz.
 */
@Component
public class PrestamoCompensacionListener {

    private final PrestamoServicio servicio;

    public PrestamoCompensacionListener(
            PrestamoServicio servicio) {
        this.servicio = servicio;
    }

    @KafkaListener(
        topics = PrestamoEventoPublicador.TOPIC,
        groupId = "servicio-prestamos-compensacion")
    public void escuchar(PrestamoEvento evento) {
        if (evento.tipo()
                == TipoEvento.PRESTAMO_CANCELADO) {
            servicio.cancelarPorCompensacion(
                evento.prestamoId());
        }
    }
}
