package com.biblioteca.prestamos.controlador;

import com.biblioteca.prestamos.dominio.PrestamoNoEncontradoException;
import com.biblioteca.prestamos.proyeccion.PrestamoProyeccion;
import com.biblioteca.prestamos.proyeccion.PrestamoProyeccionRepositorio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lado de lectura (CQRS, Capitulo 12): expone la
 * proyeccion que mantiene PrestamoProyeccionListener,
 * separada a proposito del PrestamoRepositorio de
 * escritura y de PrestamoControlador. El error 404
 * lo traduce el mismo ManejadorErrores que ya
 * existia para PrestamoNoEncontradoException.
 */
@RestController
@RequestMapping("/prestamos")
public class PrestamoProyeccionControlador {

    private final PrestamoProyeccionRepositorio
        repositorio;

    public PrestamoProyeccionControlador(
            PrestamoProyeccionRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping("/{id}/proyeccion")
    public PrestamoProyeccion buscarProyeccion(
            @PathVariable Long id) {
        return repositorio.findById(id)
            .orElseThrow(() ->
                new PrestamoNoEncontradoException(
                    id));
    }
}
