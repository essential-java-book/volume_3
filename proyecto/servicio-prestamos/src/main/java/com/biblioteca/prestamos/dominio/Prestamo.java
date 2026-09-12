package com.biblioteca.prestamos.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Prestamo de un libro a un usuario. No hay
 * relacion JPA con Libro/Usuario -- son de otros
 * microservicios, aqui solo se guarda su id
 * (informe de coherencia, contrato Vol.2 -> Vol.3).
 */
@Entity
@Table(name = "prestamos")
public class Prestamo {

    /** Plazo de prestamo, en dias (Vols. 3-4). */
    public static final int PLAZO_DIAS = 14;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;

    private Long usuarioId;

    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucionPrevista;

    private LocalDate fechaDevolucionReal;

    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado;

    protected Prestamo() {
        // JPA
    }

    /**
     * Crea un prestamo ya activo (flujo simple,
     * sin saga). El Capitulo 12 introduce el flujo
     * SOLICITADO -> ACTIVO/CANCELADO.
     */
    public Prestamo(Long libroId, Long usuarioId) {
        this.libroId = libroId;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucionPrevista =
            fechaPrestamo.plusDays(PLAZO_DIAS);
        this.estado = EstadoPrestamo.ACTIVO;
    }

    public Long getId() {
        return id;
    }

    public Long getLibroId() {
        return libroId;
    }

    public Long getUsuarioId() {
        return usuarioId;
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

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void marcarDevuelto() {
        this.fechaDevolucionReal = LocalDate.now();
        this.estado = EstadoPrestamo.DEVUELTO;
    }

    public void cancelar() {
        this.estado = EstadoPrestamo.CANCELADO;
    }
}
