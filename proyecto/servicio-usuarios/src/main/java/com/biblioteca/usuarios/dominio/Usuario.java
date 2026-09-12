package com.biblioteca.usuarios.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Usuario (socio) de la Biblioteca Municipal
 * "El Quijote". Pasara a llamarse "Socio" en el
 * Volumen 5 (informe de coherencia, SS3.2-8): aqui
 * sigue siendo "Usuario", como en los Vols. 1-4.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    private String email;

    @Column(name = "numero_carne", unique = true)
    private String numeroCarne;

    private boolean activo = true;

    /**
     * Prestamos activos ahora mismo. Lo mantiene
     * al dia la saga del Capitulo 12 (informe de
     * coherencia, hallazgo 3-D-20).
     */
    private int prestamosActivos = 0;

    protected Usuario() {
        // JPA
    }

    public Usuario(String nombre, String email,
            String numeroCarne) {
        this.nombre = nombre;
        this.email = email;
        this.numeroCarne = numeroCarne;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroCarne() {
        return numeroCarne;
    }

    public void setNumeroCarne(String numeroCarne) {
        this.numeroCarne = numeroCarne;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getPrestamosActivos() {
        return prestamosActivos;
    }

    public void incrementarPrestamosActivos() {
        this.prestamosActivos++;
    }

    public void decrementarPrestamosActivos() {
        if (this.prestamosActivos > 0) {
            this.prestamosActivos--;
        }
    }
}
