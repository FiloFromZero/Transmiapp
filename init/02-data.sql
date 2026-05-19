-- ============================================================
-- TransmiApp - Script de Carga de Datos de Prueba (Seed Data)
-- ============================================================

-- ── 1. Nuevas Paradas / Estaciones en Bogotá ─────────────────
INSERT INTO paradas (id, nombre, latitud, longitud, es_troncal) VALUES
  ('p-p80',         'Portal 80',         4.7094, -74.0998, TRUE),
  ('p-aguas',        'Las Aguas',         4.6015, -74.0685, TRUE),
  ('p-psur',         'Portal Sur',        4.5912, -74.1610, TRUE),
  ('p-marly',        'Marly',             4.6462, -74.0638, TRUE),
  ('p-calle72',      'Calle 72',          4.6612, -74.0592, TRUE),
  ('p-calle45',      'Calle 45',          4.6330, -74.0669, TRUE),
  ('p-museo',        'Museo del Oro',     4.6009, -74.0722, TRUE),
  ('p-can',          'CAN',               4.6385, -74.1068, TRUE),
  ('p-peldorado',    'Portal Eldorado',   4.6732, -74.1264, TRUE),
  ('p-aeropuerto',   'Aeropuerto Eldorado',4.6975, -74.1415, FALSE)
ON CONFLICT (id) DO NOTHING;

-- ── 2. Nuevas Rutas de TransMilenio y SITP ──────────────────
INSERT INTO rutas (id, codigo, nombre, tipo, color, origen, destino, estado, frecuencia_minutos, vehiculo_tipo) VALUES
  ('r-h75', 'H75', 'Portal Norte - Portal Tunal (Expreso)', 'troncal', '#E84155', 'Portal Norte', 'Portal Tunal', 'activa', 5, 'BIARTICULADO'),
  ('r-d21', 'D21', 'Portal Tunal - Portal 80 (Corredor)',    'troncal', '#4CAF50', 'Portal Tunal', 'Portal 80',    'activa', 7, 'ARTICULADO'),
  ('r-f23', 'F23', 'Las Aguas - Portal Américas',         'troncal', '#FF9800', 'Las Aguas',    'Portal Américas','demorada', 6, 'ARTICULADO'),
  ('r-m86', 'M86', 'Portal Eldorado - Pedregal (Dual)',     'dual',    '#795548', 'Portal Eldorado','Pedregal',    'activa', 10, 'PADRON')
ON CONFLICT (id) DO NOTHING;

-- ── 3. Asociación Ruta - Paradas (Secuencia del recorrido) ───
INSERT INTO ruta_paradas (ruta_id, parada_id, orden) VALUES
  -- H75: Portal Norte -> Héroes -> Calle 72 -> Marly -> Calle 26 -> Ricaurte -> Portal Tunal
  ('r-h75', 'p-norte', 1),
  ('r-h75', 'p-heroes', 2),
  ('r-h75', 'p-calle72', 3),
  ('r-h75', 'p-marly', 4),
  ('r-h75', 'p-calle26', 5),
  ('r-h75', 'p-ricaurte', 6),
  ('r-h75', 'p-tunal', 7),

  -- D21: Portal Tunal -> Ricaurte -> Calle 26 -> Héroes -> Portal 80
  ('r-d21', 'p-tunal', 1),
  ('r-d21', 'p-ricaurte', 2),
  ('r-d21', 'p-calle26', 3),
  ('r-d21', 'p-heroes', 4),
  ('r-d21', 'p-p80', 5),

  -- F23: Las Aguas -> Museo del Oro -> Av. Jiménez -> Ricaurte -> Portal Américas
  ('r-f23', 'p-aguas', 1),
  ('r-f23', 'p-museo', 2),
  ('r-f23', 'p-jimenez', 3),
  ('r-f23', 'p-ricaurte', 4),
  ('r-f23', 'p-americas', 5),

  -- M86: Portal Eldorado -> CAN -> Av. El Dorado -> Calle 26 -> Calle 72 -> Aeropuerto
  ('r-m86', 'p-peldorado', 1),
  ('r-m86', 'p-can', 2),
  ('r-m86', 'p-dorado', 3),
  ('r-m86', 'p-calle26', 4),
  ('r-m86', 'p-calle72', 5),
  ('r-m86', 'p-aeropuerto', 6)
ON CONFLICT (ruta_id, parada_id) DO NOTHING;

