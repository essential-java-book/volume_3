package com.biblioteca.prestamos.proyeccion;

import com.biblioteca.prestamos.dominio.EstadoPrestamo;
import com.biblioteca.prestamos.dominio.Prestamo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lado de lectura (CQRS, Capitulo 12) de un
 * prestamo. La mantiene PrestamoProyeccionListener
 * a partir de los eventos de "prestamos-eventos" --
 * nunca se escribe desde PrestamoServicio ni desde
 * PrestamoRepositorio (el lado de escritura). El id
 * coincide con el del Prestamo original, pero
 * "prestamos_proyeccion" es una tabla totalmente
 * aparte, pensada para consultas, desacoplada del
 * modelo de escritura.
 */
@Entity
@Table(name = "prestamos_proyeccion")
public class PrestamoProyeccion {

    @Id
    private Long prestamoId;

    private Long libroId;

    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado;

    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucionPrevista;

    private LocalDate fechaDevolucionReal;

    private LocalDateTime actualizadoEn;

    protected PrestamoProyeccion() {
        // JPA
    }

    public PrestamoProyeccion(Long prestamoId,
            Long libroId, Long usuarioId,
            LocalDate fechaPrestamo) {
        this.prestamoId = prestamoId;
        this.libroId = libroId;
        this.usuarioId = usuarioId;
        this.estado = EstadoPrestamo.ACTIVO;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaPrestamo
            .plusDays(Prestamo.PLAZO_DIAS);
        this.actualizadoEn = LocalDateTime.now();
    }

    public Long getPrestamoId() {
        return prestamoId;
    }

    public Long getLibroId() {
        return libroId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void marcarDevuelto(LocalDate fecha) {
        this.estado = EstadoPrestamo.DEVUELTO;
        this.fechaDevolucionReal = fecha;
        this.actualizadoEn = LocalDateTime.now();
    }

    public void marcarCancelado() {
        this.estado = EstadoPrestamo.CANCELADO;
        this.actualizadoEn = LocalDateTime.now();
    }
}
