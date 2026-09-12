package com.biblioteca.prestamos.cliente;

import com.biblioteca.prestamos.dominio.ServicioNoDisponibleException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

/**
 * Punto unico por el que servicio-prestamos llama a
 * servicio-libros/servicio-usuarios. En un bean
 * aparte (y no en PrestamoServicio) a proposito: las
 * anotaciones de Resilience4j solo funcionan a
 * traves del proxy de Spring, y una llamada de un
 * metodo a otro dentro de la misma clase no pasa por
 * el proxy. Los nombres de instancia ("servicio-
 * libros", "servicio-usuarios") coinciden con el
 * nombre de cada @FeignClient (informe SS3.3-21).
 * Un 404 (FeignException.NotFound) esta configurado
 * como "ignore-exceptions": no es un fallo tecnico,
 * es que el libro o el usuario no existe -- no debe
 * abrir el circuito ni disparar el fallback.
 */
@Component
public class ValidadorRelaciones {

    private final LibroCliente libroCliente;
    private final UsuarioCliente usuarioCliente;

    public ValidadorRelaciones(
            LibroCliente libroCliente,
            UsuarioCliente usuarioCliente) {
        this.libroCliente = libroCliente;
        this.usuarioCliente = usuarioCliente;
    }

    @CircuitBreaker(name = "servicio-libros",
        fallbackMethod = "libroNoDisponible")
    @Retry(name = "servicio-libros")
    public LibroDto obtenerLibro(Long id) {
        return libroCliente.obtenerLibro(id);
    }

    @CircuitBreaker(name = "servicio-usuarios",
        fallbackMethod = "usuarioNoDisponible")
    @Retry(name = "servicio-usuarios")
    public UsuarioDto obtenerUsuario(Long id) {
        return usuarioCliente.obtenerUsuario(id);
    }

    private LibroDto libroNoDisponible(Long id,
            Throwable causa) {
        throw new ServicioNoDisponibleException(
            "servicio-libros no disponible", causa);
    }

    private UsuarioDto usuarioNoDisponible(Long id,
            Throwable causa) {
        throw new ServicioNoDisponibleException(
            "servicio-usuarios no disponible", causa);
    }
}
