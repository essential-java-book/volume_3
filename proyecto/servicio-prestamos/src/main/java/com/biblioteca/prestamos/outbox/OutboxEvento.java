package com.biblioteca.prestamos.outbox;

import com.biblioteca.prestamos.evento.TipoEvento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Fila del patron Outbox (Capitulo 12, informe
 * SS3.4-26): PrestamoEventoPublicador escribe aqui,
 * en la misma transaccion que el Prestamo, en vez
 * de enviar a Kafka directamente. OutboxRelay lee
 * las filas con procesado=false, las envia a Kafka
 * y las marca procesadas. Solo servicio-prestamos
 * usa esta tabla -- Flyway se introduce aqui antes
 * que en el resto de servicios (que lo reciben en
 * el Capitulo 13).
 */
@Entity
@Table(name = "outbox_eventos")
public class OutboxEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long prestamoId;

    private Long libroId;

    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipo;

    private LocalDateTime fechaEvento;

    private boolean procesado = false;

    protected OutboxEvento() {
        // JPA
    }

    public OutboxEvento(Long prestamoId, Long libroId,
            Long usuarioId, TipoEvento tipo,
            LocalDateTime fechaEvento) {
        this.prestamoId = prestamoId;
        this.libroId = libroId;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.fechaEvento = fechaEvento;
        this.procesado = false;
    }

    public Long getId() {
        return id;
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

    public TipoEvento getTipo() {
        return tipo;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void marcarProcesado() {
        this.procesado = true;
    }
}
