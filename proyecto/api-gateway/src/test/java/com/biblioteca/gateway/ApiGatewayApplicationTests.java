package com.biblioteca.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de humo del Capitulo 15: solo confirma que
 * el contexto reactivo arranca sin errores. El
 * decodificador JWT del resource server se construye
 * de forma perezosa (no llama a jwk-set-uri hasta
 * validar un token de verdad) y el registro en Eureka
 * no bloquea el arranque si el servidor no esta
 * disponible, asi que el pipeline de CI puede
 * ejecutarla en un runner sin infraestructura.
 */
@SpringBootTest(webEnvironment =
    SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiGatewayApplicationTests {

    @Test
    void elContextoArranca() {
    }
}
