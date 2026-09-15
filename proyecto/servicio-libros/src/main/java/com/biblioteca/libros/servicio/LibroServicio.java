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
     * Reserva el libro para un prestamo (participa
     * en la saga de coreografia del Capitulo 12, al
     * recibir PRESTAMO_CREADO). Devuelve false si el
     * libro ya no estaba disponible -- quien llama
     * (PrestamoEventoListener) publicara entonces
     * PRESTAMO_CANCELADO.
     */
    public boolean reservar(Long id,
            Long prestamoId) {
        Libro libro = buscarPorId(id);
        boolean exito =
            libro.reservarPara(prestamoId);
        if (exito) {
            repositorio.save(libro);
        }
        return exito;
    }

    /**
     * Libera la reserva del libro (compensacion de
     * la saga al recibir PRESTAMO_CANCELADO, o
     * liberacion normal al recibir PRESTAMO_
     * DEVUELTO). Idempotente: si la reserva actual
     * no es la de ese prestamo, no hace nada.
     */
    public void liberarReserva(Long id,
            Long prestamoId) {
        Libro libro = buscarPorId(id);
        libro.liberarSiEsDe(prestamoId);
        repositorio.save(libro);
    }
}
