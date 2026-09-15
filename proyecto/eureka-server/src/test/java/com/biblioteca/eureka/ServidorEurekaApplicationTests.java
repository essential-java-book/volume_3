package com.biblioteca.eureka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de humo del Capitulo 15: solo confirma que
 * el contexto de Spring arranca sin errores. No
 * necesita ningun servicio externo (Eureka Server no
 * depende de nada mas), asi que el pipeline de CI
 * puede ejecutarla en un runner sin infraestructura.
 */
@SpringBootTest
@ActiveProfiles("test")
class ServidorEurekaApplicationTests {

    @Test
    void elContextoArranca() {
    }
}
