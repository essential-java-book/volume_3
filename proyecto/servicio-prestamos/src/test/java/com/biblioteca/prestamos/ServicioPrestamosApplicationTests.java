package com.biblioteca.prestamos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de humo del Capitulo 15: solo confirma que
 * el contexto arranca sin errores. El unico bloqueo
 * real para correr esto en un runner de CI era
 * PostgreSQL -- resuelto activando el perfil "test"
 * (application-test.properties: H2 en memoria, Flyway
 * desactivado, esquema generado por Hibernate). Los
 * clientes Feign (LibroCliente, UsuarioCliente) son
 * proxies perezosos -- no llaman a nadie hasta que se
 * invoca un metodo, asi que no bloquean el arranque
 * aunque los otros servicios no esten disponibles.
 */
@SpringBootTest
@ActiveProfiles("test")
class ServicioPrestamosApplicationTests {

    @Test
    void elContextoArranca() {
    }
}
