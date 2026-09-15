package com.biblioteca.authorization.configuracion;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

/**
 * Anade el claim "roles" al token de acceso, con
 * los nombres de rol tal cual (sin el prefijo
 * "ROLE_" que usa Spring Security por dentro) --
 * asi cada resource server los lee sin acoplarse a
 * esa convencion interna.
 */
@Component
public class PersonalizadorToken implements
        OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final String PREFIJO_ROL =
        "ROLE_";

    @Override
    public void customize(
            JwtEncodingContext contexto) {
        List<String> roles = contexto.getPrincipal()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(autoridad -> autoridad
                .startsWith(PREFIJO_ROL))
            .map(autoridad -> autoridad.substring(
                PREFIJO_ROL.length()))
            .toList();
        contexto.getClaims().claim("roles", roles);
    }
}
