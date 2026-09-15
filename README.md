# Java Esencial · Volumen 3 — Microservicios: Del monolito a un sistema distribuido en producción

Material complementario del libro **«Java Esencial: De Principiante a
Experto. Volumen 3: Microservicios — Del monolito a un sistema
distribuido en producción»**, de Basilio Fajardo Gálvez. Tercer
repositorio de la colección (`volume_1` … `volume_6`), misma
estructura que el resto.

Aquí encontrarás el **Proyecto Biblioteca** descompuesto en
microservicios (parte del monolito Spring Boot del Volumen 2) tal
como queda al terminar cada capítulo, más las prácticas, los tests
de repaso y las chuletas de cada capítulo.

## Estructura

| Carpeta | Contenido |
|---|---|
| `proyecto/` | El Proyecto Biblioteca (Maven multi-módulo, Spring Cloud). Un tag por capítulo (`v3-cap01` … `v3-cap15`); `v3.0.0` es el estado final del volumen. |
| `practicas/` | Solución de cada práctica del libro, en `capNN/practicaN.M.md`. Pendiente (se añade al cerrar el volumen). |
| `quizzes/` | Tests de conocimientos interactivos por capítulo. Pendiente. |
| `cheatsheets/` | Chuletas de una página por capítulo. Pendiente. |
| `tools/` | Scripts para verificar el proyecto capítulo a capítulo (Linux/macOS: `.sh`; Windows: `.ps1`). |

## El proyecto capítulo a capítulo

Cada capítulo del libro termina con una sección «Proyecto Biblioteca
— Capítulo N». El código de esa sección es el que hay en el tag
`v3-capNN`:

```bash
git clone https://github.com/essential-java-book/volume_3.git
cd volume_3
git checkout v3-cap08     # el proyecto tal como queda al terminar el capítulo 8
git checkout master       # volver al estado final
```

Si no usas Git, en la página del repositorio elige el tag en el
desplegable de ramas (**master ▾ → Tags**) y descarga el ZIP de ese
capítulo.

| Cap. | Tag | Lo que añade al proyecto |
|---|---|---|
| 1 | `v3-cap01` | Sin código -- estado de partida heredado del Volumen 2 |
| 2 | `v3-cap02` | Multi-módulo `biblioteca-microservicios`; `servicio-libros`, `servicio-usuarios`, `servicio-prestamos` con Spring Data JPA + H2; `EstadoPrestamo` completo; Maven Wrapper |
| 3 | `v3-cap03` | `eureka-server`; `@EnableDiscoveryClient` en los tres servicios; esqueleto de `servicio-notificaciones` |
| 4 | `v3-cap04` | `api-gateway` con rutas `/libros/**`, `/usuarios/**`, `/prestamos/**` |
| 5 | `v3-cap05` | `config-server` y `config-repo/` para los ocho módulos |
| 6 | `v3-cap06` | Clientes Feign (`LibroCliente`, `UsuarioCliente`) e `InterceptorCorrelacion` |
| 7 | `v3-cap07` | Resilience4j (circuit breaker / retry) en `servicio-prestamos` |
| 8 | `v3-cap08` | Kafka (`prestamos-eventos`, `PrestamoEvento`/`TipoEvento`); `servicio-notificaciones` consumidor |
| 9 | `v3-cap09` | Trazabilidad distribuida con Zipkin |
| 10 | `v3-cap10` | Logging centralizado con ELK |
| 11 | `v3-cap11` | `authorization-server`, resource servers, roles `USUARIO`/`BIBLIOTECARIO` |
| 12 | `v3-cap12` | Outbox, saga por coreografía, proyección de lectura (CQRS) |
| 13 | `v3-cap13` | Dockerfiles y `docker-compose.yml` de los ocho módulos; PostgreSQL + Flyway por servicio |
| 14 | `v3-cap14` | Manifiestos de Kubernetes (Deployment/Service/Ingress) |
| 15 | `v3-cap15` | GitHub Actions (CI/CD) + Helm (`.github/workflows/`, `helm/biblioteca/`) · `v3.0.0` |

## Cómo compilar y ejecutar

Requisitos: JDK 21 (Temurin recomendado) y Maven 3.9+. Desde el
capítulo 13, Docker y Docker Compose; desde el capítulo 14, un
clúster de Kubernetes local (minikube); desde el capítulo 15,
Helm si quieres desplegar con `helm/biblioteca/` en vez de los
manifiestos sueltos de `k8s/`.

```bash
cd proyecto
mvn clean package                      # compila los módulos existentes en cada tag
mvn -pl servicio-libros spring-boot:run    # arranca un módulo suelto (perfil dev, H2)

docker compose up --build              # cap. 13 en adelante
```

