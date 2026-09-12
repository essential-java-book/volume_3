package com.biblioteca.prestamos.servicio;

import com.biblioteca.prestamos.cliente.ValidadorRelaciones;
import com.biblioteca.prestamos.dominio.Prestamo;
import com.biblioteca.prestamos.dominio.PrestamoNoEncontradoException;
import com.biblioteca.prestamos.dominio.RecursoRelacionadoNoEncontradoException;
import com.biblioteca.prestamos.repositorio.PrestamoRepositorio;
import feign.FeignException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio del microservicio de prestamos.
 * Desde el Capitulo 6, "crear" valida el libro y el
 * usuario contra sus propios microservicios (via
 * ValidadorRelaciones, que envuelve los clientes
 * Feign). Desde el Capitulo 7, esas llamadas llevan
 * circuit breaker y reintentos (Resilience4j); un
 * 404 sigue siendo "no existe" (RecursoRelacionado-
 * NoEncontradoException), mientras que un fallo
 * tecnico del servicio dependiente se traduce en
 * ServicioNoDisponibleException. Hasta el Capitulo
 * 12 no hay saga ni compensacion.
 */
@Service
public class PrestamoServicio {

    private final PrestamoRepositorio repositorio;
    private final ValidadorRelaciones validador;

    public PrestamoServicio(
            PrestamoRepositorio repositorio,
            ValidadorRelaciones validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }

    public List<Prestamo> listarTodos() {
        return repositorio.findAll();
    }

    public Prestamo buscarPorId(Long id) {
        return repositorio.findById(id)
            .orElseThrow(() ->
                new PrestamoNoEncontradoException(id));
    }

    public List<Prestamo> buscarPorUsuario(
            Long usuarioId) {
        return repositorio
            .findByUsuarioId(usuarioId);
    }

    public Prestamo crear(Long libroId,
            Long usuarioId) {
        validarLibro(libroId);
        validarUsuario(usuarioId);
        Prestamo prestamo =
            new Prestamo(libroId, usuarioId);
        return repositorio.save(prestamo);
    }

    public Prestamo marcarDevuelto(Long id) {
        Prestamo prestamo = buscarPorId(id);
        prestamo.marcarDevuelto();
        return repositorio.save(prestamo);
    }

    private void validarLibro(Long libroId) {
        try {
            validador.obtenerLibro(libroId);
        } catch (FeignException.NotFound ex) {
            throw noEncontrado(
                "Libro no encontrado: ", libroId);
        }
    }

    private void validarUsuario(Long usuarioId) {
        try {
            validador.obtenerUsuario(usuarioId);
        } catch (FeignException.NotFound ex) {
            throw noEncontrado(
                "Usuario no encontrado: ", usuarioId);
        }
    }

    private RecursoRelacionadoNoEncontradoException
            noEncontrado(String prefijo, Long id) {
        return new
            RecursoRelacionadoNoEncontradoException(
                prefijo + id);
    }
}
