package com.biblioteca.libros;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de humo del Capitulo 15: solo confirma que
 * el contexto arranca sin errores. El unico bloqueo
 * real para correr esto en un runner de CI era
 * PostgreSQL -- resuelto activando el perfil "test"
 * (application-test.properties: H2 en memoria, Flyway
 * desactivado, esquema generado por Hibernate). El
 * consumidor Kafka no bloquea el arranque si el
 * broker no esta disponible (reintenta en segundo
 * plano, igual que se comprobo en el Capitulo 14).
 */
@SpringBootTest
@ActiveProfiles("test")
class ServicioLibrosApplicationTests {

    @Test
    void elContextoArranca() {
    }
}
