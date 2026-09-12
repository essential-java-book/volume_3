# Dockerfile de servicio-prestamos (Capitulo 13).
# Build multicapa de 3 fases: 1) compila con
# Maven Wrapper sobre un JDK; 2) extrae las capas
# de Spring Boot (dependencias, loader, snapshot,
# aplicacion) de ese jar; 3) copia solo esas capas
# a una imagen final minima con JRE. El contexto
# de build es la raiz del repo (junto a pom.xml)
# para que el reactor Maven vea los ocho modulos.

FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw
COPY eureka-server/pom.xml eureka-server/
COPY config-server/pom.xml config-server/
COPY api-gateway/pom.xml api-gateway/
COPY authorization-server/pom.xml authorization-server/
COPY servicio-libros/pom.xml servicio-libros/
COPY servicio-usuarios/pom.xml servicio-usuarios/
COPY servicio-prestamos/pom.xml servicio-prestamos/
COPY servicio-notificaciones/pom.xml servicio-notificaciones/

COPY servicio-prestamos/src servicio-prestamos/src
RUN ./mvnw -pl servicio-prestamos -am package -DskipTests

FROM eclipse-temurin:21-jre AS layers
WORKDIR /application
ARG JAR_FILE=/workspace/servicio-prestamos/target/*.jar
COPY --from=build ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar \
    application.jar extract

FROM eclipse-temurin:21-jre
WORKDIR /application
COPY --from=layers /application/dependencies/ ./
COPY --from=layers \
    /application/spring-boot-loader/ ./
COPY --from=layers \
    /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./
EXPOSE 8083
ENTRYPOINT [ \
    "java", \
    "org.springframework.boot.loader.launch.JarLauncher" \
]
