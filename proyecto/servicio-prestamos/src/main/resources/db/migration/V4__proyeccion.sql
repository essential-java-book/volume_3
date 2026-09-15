CREATE TABLE prestamos_proyeccion (
    prestamo_id BIGINT PRIMARY KEY,
    libro_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_prevista DATE NOT NULL,
    fecha_devolucion_real DATE,
    actualizado_en TIMESTAMP NOT NULL
);
