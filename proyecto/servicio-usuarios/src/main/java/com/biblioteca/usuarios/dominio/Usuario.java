package com.biblioteca.usuarios.dominio;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

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

    /**
     * Ids de los prestamos que ya se han contado
     * en prestamosActivos (Capitulo 12): sin este
     * registro, una compensacion tardia podria
     * restar un prestamo que este usuario nunca
     * llego a registrar (por ejemplo, si la validacion
     * fallo porque el usuario estaba inactivo).
     */
    @ElementCollection
    @CollectionTable(
        name = "usuario_prestamos_registrados",
        joinColumns = @JoinColumn(
            name = "usuario_id"))
    @Column(name = "prestamo_id")
    private Set<Long> prestamosRegistrados =
        new HashSet<>();

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

    /**
     * Registra un prestamo nuevo para este usuario
     * (participa en la saga del Capitulo 12, al
     * recibir PRESTAMO_CREADO). Devuelve false sin
     * cambiar nada si el usuario esta inactivo --
     * quien llama publicara entonces PRESTAMO_
     * CANCELADO. Idempotente frente a reentregas: si
     * ese prestamoId ya estaba registrado, no vuelve
     * a incrementar el contador.
     */
    public boolean registrarPrestamo(
            Long prestamoId) {
        if (!activo) {
            return false;
        }
        if (prestamosRegistrados.add(prestamoId)) {
            prestamosActivos++;
        }
        return true;
    }

    /**
     * Libera un prestamo (compensacion de la saga o
     * devolucion). Si ese prestamoId no estaba
     * registrado -- por ejemplo, porque este mismo
     * usuario fue quien hizo fallar la validacion --
     * no hace nada: nunca se resta lo que no se
     * llego a sumar.
     */
    public void liberarPrestamo(Long prestamoId) {
        if (prestamosRegistrados.remove(prestamoId)
                && prestamosActivos > 0) {
            prestamosActivos--;
        }
    }
}
