package com.biblioteca.libros.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Libro del catalogo de la Biblioteca Municipal
 * "El Quijote". Cada microservicio de negocio es
 * dueno de su propia tabla (aqui, "libros").
 */
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String autor;

    @NotBlank
    private String isbn;

    @NotNull
    private Integer anioPublicacion;

    private boolean disponible = true;

    /**
     * Id del prestamo (servicio-prestamos) que
     * tiene este libro reservado ahora mismo, o
     * null si esta disponible. Lo usa la saga del
     * Capitulo 12 para saber, al recibir
     * PRESTAMO_CANCELADO o PRESTAMO_DEVUELTO, si
     * esta liberacion le corresponde a este libro
     * o si su reserva actual (si tiene otra) es de
     * un prestamo distinto.
     */
    private Long prestamoReservaId;

    protected Libro() {
        // JPA
    }

    public Libro(String titulo, String autor,
            String isbn, Integer anioPublicacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.disponible = true;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anio) {
        this.anioPublicacion = anio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void marcarNoDisponible() {
        this.disponible = false;
    }

    public void marcarDisponible() {
        this.disponible = true;
    }

    public Long getPrestamoReservaId() {
        return prestamoReservaId;
    }

    /**
     * Reserva el libro para un prestamo (Capitulo
     * 12). Si ya no esta disponible, no hace nada y
     * devuelve false -- quien lo llame decidira si
     * eso implica cancelar la saga.
     */
    public boolean reservarPara(Long prestamoId) {
        if (!disponible) {
            return false;
        }
        this.disponible = false;
        this.prestamoReservaId = prestamoId;
        return true;
    }

    /**
     * Libera la reserva (compensacion de la saga o
     * devolucion), pero solo si la reserva actual es
     * la de ese mismo prestamo -- evita liberar por
     * error un libro que esta reservado por otro
     * prestamo distinto.
     */
    public void liberarSiEsDe(Long prestamoId) {
        if (prestamoId != null && prestamoId.equals(
                prestamoReservaId)) {
            this.disponible = true;
            this.prestamoReservaId = null;
        }
    }
}
