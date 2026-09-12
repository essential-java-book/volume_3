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
     * Valida que el usuario puede tomar un
     * prestamo mas y actualiza su contador. Lo usa
     * la saga del Capitulo 12.
     */
    public boolean validarYRegistrarPrestamo(Long id) {
        Usuario usuario = buscarPorId(id);
        if (!usuario.isActivo()) {
            return false;
        }
        usuario.incrementarPrestamosActivos();
        repositorio.save(usuario);
        return true;
    }

    public void liberarPrestamo(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.decrementarPrestamosActivos();
        repositorio.save(usuario);
    }
}
