package com.biblioteca.prestamos.controlador;

import com.biblioteca.prestamos.dominio.Prestamo;
import com.biblioteca.prestamos.servicio.PrestamoServicio;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * API REST del microservicio de prestamos.
 */
@RestController
@RequestMapping("/prestamos")
public class PrestamoControlador {

    private final PrestamoServicio servicio;

    public PrestamoControlador(
            PrestamoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Prestamo> listarTodos() {
        return servicio.listarTodos();
    }

    @GetMapping("/{id}")
    public Prestamo buscarPorId(
            @PathVariable Long id) {
        return servicio.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Prestamo> buscarPorUsuario(
            @PathVariable Long usuarioId) {
        return servicio
            .buscarPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<Prestamo> crear(
            @Valid @RequestBody
            PeticionPrestamo peticion) {
        Prestamo creado = servicio.crear(
            peticion.libroId(),
            peticion.usuarioId());
        URI ubicacion = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creado.getId())
            .toUri();
        return ResponseEntity.created(ubicacion)
            .body(creado);
    }

    /**
     * Verbo "/devolver" (decision canonica
     * SS3.4-20; corregido en el Capitulo 12 desde
     * "/devolucion", que no seguia el informe).
     */
    @PatchMapping("/{id}/devolver")
    public Prestamo marcarDevuelto(
            @PathVariable Long id) {
        return servicio.marcarDevuelto(id);
    }
}
