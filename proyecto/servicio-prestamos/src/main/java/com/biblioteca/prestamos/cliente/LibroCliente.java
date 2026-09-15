package com.biblioteca.prestamos.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign hacia servicio-libros. El nombre
 * ("servicio-libros") se resuelve via Eureka --
 * nunca una URL fija. Decision canonica (informe
 * SS3.3-21): "LibroCliente.obtenerLibro(id)".
 */
@FeignClient(name = "servicio-libros")
public interface LibroCliente {

    @GetMapping("/libros/{id}")
    LibroDto obtenerLibro(@PathVariable("id") Long id);
}
