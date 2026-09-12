package com.biblioteca.prestamos.servicio;

import com.biblioteca.prestamos.dominio.Prestamo;
import com.biblioteca.prestamos.dominio.PrestamoNoEncontradoException;
import com.biblioteca.prestamos.repositorio.PrestamoRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio del microservicio de
 * prestamos. Hasta el Capitulo 6 no valida el
 * libro ni el usuario contra los otros servicios
 * (eso llega con los clientes Feign); hasta el
 * Capitulo 12 no hay saga ni compensacion.
 */
@Service
public class PrestamoServicio {

    private final PrestamoRepositorio repositorio;

    public PrestamoServicio(
            PrestamoRepositorio repositorio) {
        this.repositorio = repositorio;
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
        Prestamo prestamo =
            new Prestamo(libroId, usuarioId);
        return repositorio.save(prestamo);
    }

    public Prestamo marcarDevuelto(Long id) {
        Prestamo prestamo = buscarPorId(id);
        prestamo.marcarDevuelto();
        return repositorio.save(prestamo);
    }
}
