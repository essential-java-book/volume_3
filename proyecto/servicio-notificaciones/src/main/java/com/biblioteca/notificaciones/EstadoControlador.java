package com.biblioteca.notificaciones;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint minimo para comprobar que el servicio
 * esta arriba y registrado en Eureka. La logica
 * real (consumir Kafka) llega en el Capitulo 8.
 */
@RestController
@RequestMapping("/notificaciones")
public class EstadoControlador {

    @GetMapping("/salud")
    public String salud() {
        return "servicio-notificaciones activo";
    }
}
