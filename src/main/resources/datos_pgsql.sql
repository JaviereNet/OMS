-- datos_pgsql.sql: datos de prueba para el OMS (almacén)
-- Convertido para PostgreSQL (usado por Render)
-- NOTA: Se asume que las tablas existen y están vacías; por eso se usan INSERT INTO simples.

-- -----------------------
-- PRODUCTOS
-- columnas: id, categoria, codigo, descripcion, marca, nombre, precio
-- -----------------------
INSERT INTO productos (id, categoria, codigo, descripcion, marca, nombre, precio) VALUES
 (1, 'Ropa', 'P100', 'Camiseta 100% algodón talla M', 'MarcaA', 'Camiseta Azul', 15.50),
 (2, 'Hogar', 'P200', 'Taza cerámica 350ml', 'MarcaB', 'Taza Café', 4.99),
 (3, 'Electrónica', 'P300', 'Auriculares Bluetooth con micrófono', 'MarcaC', 'Auriculares BT', 29.99),
 (4, 'Papelería', 'P400', 'Libreta cosida 80 páginas', 'MarcaD', 'Libreta A5', 3.50),
 (5, 'Hogar', 'P500', 'Bolsa reutilizable 40x35cm', 'MarcaE', 'Bolsa Tela', 2.00);

-- -----------------------
-- UBICACIONES
-- columnas: id_ubicacion, codigo, descripcion, estanteria, exterior, mesa, nivel, pasillo
-- -----------------------
INSERT INTO ubicacion (id_ubicacion, codigo, descripcion, estanteria, exterior, mesa, nivel, pasillo) VALUES
 (1, 'A-01-01', 'Pasillo A estantería 1 nivel 1', 1, false, 'A', 1, 'A'),
 (2, 'A-01-02', 'Pasillo A estantería 1 nivel 2', 1, false, 'A', 2, 'A'),
 (3, 'B-02-01', 'Pasillo B estantería 2 nivel 1', 2, false, 'B', 1, 'B'),
 (4, 'B-02-02', 'Pasillo B estantería 2 nivel 2', 2, false, 'B', 2, 'B');

-- -----------------------
-- PERSONAS
-- columnas: id_persona, email, nombre
-- -----------------------
INSERT INTO personas (id_persona, email, nombre) VALUES
 (2, 'operario@example.com', 'Operario'),
 (3, 'soporte@example.com', 'Soporte');

-- -----------------------
-- CLIENTES
-- columnas: id_cliente, direccion_envio, email, fecha_nacimiento, fecha_registro, nif_nie, nombre, telefono
-- -----------------------
INSERT INTO clientes (id_cliente, direccion_envio, email, fecha_nacimiento, fecha_registro, nif_nie, nombre, telefono) VALUES
 (1, 'Calle Falsa 1', 'cliente1@example.com', '1990-01-01', '2025-01-01', '12345678A', 'Cliente Uno', '600111222'),
 (2, 'Avenida Siempre Viva 5', 'cliente2@example.com', '1985-05-05', '2025-02-10', '87654321B', 'Cliente Dos', '600333444'),
 (3, 'Plaza Mayor 3', 'cliente3@example.com', '1992-03-03', '2025-03-15', '11223344C', 'Cliente Tres', '600555666'),
 (4, 'Calle Luna 7', 'cliente4@example.com', '1980-07-07', '2025-04-20', '44332211D', 'Cliente Cuatro', '600777888');

-- -----------------------
-- CREDENCIALES
-- columnas: id, password, usuario, id_cliente, id_persona
-- -----------------------
-- NOTA: hashes ejemplo para dev; no usar en producción.
INSERT INTO credenciales (id, password, usuario, id_cliente, id_persona) VALUES
 (2, '$2a$10$C06Xc0cLuCywgWVQNiWtquYUvZ4kgIOIjUb8UUK0wcdv6VK2hjF7i', 'cliente1', 1, NULL),
 (3, '$2a$10$C06Xc0cLuCywgWVQNiWtquYUvZ4kgIOIjUb8UUK0wcdv6VK2hjF7i', 'operario', NULL, 2);

-- -----------------------
-- STOCK
-- columnas: id, cantidad, lote, ultima_actualizacion, unidad, producto_id, ubicacion_id
-- -----------------------
INSERT INTO stock (id, cantidad, lote, ultima_actualizacion, unidad, producto_id, ubicacion_id) VALUES
 (1, 10, 'L1', NOW(), 'uds', 1, 1),
 (2, 5, 'L2', NOW(), 'uds', 1, 2),
 (3, 20, 'L1', NOW(), 'uds', 2, 1),
 (4, 8, 'L3', NOW(), 'uds', 3, 3),
 (5, 2, 'L4', NOW(), 'uds', 3, 4),
 (6, 50, 'L1', NOW(), 'uds', 4, 2),
 (7, 100, 'L1', NOW(), 'uds', 5, 4);

-- -----------------------
-- PEDIDOS
-- columnas: id, estado, fecha_pedido, cliente_id
-- Valores enum: PENDIENTE=0, CONFIRMADO=1, EN_PREPARACION=2, PREPARADO=3, EN_TRANSITO=4, ENTREGADO=5, COMPLETADO=6, CANCELADO=7
-- -----------------------
INSERT INTO pedidos (id, estado, fecha_pedido, cliente_id) VALUES
 (1, 1, '2025-11-29', 1),
 (2, 1, '2025-11-28', 2),
 (3, 0, '2025-11-29', 3),
 (4, 0, '2025-11-29', 4);

-- -----------------------
-- PEDIDO_ITEMS
-- columnas: id, cantidad, creado_en, estado, lote, precio_unitario, unidad, pedido_id, producto_id
-- -----------------------
INSERT INTO pedido_items (id, cantidad, creado_en, estado, lote, precio_unitario, unidad, pedido_id, producto_id) VALUES
 (1, 2, '2025-11-29 10:00:00', 'CONFIRMADO', 'L1', 15.50, 'uds', 1, 1),
 (2, 1, '2025-11-29 10:00:00', 'CONFIRMADO', 'L2', 15.50, 'uds', 1, 1),
 (3, 1, '2025-11-29 10:00:00', 'CONFIRMADO', 'L3', 29.99, 'uds', 1, 3),
 (4, 3, '2025-11-28 12:00:00', 'RESERVADO', NULL, 4.99, 'uds', 2, 2),
 (5, 12, '2025-11-29 12:00:00', 'RESERVADO', NULL, 15.50, 'uds', 3, 1),
 (6, 9, '2025-11-29 12:30:00', 'RESERVADO', NULL, 29.99, 'uds', 4, 3);

-- -----------------------
-- HISTORIAL
-- columnas: id_historial, actualizado, nh, id_pedido
-- -----------------------
INSERT INTO historial (id_historial, actualizado, nh, id_pedido) VALUES
 (1, '2025-11-29', 'H-1', 1);

-- -----------------------
-- HISTORIAL_ENTRADAS
-- columnas: id, comentario, estado, fecha_hora, id_historial
-- -----------------------
INSERT INTO historial_entradas (id, comentario, estado, fecha_hora, id_historial) VALUES
 (1, 'Pedido creado y confirmado parcialmente', 'CONFIRMADO', '2025-11-29 10:00:00', 1),
 (2, 'Preparado para envío', 'PREPARADO', '2025-11-29 11:00:00', 1);

-- -----------------------
-- FAVORITOS (productos_clientes)
-- columnas: id_cliente, id_producto
-- -----------------------
INSERT INTO productos_clientes (id_cliente, id_producto) VALUES
 (1, 1),
 (1, 3),
 (3, 2);

-- FIN datos de prueba
