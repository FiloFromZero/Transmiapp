// ============================================================
// TRANSMILENIO APP - Interfaces de dominio de negocio
// ============================================================

export type EstadoRuta = 'activa' | 'suspendida' | 'demorada' | 'fuera_de_servicio';
export type EstadoVehiculo = 'en_ruta' | 'en_paradero' | 'fuera_de_servicio' | 'mantenimiento';
export type SeveridadIncidente = 'baja' | 'media' | 'alta' | 'critica';
export type TipoIncidente =
  | 'accidente'
  | 'congestion'
  | 'falla_mecanica'
  | 'desorden_publico'
  | 'via_bloqueada'
  | 'otro';

/** Coordenadas GPS */
export interface Coordenada {
  lat: number;
  lng: number;
}

/** Parada / estación dentro de una ruta */
export interface Parada {
  id: string;
  nombre: string;
  coordenada: Coordenada;
  esTroncal: boolean;
  tiempoEstimadoLlegada?: number; // minutos
}

/** Ruta de Transmilenio / SITP */
export interface Ruta {
  id: string;
  codigo: string;          // ej. "B19", "J29"
  nombre: string;
  tipo: 'troncal' | 'alimentadora' | 'sitp' | 'cable';
  color: string;           // color hex para UI
  origen: string;
  destino: string;
  paradas: Parada[];
  estado: EstadoRuta;
  frecuenciaMinutos: number;
  ultimaActualizacion: Date;
}

/** Vehículo / Bus en operación */
export interface Vehiculo {
  id: string;
  placa: string;
  rutaId: string;
  coordenada: Coordenada;
  rumbo: number;            // grados 0-360
  velocidadKmh: number;
  capacidadTotal: number;
  ocupacionActual: number;
  estado: EstadoVehiculo;
  paradaActualId?: string;
  proximaParadaId?: string;
  minutosParaProximaParada?: number;
  ultimaActualizacion: Date;
}

/** Incidente / Novedad en el sistema */
export interface Incidente {
  id: string;
  tipo: TipoIncidente;
  severidad: SeveridadIncidente;
  titulo: string;
  descripcion: string;
  coordenada: Coordenada;
  rutasAfectadas: string[]; // IDs de rutas
  reportadoPor: 'sistema' | 'conductor' | 'usuario';
  fechaReporte: Date;
  fechaResolucion?: Date;
  activo: boolean;
}

/** DTO para crear un nuevo incidente */
export interface NuevoIncidenteDTO {
  tipo: TipoIncidente;
  severidad: SeveridadIncidente;
  titulo: string;
  descripcion: string;
  coordenada: Coordenada;
  rutasAfectadas: string[];
}

/** Resumen de una ruta para el dashboard */
export interface ResumenRuta {
  ruta: Ruta;
  vehiculosActivos: number;
  incidentesActivos: number;
  tiempoProximoBus?: number; // minutos
}

/** Tiempo de llegada calculado */
export interface TiempoLlegada {
  vehiculoId: string;
  paradaId: string;
  minutosEstimados: number;
  confiabilidad: 'alta' | 'media' | 'baja';
}
