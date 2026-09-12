INSERT INTO prestamos (libro_id, usuario_id,
    fecha_prestamo, fecha_devolucion_prevista,
    estado) VALUES
  (1, 1, CURRENT_DATE,
   DATEADD('DAY', 14, CURRENT_DATE), 'ACTIVO'),
  (2, 2, CURRENT_DATE,
   DATEADD('DAY', 14, CURRENT_DATE), 'ACTIVO');
