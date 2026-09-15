package com.biblioteca.authorization.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Credencial de acceso (usuario/clave/rol). En el
 * monolito del Vol. 2 vivia dentro del proyecto
 * unico (tabla "credencial", JWT HS256 propio); en
 * el Vol. 3 se aisla aqui, en el authorization-
 * server, que es quien emite el JWT (RSA) que
 * todos los demas modulos validan (informe SS3.4-27).
 * Roles: "USUARIO" / "BIBLIOTECARIO" (renombrado
 * desde ROLE_USER/ROLE_ADMIN del Vol. 2).
 */
@Entity
@Table(name = "credenciales")
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String rol;

    protected Credencial() {
        // JPA
    }

    public Credencial(String username,
            String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }
}
