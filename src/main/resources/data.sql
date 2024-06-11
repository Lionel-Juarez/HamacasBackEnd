INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 1, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 1);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 2, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 2);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 3, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 3);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 4, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 4);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 5, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 5);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 6, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 6);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 7, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 7);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 8, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 8);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 9, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 9);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 10, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 10);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 11, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 11);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 12, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 12);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 13, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 13);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 14, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 14);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 15, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 15);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 16, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 16);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 17, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 17);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 18, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 18);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 19, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 19);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 20, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 20);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 21, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 21);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 22, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 22);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 23, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 23);
INSERT INTO sombrilla (cantidad_hamacas, numero_sombrilla, ocupada, precio, reservada, pagada)
SELECT 0, 24, false, 5, false, false FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sombrilla WHERE numero_sombrilla = 24);


-- Insertar datos en la tabla 'usuario' solo si no existen
INSERT INTO usuario (rol, email, nombre_completo, telefono, uid, username)
SELECT 'ADMIN', 'admin@admin.com', 'Lionel Juarez', '600000000', 'KvLFipGQnxWuVjYu5OJGvFihGUx1', 'lioneljuarez' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE uid = 'KvLFipGQnxWuVjYu5OJGvFihGUx1');
INSERT INTO usuario (rol, email, nombre_completo, telefono, uid, username)
SELECT 'TRABAJADOR', 'worker@worker.com', 'Trabajador', '600000000', 'Q6hFHOgV5daczDWTWcTy5silgBs1', 'trabajador' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE uid = 'Q6hFHOgV5daczDWTWcTy5silgBs1');

-- Insertar datos en la tabla 'cliente' solo si no existen
INSERT INTO cliente (nombre_completo, numero_telefono, email, rol, uid)
SELECT 'Administrador', '600000000', 'admin@admin.com', 'ADMIN', 'KvLFipGQnxWuVjYu5OJGvFihGUx1' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE uid = 'KvLFipGQnxWuVjYu5OJGvFihGUx1');
INSERT INTO cliente (nombre_completo, numero_telefono, email, rol, uid)
SELECT 'Trabajador', '600000000', 'worker@worker.com', 'TRABAJADOR', 'Q6hFHOgV5daczDWTWcTy5silgBs1' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE uid = 'Q6hFHOgV5daczDWTWcTy5silgBs1');
INSERT INTO cliente (nombre_completo, numero_telefono, email, rol, uid)
SELECT 'Cliente de prueba', '600000000', 'cliente@cliente.com', 'CLIENTE', 'AWFRc2DPQsfvxrdqcbmRJdKuGWs2' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE uid = 'AWFRc2DPQsfvxrdqcbmRJdKuGWs2');