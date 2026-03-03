INSERT IGNORE INTO rol (id, nombre, descripcion) VALUES (1, 'ADMIN', 'Admin con poderes de crear perfiles a propietarios.');
INSERT IGNORE INTO rol (id, nombre, descripcion) VALUES (2, 'PROPIETARIO', 'Dueno de la cuenta o establecimiento.');
INSERT IGNORE INTO rol (id, nombre, descripcion) VALUES (3, 'EMPLEADO', 'Personal con acceso limitado.');
INSERT IGNORE INTO rol (id, nombre, descripcion) VALUES (4, 'CLIENTE', 'Usuario final del servicio.');

CREATE TABLE IF NOT EXISTS empleado_restaurante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL UNIQUE,
    id_restaurante BIGINT NOT NULL,
    CONSTRAINT fk_empleado_restaurante_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'usuarios'
      AND column_name = 'id_restaurante'
);
SET @drop_sql := IF(@col_exists > 0,
    'ALTER TABLE usuarios DROP COLUMN id_restaurante',
    'SELECT 1');
PREPARE stmt FROM @drop_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
