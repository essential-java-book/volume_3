package com.biblioteca.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de humo del Capitulo 15: solo confirma que
 * el contexto de Spring arranca sin errores. Sin
 * @ActiveProfiles a proposito: este modulo ya activa
 * "native" por defecto en su application.properties
 * (Capitulo 5) para servir config-repo desde el
 * classpath, y forzar aqui el perfil "test" podria
 * sustituir ese "native" en vez de sumarse a el --
 * misma familia de bug de precedencia de perfiles ya
 * encontrada y corregida en el Capitulo 14 (Kubernetes
 * pisando "native" con su propio perfil). No depende
 * de ningun servicio externo, asi que el pipeline de
 * CI puede ejecutarla en un runner sin infraestructura
 * tal cual, sin necesitar un perfil de test propio.
 */
@SpringBootTest
class ConfigServerApplicationTests {

    @Test
    void elContextoArranca() {
    }
}
