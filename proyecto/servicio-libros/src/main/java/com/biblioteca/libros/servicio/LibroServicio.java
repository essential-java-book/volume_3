package com.biblioteca.libros.servicio;

import com.biblioteca.libros.dominio.Libro;
import com.biblioteca.libros.dominio.LibroNoEncontradoException;
import com.biblioteca.libros.repositorio.LibroRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Logica de negocio del microservicio de libros.
 */
@Service
public class LibroServicio {

    private final LibroRepositorio repositorio;

    public LibroServicio(LibroRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Libro> listarTodos() {
        return repositorio.findAll();
    }

    public Libro buscarPorId(Long id) {
        return repositorio.findById(id)
            .orElseThrow(() ->
                new LibroNoEncontradoException(id));
    }

    public Libro crear(Libro libro) {
        return repositorio.save(libro);
    }

    public Libro actualizar(Long id, Libro datos) {
        Libro libro = buscarPorId(id);
        libro.setTitulo(datos.getTitulo());
        libro.setAutor(datos.getAutor());
        libro.setIsbn(datos.getIsbn());
        libro.setAnioPublicacion(
            datos.getAnioPublicacion());
        return repositorio.save(libro);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        repositorio.deleteById(id);
    }

    /**
     * Marca el libro como no disponible. Lo usara
     * la saga del Capitulo 12 al reservarlo para
     * un prestamo.
     */
    public void reservar(Long id) {
        Libro libro = buscarPorId(id);
        libro.marcarNoDisponible();
        repositorio.save(libro);
    }

    /**
     * Libera la reserva (compensacion de la saga).
     */
    public void liberarReserva(Long id) {
        Libro libro = buscarPorId(id);
        libro.marcarDisponible();
        repositorio.save(libro);
    }
}
