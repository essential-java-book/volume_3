package com.biblioteca.libros.controlador;

import com.biblioteca.libros.dominio.Libro;
import com.biblioteca.libros.servicio.LibroServicio;
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
 * API REST del microservicio de libros.
 * Ruta base "/libros" -- el prefijo "/api" llega
 * en el Volumen 4 (informe de coherencia, SS3.3-19).
 */
@RestController
@RequestMapping("/libros")
public class LibroControlador {

    private final LibroServicio servicio;

    public LibroControlador(LibroServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Libro> listarTodos() {
        return servicio.listarTodos();
    }

    @GetMapping("/{id}")
    public Libro buscarPorId(@PathVariable Long id) {
        return servicio.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Libro> crear(
            @Valid @RequestBody Libro libro) {
        Libro creado = servicio.crear(libro);
        URI ubicacion = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creado.getId())
            .toUri();
        return ResponseEntity.created(ubicacion)
            .body(creado);
    }

    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable Long id,
            @Valid @RequestBody Libro datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
