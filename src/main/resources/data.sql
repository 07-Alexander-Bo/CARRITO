-- Datos de prueba: Productos iniciales
-- CORRECCIÓN: H2 no soporta "INSERT ... SELECT ... WHERE NOT EXISTS" igual que PostgreSQL.
-- Se usa MERGE INTO (equivalente a INSERT OR IGNORE) compatible con H2.
MERGE INTO producto (id, nombre, precio, stock)
    KEY(id)
    VALUES (1, 'Laptop Dell 15"', 899.99, 10);

MERGE INTO producto (id, nombre, precio, stock)
    KEY(id)
    VALUES (2, 'Mouse Inalámbrico', 25.50, 50);

MERGE INTO producto (id, nombre, precio, stock)
    KEY(id)
    VALUES (3, 'Teclado Mecánico', 75.00, 30);

MERGE INTO producto (id, nombre, precio, stock)
    KEY(id)
    VALUES (4, 'Monitor 24" Full HD', 249.99, 15);

MERGE INTO producto (id, nombre, precio, stock)
    KEY(id)
    VALUES (5, 'Auriculares Bluetooth', 59.99, 25);
