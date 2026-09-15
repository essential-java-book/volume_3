package com.biblioteca.prestamos.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign hacia servicio-usuarios. Decision
 * canonica (informe SS3.3-21):
 * "UsuarioCliente.obtenerUsuario(id)".
 */
@FeignClient(name = "servicio-usuarios")
public interface UsuarioCliente {

    @GetMapping("/usuarios/{id}")
    UsuarioDto obtenerUsuario(@PathVariable("id") Long id);
}
