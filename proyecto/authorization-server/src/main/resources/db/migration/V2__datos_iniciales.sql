-- Los hashes bcrypt (68 caracteres) se dividen en
-- dos literales concatenados con "||" (ANSI SQL,
-- valido en H2 y en PostgreSQL) solo para respetar
-- el limite de 66 columnas -- el valor resultante
-- es el mismo hash completo.
INSERT INTO credenciales (username, password,
    rol) VALUES
  ('usuario1',
   '{bcrypt}$2b$10$Pg/svYKclSWisCSp7R4' ||
   'GSeblDMFo2T.BySBRSppR9KSo6Rj90Gyta',
   'USUARIO'),
  ('bibliotecario1',
   '{bcrypt}$2b$10$ZAKWv.lrGs8iP0Pc/33' ||
   'F1.PiVJdUek5tpkw0eyBQhyfXicbE37Jwm',
   'BIBLIOTECARIO');
