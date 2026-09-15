-- CURRENT_DATE + 14 (no DATEADD, especifico de
-- H2): sintaxis de suma de dias valida en
-- PostgreSQL. Se corrige aqui al migrar a
-- Postgres (Capitulo 13); H2 ya no es una
-- dependencia de este modulo.
INSERT INTO prestamos (libro_id, usuario_id,
    fecha_prestamo, fecha_devolucion_prevista,
    estado) VALUES
  (1, 1, CURRENT_DATE,
   CURRENT_DATE + 14, 'ACTIVO'),
  (2, 2, CURRENT_DATE,
   CURRENT_DATE + 14, 'ACTIVO');
