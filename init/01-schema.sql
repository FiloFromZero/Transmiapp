-- ============================================================
-- TransmiApp - Script de inicialización de base de datos
-- ============================================================

-- ── Tabla de rutas ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS rutas (
    id            VARCHAR(20) PRIMARY KEY,
    codigo        VARCHAR(10)  NOT NULL UNIQUE,
    nombre        VARCHAR(100) NOT NULL,
    tipo          VARCHAR(20)  NOT NULL DEFAULT 'troncal',
    color         VARCHAR(7)   NOT NULL DEFAULT '#E84155',
    origen        VARCHAR(100) NOT NULL,
    destino       VARCHAR(100) NOT NULL,
    estado        VARCHAR(20)  NOT NULL DEFAULT 'activa',
    frecuencia_minutos INTEGER NOT NULL DEFAULT 10,
    vehiculo_tipo VARCHAR(20)  NOT NULL DEFAULT 'ARTICULADO',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── Tabla de paradas / estaciones ───────────────────────────
CREATE TABLE IF NOT EXISTS paradas (
    id          VARCHAR(20)  PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    latitud     DOUBLE PRECISION NOT NULL,
    longitud    DOUBLE PRECISION NOT NULL,
    es_troncal  BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── Tabla de relación ruta-parada (con orden) ───────────────
CREATE TABLE IF NOT EXISTS ruta_paradas (
    ruta_id   VARCHAR(20) REFERENCES rutas(id),
    parada_id VARCHAR(20) REFERENCES paradas(id),
    orden     INTEGER NOT NULL,
    PRIMARY KEY (ruta_id, parada_id)
);

-- ── Tabla de vehículos ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS vehiculos (
    id                VARCHAR(20) PRIMARY KEY,
    placa             VARCHAR(10)  NOT NULL UNIQUE,
    tipo              VARCHAR(20)  NOT NULL DEFAULT 'ARTICULADO',
    capacidad_total   INTEGER      NOT NULL DEFAULT 160,
    ruta_id           VARCHAR(20)  REFERENCES rutas(id),
    estado            VARCHAR(20)  NOT NULL DEFAULT 'en_ruta',
    latitud           DOUBLE PRECISION,
    longitud          DOUBLE PRECISION,
    rumbo             INTEGER DEFAULT 0,
    velocidad_kmh     INTEGER DEFAULT 0,
    ocupacion_actual  INTEGER DEFAULT 0,
    proxima_parada_id VARCHAR(20),
    minutos_proxima   DOUBLE PRECISION,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── Tabla de incidentes / novedades ─────────────────────────
CREATE TABLE IF NOT EXISTS incidentes (
    id               VARCHAR(30) PRIMARY KEY,
    tipo             VARCHAR(30)  NOT NULL,
    severidad        VARCHAR(10)  NOT NULL DEFAULT 'media',
    titulo           VARCHAR(200) NOT NULL,
    descripcion      TEXT,
    latitud          DOUBLE PRECISION NOT NULL,
    longitud         DOUBLE PRECISION NOT NULL,
    reportado_por    VARCHAR(20)  NOT NULL DEFAULT 'sistema',
    activo           BOOLEAN DEFAULT TRUE,
    fecha_reporte    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_resolucion TIMESTAMP
);

-- ── Tabla de relación incidente-ruta ────────────────────────
CREATE TABLE IF NOT EXISTS incidente_rutas (
    incidente_id VARCHAR(30) REFERENCES incidentes(id),
    ruta_id      VARCHAR(20) REFERENCES rutas(id),
    PRIMARY KEY (incidente_id, ruta_id)
);

-- ════════════════════════════════════════════════════════════
-- DATOS INICIALES
-- ════════════════════════════════════════════════════════════

-- ── Paradas ─────────────────────────────────────────────────
INSERT INTO paradas (id, nombre, latitud, longitud, es_troncal) VALUES
  ('p-tunal',     'Portal Tunal',     4.5766, -74.1351, TRUE),
  ('p-ricaurte',  'Ricaurte',         4.6080, -74.0935, TRUE),
  ('p-calle26',   'Calle 26',         4.6280, -74.0892, TRUE),
  ('p-100',       'Calle 100',        4.6887, -74.0524, TRUE),
  ('p-norte',     'Portal Norte',     4.7651, -74.0474, TRUE),
  ('p-americas',  'Portal Américas',  4.6286, -74.1638, TRUE),
  ('p-bosa',      'Bosa',             4.6213, -74.1540, FALSE),
  ('p-jimenez',   'Av. Jiménez',      4.5986, -74.0748, TRUE),
  ('p-sanvic',    'San Victorino',    4.5970, -74.0759, TRUE),
  ('p-heroes',    'Héroes',           4.6598, -74.0614, TRUE),
  ('p-suba',      'Portal Suba',      4.7434, -74.0939, TRUE),
  ('p-fontibon',  'Fontibón Centro',  4.6684, -74.1438, FALSE),
  ('p-dorado',    'Av. El Dorado',    4.6598, -74.1134, FALSE);

-- ── Rutas ───────────────────────────────────────────────────
INSERT INTO rutas (id, codigo, nombre, tipo, color, origen, destino, estado, frecuencia_minutos, vehiculo_tipo) VALUES
  ('r-b19', 'B19', 'Portal Tunal - Portal Norte',     'troncal',      '#E84155', 'Portal Tunal',     'Portal Norte',  'activa',   4,  'ARTICULADO'),
  ('r-j29', 'J29', 'Portal Américas - Av. Jiménez',   'troncal',      '#FF8C00', 'Portal Américas',  'Av. Jiménez',   'activa',   6,  'ARTICULADO'),
  ('r-c34', 'C34', 'San Victorino - Portal Suba',      'troncal',      '#2196F3', 'San Victorino',    'Portal Suba',   'demorada', 8,  'BIARTICULADO'),
  ('r-k12', 'K12', 'SITP Fontibón',                    'sitp',         '#4CAF50', 'Fontibón Centro',  'Av. El Dorado', 'activa',   12, 'ALIMENTADOR');

-- ── Relaciones ruta-parada ──────────────────────────────────
INSERT INTO ruta_paradas (ruta_id, parada_id, orden) VALUES
  ('r-b19', 'p-tunal',    1), ('r-b19', 'p-ricaurte', 2), ('r-b19', 'p-calle26', 3),
  ('r-b19', 'p-100',      4), ('r-b19', 'p-norte',    5),
  ('r-j29', 'p-americas', 1), ('r-j29', 'p-bosa',     2), ('r-j29', 'p-jimenez', 3),
  ('r-c34', 'p-sanvic',   1), ('r-c34', 'p-heroes',   2), ('r-c34', 'p-suba',    3),
  ('r-k12', 'p-fontibon', 1), ('r-k12', 'p-dorado',   2);

-- ── Vehículos ───────────────────────────────────────────────
INSERT INTO vehiculos (id, placa, tipo, capacidad_total, ruta_id, estado, latitud, longitud, rumbo, velocidad_kmh, ocupacion_actual, proxima_parada_id, minutos_proxima) VALUES
  ('v-b19-0', 'TM1001', 'ARTICULADO',   160, 'r-b19', 'en_ruta', 4.5800, -74.1320, 45,  25, 120, 'p-ricaurte', 3),
  ('v-b19-1', 'TM1002', 'ARTICULADO',   160, 'r-b19', 'en_ruta', 4.6100, -74.0910, 10,  30, 85,  'p-calle26',  5),
  ('v-b19-2', 'TM1003', 'ARTICULADO',   160, 'r-b19', 'en_ruta', 4.6500, -74.0700, 20,  28, 95,  'p-100',      4),
  ('v-b19-3', 'TM1004', 'ARTICULADO',   160, 'r-b19', 'en_ruta', 4.7000, -74.0500, 15,  22, 70,  'p-norte',    6),
  ('v-b19-4', 'TM1005', 'ARTICULADO',   160, 'r-b19', 'en_ruta', 4.7400, -74.0480, 5,   18, 40,  'p-norte',    2),
  ('v-j29-0', 'TM2001', 'ARTICULADO',   160, 'r-j29', 'en_ruta', 4.6290, -74.1600, 90,  20, 100, 'p-bosa',     4),
  ('v-j29-1', 'TM2002', 'ARTICULADO',   160, 'r-j29', 'en_ruta', 4.6200, -74.1500, 120, 15, 60,  'p-jimenez',  8),
  ('v-j29-2', 'TM2003', 'ARTICULADO',   160, 'r-j29', 'en_ruta', 4.6050, -74.0800, 180, 35, 130, 'p-jimenez',  2),
  ('v-c34-0', 'TM3001', 'BIARTICULADO', 250, 'r-c34', 'en_ruta', 4.6000, -74.0750, 0,   20, 180, 'p-heroes',   5),
  ('v-c34-1', 'TM3002', 'BIARTICULADO', 250, 'r-c34', 'en_ruta', 4.6700, -74.0650, 340, 18, 150, 'p-suba',     7),
  ('v-k12-0', 'TM4001', 'ALIMENTADOR',  40,  'r-k12', 'en_ruta', 4.6690, -74.1400, 90,  25, 30,  'p-dorado',   6),
  ('v-k12-1', 'TM4002', 'ALIMENTADOR',  40,  'r-k12', 'en_ruta', 4.6650, -74.1200, 270, 20, 25,  'p-fontibon', 4);

-- ── Incidentes ──────────────────────────────────────────────
INSERT INTO incidentes (id, tipo, severidad, titulo, descripcion, latitud, longitud, reportado_por, activo) VALUES
  ('inc-001', 'congestion',     'alta',  'Congestión en Calle 26',      'Trancón severo por obras en la intersección con Caracas.',   4.6280, -74.0892, 'sistema',   TRUE),
  ('inc-002', 'falla_mecanica', 'media', 'Bus varado en Portal Suba',   'Articulado con falla de motor bloqueando un carril.',        4.7434, -74.0939, 'conductor', TRUE);

INSERT INTO incidente_rutas (incidente_id, ruta_id) VALUES
  ('inc-001', 'r-b19'), ('inc-001', 'r-j29'), ('inc-002', 'r-c34');