-- ── 4. Gran Lote de Vehículos con Datos en Tiempo Real ────────
INSERT INTO vehiculos (id, placa, tipo, capacidad_total, ruta_id, estado, latitud, longitud, rumbo, velocidad_kmh, ocupacion_actual, proxima_parada_id, minutos_proxima) VALUES
  -- H75 (Biarticulados)
  ('v-h75-0', 'TM1011', 'BIARTICULADO', 250, 'r-h75', 'en_ruta', 4.7512, -74.0498, 180, 42, 190, 'p-heroes', 6.0),
  ('v-h75-1', 'TM1012', 'BIARTICULADO', 250, 'r-h75', 'en_ruta', 4.6550, -74.0620, 180, 35, 230, 'p-calle72', 2.0),
  ('v-h75-2', 'TM1013', 'BIARTICULADO', 250, 'r-h75', 'en_ruta', 4.6402, -74.0650, 190, 20, 180, 'p-marly', 3.5),
  ('v-h75-3', 'TM1014', 'BIARTICULADO', 250, 'r-h75', 'en_ruta', 4.6180, -74.0910, 200, 38, 140, 'p-ricaurte', 4.0),
  ('v-h75-4', 'TM1015', 'BIARTICULADO', 250, 'r-h75', 'detenido', 4.5772, -74.1348, 0,    0,  15,  'p-tunal', 1.0),

  -- D21 (Articulados)
  ('v-d21-0', 'TM1021', 'ARTICULADO',   160, 'r-d21', 'en_ruta', 4.5820, -74.1310, 45,  30, 110, 'p-ricaurte', 5.0),
  ('v-d21-1', 'TM1022', 'ARTICULADO',   160, 'r-d21', 'en_ruta', 4.6220, -74.0905, 15,  28, 140, 'p-calle26', 3.0),
  ('v-d21-2', 'TM1023', 'ARTICULADO',   160, 'r-d21', 'en_ruta', 4.6850, -74.0560, 340, 45, 95,  'p-p80', 7.5),

  -- F23 (Articulados)
  ('v-f23-0', 'TM1031', 'ARTICULADO',   160, 'r-f23', 'en_ruta', 4.6012, -74.0690, 270, 15, 150, 'p-museo', 2.0),
  ('v-f23-1', 'TM1032', 'ARTICULADO',   160, 'r-f23', 'en_ruta', 4.6030, -74.0780, 260, 22, 135, 'p-ricaurte', 4.0),
  ('v-f23-2', 'TM1033', 'ARTICULADO',   160, 'r-f23', 'en_ruta', 4.6150, -74.1200, 250, 40, 85,  'p-americas', 6.0),

  -- M86 (Dual / Padrón)
  ('v-m86-0', 'TM1041', 'PADRON',       90,  'r-m86', 'en_ruta', 4.6710, -74.1240, 90,  32, 60,  'p-can', 3.0),
  ('v-m86-1', 'TM1042', 'PADRON',       90,  'r-m86', 'en_ruta', 4.6420, -74.1010, 120, 25, 75,  'p-calle26', 4.5),
  ('v-m86-2', 'TM1043', 'PADRON',       90,  'r-m86', 'en_ruta', 4.6880, -74.1380, 270, 30, 40,  'p-aeropuerto', 5.0),

  -- Más buses para las rutas originales para hacerlas más densas
  -- B19
  ('v-b19-5', 'TM1006', 'ARTICULADO',   160, 'r-b19', 'en_ruta', 4.6320, -74.0850, 45,  35, 115, 'p-100', 9.0),
  ('v-b19-6', 'TM1007', 'ARTICULADO',   160, 'r-b19', 'en_ruta', 4.6710, -74.0550, 30,  40, 130, 'p-100', 3.0),
  -- J29
  ('v-j29-3', 'TM2004', 'ARTICULADO',   160, 'r-j29', 'en_ruta', 4.6120, -74.1050, 90,  26, 90,  'p-jimenez', 5.0),
  -- C34
  ('v-c34-2', 'TM3003', 'BIARTICULADO', 250, 'r-c34', 'en_ruta', 4.6210, -74.0720, 10,  24, 205, 'p-heroes', 8.0)
ON CONFLICT (id) DO NOTHING;

-- ── 5. Nuevos Incidentes / Reportes Viales Reales en Bogotá ──
INSERT INTO incidentes (id, tipo, severidad, titulo, descripcion, latitud, longitud, reportado_por, activo) VALUES
  ('inc-003', 'accidente', 'alta', 'Choque Simple en Autopista Norte con Calle 80', 'Colisión entre dos vehículos particulares bloquea la calzada exclusiva sentido norte-sur, afectando la entrada al intercambiador.', 4.6675, -74.0608, 'sistema', TRUE),
  ('inc-004', 'manifestacion', 'alta', 'Manifestación en la Av. Caracas con Calle 45', 'Protesta estudiantil bloquea ambos sentidos de la troncal Caracas a la altura de la Universidad Distrital. Transbordo activo.', 4.6330, -74.0669, 'usuario', TRUE),
  ('inc-005', 'clima', 'baja', 'Lluvia Fuerte en el Sector Norte', 'Lluvias torrenciales reducen la velocidad de operación en la Autopista Norte entre la Calle 100 y Portal Norte. Se recomienda precaución.', 4.7210, -74.0485, 'sistema', TRUE),
  ('inc-006', 'obras', 'media', 'Obras de Mantenimiento en Av. Eldorado', 'Cierre de carril exclusivo por reparación de losas de concreto a la altura de la estación CAN. Tránsito lento en sentido oriente-occidente.', 4.6385, -74.1068, 'conductor', TRUE)
ON CONFLICT (id) DO NOTHING;

-- Relación de Incidentes con Rutas Afectadas
INSERT INTO incidente_rutas (incidente_id, ruta_id) VALUES
  -- Accidente en Autonorte afecta a H75, C34, B19
  ('inc-003', 'r-h75'),
  ('inc-003', 'r-c34'),
  ('inc-003', 'r-b19'),
  -- Manifestación en Calle 45 afecta a H75
  ('inc-004', 'r-h75'),
  -- Clima en el Norte afecta a B19, H75
  ('inc-005', 'r-b19'),
  ('inc-005', 'r-h75'),
  -- Obras en el Dorado afectan a M86
  ('inc-006', 'r-m86')
ON CONFLICT (incidente_id, ruta_id) DO NOTHING;
