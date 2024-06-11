-- Crear tabla 'cliente'
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(255) NOT NULL,
    numero_telefono VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    uid VARCHAR(255) NOT NULL
);

-- Crear tabla 'usuario'
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

-- Crear tabla 'sombrilla'
CREATE TABLE IF NOT EXISTS sombrilla (
    id_sombrilla BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_sombrilla VARCHAR(50) NOT NULL,
    precio DOUBLE NOT NULL,
    ocupada BOOLEAN NOT NULL,
    reservada BOOLEAN NOT NULL,
    pagada BOOLEAN NOT NULL,
    cantidad_hamacas VARCHAR(50) NOT NULL
);

-- Crear tabla 'reserva'
CREATE TABLE IF NOT EXISTS reserva (
    id_reserva BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(50) NOT NULL,
    pagada BOOLEAN NOT NULL,
    metodo_pago VARCHAR(50),
    hora_llegada VARCHAR(50),
    fecha_reserva DATETIME,
    fecha_pago DATETIME,
    fecha_reserva_realizada DATETIME,
    id_cliente BIGINT,
    CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- Crear tabla 'pago'
CREATE TABLE IF NOT EXISTS pago (
    id_pago BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_pago DATETIME,
    cantidad DOUBLE NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    pagado BOOLEAN NOT NULL,
    detalles_pago VARCHAR(255),
    tipo_hamaca VARCHAR(50),
    id_reserva BIGINT,
    CONSTRAINT fk_reserva FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva)
);

-- Crear tabla 'reporte'
CREATE TABLE IF NOT EXISTS reporte (
    id_reporte BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    comentario_completo VARCHAR(200),
    fecha_creacion DATETIME
);
