package com.biblioteca.usuarios.evento;

import com.biblioteca.usuarios.servicio.UsuarioServicio;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Participante de la saga de coreografia (Capitulo
 * 12): al recibir PRESTAMO_CREADO intenta validar y
 * registrar el prestamo del usuario; si no puede
 * (usuario inactivo), publica PRESTAMO_CANCELADO
 * para que los demas participantes (y el propio
 * servicio-prestamos) deshagan su parte. Al recibir
 * PRESTAMO_CANCELADO o PRESTAMO_DEVUELTO, libera el
 * prestamo -- en ambos casos deja de contar como
 * activo para ese usuario.
 */
@Component
public class PrestamoEventoListener {

    private final UsuarioServicio servicio;
    private final PrestamoEventoPublicador publicador;

    public PrestamoEventoListener(
            UsuarioServicio servicio,
            PrestamoEventoPublicador publicador) {
        this.servicio = servicio;
        this.publicador = publicador;
    }

    @KafkaListener(
        topics = PrestamoEventoPublicador.TOPIC,
        groupId = "servicio-usuarios")
    public void escuchar(PrestamoEvento evento) {
        switch (evento.tipo()) {
            case PRESTAMO_CREADO ->
                manejarCreado(evento);
            case PRESTAMO_DEVUELTO, PRESTAMO_CANCELADO ->
                servicio.liberarPrestamo(
                    evento.usuarioId(),
                    evento.prestamoId());
        }
    }

    private void manejarCreado(
            PrestamoEvento evento) {
        boolean exito = servicio
            .validarYRegistrarPrestamo(
                evento.usuarioId(),
                evento.prestamoId());
        if (!exito) {
            publicador.publicarCancelado(
                evento.prestamoId(),
                evento.libroId(),
                evento.usuarioId());
        }
    }
}
