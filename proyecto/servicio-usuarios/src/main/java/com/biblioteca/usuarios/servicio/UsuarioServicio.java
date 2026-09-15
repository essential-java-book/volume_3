package com.biblioteca.usuarios.servicio;

import com.biblioteca.usuarios.dominio.Usuario;
import com.biblioteca.usuarios.dominio.UsuarioNoEncontradoException;
import com.biblioteca.usuarios.repositorio.UsuarioRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio del microservicio de usuarios.
 */
@Service
public class UsuarioServicio {

    private final UsuarioRepositorio repositorio;

    public UsuarioServicio(
            UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Usuario> listarTodos() {
        return repositorio.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repositorio.findById(id)
            .orElseThrow(() ->
                new UsuarioNoEncontradoException(id));
    }

    public Usuario crear(Usuario usuario) {
        return repositorio.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario usuario = buscarPorId(id);
        usuario.setNombre(datos.getNombre());
        usuario.setEmail(datos.getEmail());
        usuario.setNumeroCarne(
            datos.getNumeroCarne());
        return repositorio.save(usuario);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        repositorio.deleteById(id);
    }

    /**
     * Valida que el usuario puede tomar un prestamo
     * mas y registra ese prestamo (participa en la
     * saga del Capitulo 12, al recibir PRESTAMO_
     * CREADO). Devuelve false si el usuario esta
     * inactivo -- quien llama publicara entonces
     * PRESTAMO_CANCELADO.
     */
    public boolean validarYRegistrarPrestamo(Long id,
            Long prestamoId) {
        Usuario usuario = buscarPorId(id);
        boolean exito = usuario
            .registrarPrestamo(prestamoId);
        if (exito) {
            repositorio.save(usuario);
        }
        return exito;
    }

    /**
     * Libera un prestamo (compensacion de la saga o
     * devolucion). Idempotente: ver Usuario.
     * liberarPrestamo.
     */
    public void liberarPrestamo(Long id,
            Long prestamoId) {
        Usuario usuario = buscarPorId(id);
        usuario.liberarPrestamo(prestamoId);
        repositorio.save(usuario);
    }
}
