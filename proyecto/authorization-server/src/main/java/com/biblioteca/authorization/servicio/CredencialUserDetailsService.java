package com.biblioteca.authorization.servicio;

import com.biblioteca.authorization.dominio.Credencial;
import com.biblioteca.authorization.repositorio.CredencialRepositorio;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Carga la credencial desde "credenciales" (H2 en
 * este capitulo, PostgreSQL "auth_db" desde el
 * Capitulo 13) y la traduce a un UserDetails con
 * una unica autoridad "ROLE_" + rol -- por ejemplo,
 * "ROLE_BIBLIOTECARIO". PersonalizadorToken quita
 * ese prefijo al escribir el claim "roles" del JWT.
 */
@Service
public class CredencialUserDetailsService
        implements UserDetailsService {

    private final CredencialRepositorio repositorio;

    public CredencialUserDetailsService(
            CredencialRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {
        Credencial credencial = repositorio
            .findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    username));
        return User
            .withUsername(credencial.getUsername())
            .password(credencial.getPassword())
            .roles(credencial.getRol())
            .build();
    }
}
