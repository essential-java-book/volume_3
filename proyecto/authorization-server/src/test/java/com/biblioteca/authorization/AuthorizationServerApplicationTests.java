package com.biblioteca.authorization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de humo del Capitulo 15: solo confirma que
 * el contexto arranca sin errores. Las claves RSA se
 * generan en memoria (Capitulo 11, sin almacen
 * externo), asi que el unico bloqueo real para correr
 * esto en un runner de CI era PostgreSQL -- resuelto
 * activando el perfil "test" (application-test.
 * properties: H2 en memoria, Flyway desactivado,
 * esquema generado por Hibernate desde las entidades).
 */
@SpringBootTest
@ActiveProfiles("test")
class AuthorizationServerApplicationTests {

    @Test
    void elContextoArranca() {
    }
}
