package com.biblioteca.notificaciones;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de humo del Capitulo 15: solo confirma que
 * el contexto arranca sin errores. Sin base de datos
 * (esqueleto del Capitulo 3, consumidor Kafka real
 * desde el Capitulo 8); el listener de Kafka no
 * bloquea el arranque si el broker no esta disponible,
 * asi que el pipeline de CI puede ejecutarla en un
 * runner sin infraestructura.
 */
@SpringBootTest
@ActiveProfiles("test")
class ServicioNotificacionesApplicationTests {

    @Test
    void elContextoArranca() {
    }
}
