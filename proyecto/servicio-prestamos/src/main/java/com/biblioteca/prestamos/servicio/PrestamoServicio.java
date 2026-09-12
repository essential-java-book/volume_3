package com.biblioteca.prestamos.servicio;

import com.biblioteca.prestamos.cliente.ValidadorRelaciones;
import com.biblioteca.prestamos.dominio.Prestamo;
import com.biblioteca.prestamos.dominio.PrestamoNoEncontradoException;
import com.biblioteca.prestamos.dominio.RecursoRelacionadoNoEncontradoException;
import com.biblioteca.prestamos.evento.PrestamoEventoPublicador;
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
 * ServicioNoDisponibleException. Desde el Capitulo
 * 8, "crear" y "marcarDevuelto" publican un evento
 * en Kafka despues de guardar -- todavia sin saga ni
 * compensacion (eso llega en el Capitulo 12).
 */
@Service
public class PrestamoServicio {

    private final PrestamoRepositorio repositorio;
    private final ValidadorRelaciones validador;
    private final PrestamoEventoPublicador publicador;

    public PrestamoServicio(
            PrestamoRepositorio repositorio,
            ValidadorRelaciones validador,
            PrestamoEventoPublicador publicador) {
        this.repositorio = repositorio;
        this.validador = validador;
        this.publicador = publicador;
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
        Prestamo guardado =
            repositorio.save(prestamo);
        publicador.publicarCreado(
            guardado.getId(), libroId, usuarioId);
        return guardado;
    }

    public Prestamo marcarDevuelto(Long id) {
        Prestamo prestamo = buscarPorId(id);
        prestamo.marcarDevuelto();
        Prestamo guardado =
            repositorio.save(prestamo);
        publicador.publicarDevuelto(guardado.getId(),
            guardado.getLibroId(),
            guardado.getUsuarioId());
        return guardado;
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
