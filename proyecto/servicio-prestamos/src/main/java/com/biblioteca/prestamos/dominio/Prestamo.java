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
     * Crea un prestamo en estado SOLICITADO (flujo
     * del Capitulo 12: la saga de coreografia lo
     * confirma de inmediato -- ver confirmar() --,
     * sin esperar a servicio-libros/servicio-
     * usuarios, y solo lo cancela mas tarde si
     * alguno de los dos no puede completar su
     * parte).
     */
    public Prestamo(Long libroId, Long usuarioId) {
        this.libroId = libroId;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucionPrevista =
            fechaPrestamo.plusDays(PLAZO_DIAS);
        this.estado = EstadoPrestamo.SOLICITADO;
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

    /**
     * Finalizacion optimista de la saga (Capitulo
     * 12): SOLICITADO -> ACTIVO sin esperar
     * confirmacion sincrona de los participantes.
     * Si alguno falla despues, cancelar() deshace
     * esto via la compensacion de la coreografia.
     */
    public void confirmar() {
        this.estado = EstadoPrestamo.ACTIVO;
    }

    public void marcarDevuelto() {
        this.fechaDevolucionReal = LocalDate.now();
        this.estado = EstadoPrestamo.DEVUELTO;
    }

    public void cancelar() {
        this.estado = EstadoPrestamo.CANCELADO;
    }
}