Ver `proyecto/README.md` para el detalle de arranque de cada módulo.

### Scripts de `tools/`

| Script | Qué hace |
|---|---|
| `verificar-todo.sh` / `verificar-todo.ps1` | Recorre los 15 tags: compila cada uno con Maven (reactor completo de `proyecto/`) y, desde que existen tests, los ejecuta también. |

En Windows: `powershell -ExecutionPolicy Bypass -File tools\verificar-todo.ps1` desde la raíz del repositorio, con `git`, `mvn` y `java` en el PATH.

## CI/CD (Capítulo 15)

`.github/workflows/cicd.yml` compila, testea, construye y
publica en Docker Hub las ocho imágenes, despliega a staging
y, con aprobación manual, a producción. `.github/workflows/
pr-check.yml` es la comprobación ligera de cada Pull Request
(Checkstyle + `mvn verify`). El despliegue usa el Chart de
Helm de `helm/biblioteca/` (nuevo en este capítulo: el
Capítulo 14 usaba Kubernetes sin Helm a propósito) — con los
valores por defecto de `values.yaml` reproduce exactamente
los mismos manifiestos que ya había en `k8s/`; `values-
staging.yaml` y `values-prod.yaml` sólo cambian lo que varía
por entorno.

Los jobs `ci` y `docker-build` se pueden ejecutar de verdad
con solo los secrets `DOCKERHUB_USERNAME`/`DOCKERHUB_TOKEN`
configurados en el repositorio. `deploy-staging` y `deploy-
production` necesitan un clúster de Kubernetes real y
accesible desde un runner de GitHub Actions en la nube — el
único clúster de este proyecto es el minikube local, al que
un runner en la nube no llega, así que esos dos jobs quedan
como código correcto según el manuscrito, sin ejecutar contra
ningún clúster real (ver `CHANGELOG-capitulos.md`, Cap. 15).

## Reglas del proyecto (para que tu código coincida con el del libro)

- `groupId` `com.biblioteca` (cambia del `com.javaesencial` de los Vols. 1-2); `artifactId` del POM padre `biblioteca-microservicios`; raíz `biblioteca/` dentro de `proyecto/`.
- Paquete de dominio: `dominio` (`com.biblioteca.<servicio>.dominio`) -- **nunca `modelo`** en ningún módulo.
- Persistencia: Spring Data JPA + H2 desde el capítulo 2 (nunca `HashMap`); PostgreSQL + Flyway por servicio desde el capítulo 13.
- `EstadoPrestamo` (enum de nivel superior, en `dominio` de `servicio-prestamos`): `SOLICITADO, ACTIVO, DEVUELTO, VENCIDO, CANCELADO` -- completo desde el capítulo 2.
- Ocho módulos: `eureka-server`, `config-server`, `api-gateway`, `authorization-server`, `servicio-libros` (8081), `servicio-usuarios` (8082), `servicio-prestamos` (8083), `servicio-notificaciones` (8084).
- Rutas del gateway: `/libros/**`, `/usuarios/**`, `/prestamos/**` (nunca `/libros-service/...`, `/prestamos-service/...`).
- Tablas en plural, una base de datos por servicio: `libros_db`, `usuarios_db`, `prestamos_db`.
- Roles `USUARIO`/`BIBLIOTECARIO` (no `ROLE_USER`/`ROLE_ADMIN` del Vol. 2); secreto en `.env`/Secret: `ENCRYPT_KEY` (Config Server), nunca `JWT_SECRET`.
- Kafka: topic `prestamos-eventos`; enum `TipoEvento` de nivel superior en `com.biblioteca.prestamos.evento`.
- Ninguna línea de código supera los 66 caracteres.
- Código en español (identificadores sin tildes ni ñ), comentarios en español de España; marcadores `[OK]`/`[XX]`/`[!]` y marcos ASCII donde el capítulo muestre salida de consola o terminal -- nunca ✓/✗ ni cajas `═`.

## La colección

| Vol. | Título | Repositorio |
|---|---|---|
| 1 | Fundamentos del Lenguaje | `volume_1` |
| 2 | Spring Boot — De la consola a una API REST profesional | `volume_2` |
| 3 | Microservicios — Del monolito a un sistema distribuido en producción | `volume_3` (este) |
| 4 | Java de Alta Demanda | `volume_4` |
| 5 | Arquitectura Empresarial — DDD, Kafka, Seguridad y Java Moderno | `volume_5` |
| 6 | Java Inteligente | `volume_6` |

¿Has encontrado una errata o un error en el código? Abre un *issue*
en este repositorio.
