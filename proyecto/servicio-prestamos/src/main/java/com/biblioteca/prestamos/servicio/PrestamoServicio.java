package com.biblioteca.prestamos.servicio;

import com.biblioteca.prestamos.cliente.ValidadorRelaciones;
import com.biblioteca.prestamos.dominio.EstadoPrestamo;
import com.biblioteca.prestamos.dominio.Prestamo;
import com.biblioteca.prestamos.dominio.PrestamoNoEncontradoException;
import com.biblioteca.prestamos.dominio.RecursoRelacionadoNoEncontradoException;
import com.biblioteca.prestamos.evento.PrestamoEventoPublicador;
import com.biblioteca.prestamos.repositorio.PrestamoRepositorio;
import feign.FeignException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * 12, "crear" crea el prestamo SOLICITADO y lo
 * confirma de inmediato (finalizacion optimista de
 * la saga de coreografia): el guardado del Prestamo
 * y la escritura en el Outbox comparten transaccion
 * (@Transactional), y si servicio-libros o
 * servicio-usuarios no puede completar su parte,
 * cancelarPorCompensacion() deshace esto despues, de
 * forma asincrona.
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

    @Transactional
    public Prestamo crear(Long libroId,
            Long usuarioId) {
        validarLibro(libroId);
        validarUsuario(usuarioId);
        Prestamo prestamo =
            new Prestamo(libroId, usuarioId);
        Prestamo guardado =
            repositorio.save(prestamo);
        guardado.confirmar();
        Prestamo confirmado =
            repositorio.save(guardado);
        publicador.publicarCreado(
            confirmado.getId(), libroId, usuarioId);
        return confirmado;
    }

    @Transactional
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

    /**
     * Compensacion de la saga (Capitulo 12): la
     * invoca PrestamoCompensacionListener al recibir
     * PRESTAMO_CANCELADO. No cancela un prestamo ya
     * DEVUELTO -- una compensacion tardia nunca debe
     * deshacer una devolucion real.
     */
    @Transactional
    public void cancelarPorCompensacion(
            Long prestamoId) {
        Prestamo prestamo = buscarPorId(prestamoId);
        if (prestamo.getEstado()
                != EstadoPrestamo.DEVUELTO) {
            prestamo.cancelar();
            repositorio.save(prestamo);
        }
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
