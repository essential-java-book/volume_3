package com.biblioteca.prestamos.proyeccion;

import com.biblioteca.prestamos.evento.PrestamoEvento;
import com.biblioteca.prestamos.evento.PrestamoEventoPublicador;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Alimenta el lado de lectura CQRS (Capitulo 12) a
 * partir de "prestamos-eventos", en un grupo de
 * consumidores propio ("servicio-prestamos-
 * proyeccion") para no competir con
 * PrestamoCompensacionListener: en Kafka, cada
 * group.id recibe su propia copia de cada mensaje.
 * Como PrestamoProyeccion usa un id asignado (no
 * autogenerado), reprocesar el mismo evento dos
 * veces (reentrega "al menos una vez" del Outbox)
 * hace un update en vez de duplicar la fila.
 */
@Component
public class PrestamoProyeccionListener {

    private final PrestamoProyeccionRepositorio
        repositorio;

    public PrestamoProyeccionListener(
            PrestamoProyeccionRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @KafkaListener(
        topics = PrestamoEventoPublicador.TOPIC,
        groupId = "servicio-prestamos-proyeccion")
    public void escuchar(PrestamoEvento evento) {
        switch (evento.tipo()) {
            case PRESTAMO_CREADO ->
                crearProyeccion(evento);
            case PRESTAMO_DEVUELTO ->
                marcarDevuelto(evento);
            case PRESTAMO_CANCELADO ->
                marcarCancelado(evento);
        }
    }

    private void crearProyeccion(
            PrestamoEvento evento) {
        if (repositorio.existsById(
                evento.prestamoId())) {
            return;
        }
        PrestamoProyeccion proyeccion =
            new PrestamoProyeccion(
                evento.prestamoId(),
                evento.libroId(), evento.usuarioId(),
                evento.fecha().toLocalDate());
        repositorio.save(proyeccion);
    }

    private void marcarDevuelto(
            PrestamoEvento evento) {
        PrestamoProyeccion proyeccion = repositorio
            .findById(evento.prestamoId())
            .orElse(null);
        if (proyeccion != null) {
            proyeccion.marcarDevuelto(
                evento.fecha().toLocalDate());
            repositorio.save(proyeccion);
        }
    }

    private void marcarCancelado(
            PrestamoEvento evento) {
        PrestamoProyeccion proyeccion = repositorio
            .findById(evento.prestamoId())
            .orElse(null);
        if (proyeccion != null) {
            proyeccion.marcarCancelado();
            repositorio.save(proyeccion);
        }
    }
}
