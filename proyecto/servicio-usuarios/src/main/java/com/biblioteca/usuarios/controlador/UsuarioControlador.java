package com.biblioteca.usuarios.controlador;

import com.biblioteca.usuarios.dominio.Usuario;
import com.biblioteca.usuarios.servicio.UsuarioServicio;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * API REST del microservicio de usuarios. No
 * existia en el Vol. 2 (informe de coherencia,
 * SS3.5) -- nace aqui, en el Volumen 3.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio servicio;

    public UsuarioControlador(
            UsuarioServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return servicio.listarTodos();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return servicio.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(
            @Valid @RequestBody Usuario usuario) {
        Usuario creado = servicio.crear(usuario);
        URI ubicacion = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creado.getId())
            .toUri();
        return ResponseEntity.created(ubicacion)
            .body(creado);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id,
            @Valid @RequestBody Usuario datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
