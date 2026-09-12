package com.biblioteca.prestamos.servicio;

import com.biblioteca.prestamos.cliente.LibroCliente;
import com.biblioteca.prestamos.cliente.UsuarioCliente;
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
 * Feign) antes de dar de alta el prestamo. Hasta el
 * Capitulo 12 no hay saga ni compensacion: aqui la
 * validacion es sincrona y no reserva nada todavia.
 */
@Service
public class PrestamoServicio {

    private final PrestamoRepositorio repositorio;
    private final LibroCliente libroCliente;
    private final UsuarioCliente usuarioCliente;

    public PrestamoServicio(
            PrestamoRepositorio repositorio,
            LibroCliente libroCliente,
            UsuarioCliente usuarioCliente) {
        this.repositorio = repositorio;
        this.libroCliente = libroCliente;
        this.usuarioCliente = usuarioCliente;
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
            libroCliente.obtenerLibro(libroId);
        } catch (FeignException.NotFound ex) {
            throw noEncontrado(
                "Libro no encontrado: ", libroId);
        }
    }

    private void validarUsuario(Long usuarioId) {
        try {
            usuarioCliente.obtenerUsuario(usuarioId);
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
