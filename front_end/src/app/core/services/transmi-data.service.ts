import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, interval, of, Subject, catchError, tap } from 'rxjs';
import { map, startWith, switchMap } from 'rxjs/operators';
import {
  Ruta, Vehiculo, Incidente, ResumenRuta, NuevoIncidenteDTO,
  TiempoLlegada, EstadoRuta, Coordenada
} from '../models/transmi.models';

// ── Configuración de la API ────────────────────────────────
const API_BASE = '/api/data';

// ── Servicio principal ──────────────────────────────────────
@Injectable({ providedIn: 'root' })
export class TransmiDataService {
  // ── Signals de estado global ──────────────────────────
  private readonly _rutas = signal<Ruta[]>([]);
  private readonly _vehiculos = signal<Vehiculo[]>([]);
  private readonly _incidentes = signal<Incidente[]>([]);
  private readonly _cargando = signal<boolean>(false);
  private _initialized = false;

  // ── Signals públicas (solo lectura) ───────────────────
  readonly rutas = this._rutas.asReadonly();
  readonly vehiculos = this._vehiculos.asReadonly();
  readonly incidentes = this._incidentes.asReadonly();
  readonly cargando = this._cargando.asReadonly();

  // ── Signals computadas ────────────────────────────────
  readonly incidentesActivos = computed(() =>
    this._incidentes().filter(i => i.activo)
  );

  readonly rutasConIncidentes = computed(() => {
    const idsAfectadas = new Set(
      this.incidentesActivos().flatMap(i => i.rutasAfectadas)
    );
    return this._rutas().filter(r => idsAfectadas.has(r.id));
  });

  readonly resumenesRutas = computed<ResumenRuta[]>(() =>
    this._rutas().map(ruta => ({
      ruta,
      vehiculosActivos: this._vehiculos().filter(v => v.rutaId === ruta.id && v.estado === 'en_ruta').length,
      incidentesActivos: this.incidentesActivos().filter(i => i.rutasAfectadas.includes(ruta.id)).length,
      tiempoProximoBus: this.calcularTiempoProximoBus(ruta.id),
    }))
  );

  // ── Stream de notificaciones de nuevos incidentes ─────
  private readonly _nuevaNovedadSubject = new Subject<Incidente>();
  readonly nuevaNovedadStream$ = this._nuevaNovedadSubject.asObservable();

  constructor(private http: HttpClient) {
    this.cargarDatosIniciales();

    // Actualizar datos cada 15 segundos desde el backend
    interval(15000).pipe(
      startWith(0),
      tap(() => {
        if (this._initialized) {
          this.refrescarVehiculos();
        }
      })
    ).subscribe();
  }

  // ── Carga inicial desde el backend ────────────────────

  private cargarDatosIniciales(): void {
    this._cargando.set(true);

    this.http.get<any[]>(`${API_BASE}/rutas`).pipe(
      catchError(err => {
        console.warn('Backend no disponible, usando datos vacíos:', err.message);
        return of([]);
      })
    ).subscribe(rutas => {
      const mapped: Ruta[] = rutas.map(r => ({
        id: r.id,
        codigo: r.codigo,
        nombre: r.nombre,
        tipo: r.tipo,
        color: r.color,
        origen: r.origen,
        destino: r.destino,
        estado: r.estado as EstadoRuta,
        frecuenciaMinutos: r.frecuenciaMinutos,
        ultimaActualizacion: new Date(r.ultimaActualizacion),
        paradas: (r.paradas || []).map((p: any) => ({
          id: p.id,
          nombre: p.nombre,
          coordenada: p.coordenada,
          esTroncal: p.esTroncal,
        })),
      }));
      this._rutas.set(mapped);
    });

    this.http.get<any[]>(`${API_BASE}/vehiculos`).pipe(
      catchError(() => of([]))
    ).subscribe(vehiculos => {
      this._vehiculos.set(vehiculos.map(v => this.mapVehiculoApi(v)));
    });

    this.http.get<any[]>(`${API_BASE}/incidentes`).pipe(
      catchError(() => of([]))
    ).subscribe(incidentes => {
      this._incidentes.set(incidentes.map(i => this.mapIncidenteApi(i)));
      this._cargando.set(false);
      this._initialized = true;
    });
  }

  private refrescarVehiculos(): void {
    this.http.get<any[]>(`${API_BASE}/vehiculos`).pipe(
      catchError(() => of([]))
    ).subscribe(vehiculos => {
      this._vehiculos.set(vehiculos.map(v => this.mapVehiculoApi(v)));
    });
  }

  // ── Mappers de API a modelo ───────────────────────────

  private mapVehiculoApi(v: any): Vehiculo {
    return {
      id: v.id,
      placa: v.placa,
      rutaId: v.rutaId,
      coordenada: v.coordenada,
      rumbo: v.rumbo ?? 0,
      velocidadKmh: v.velocidadKmh ?? 0,
      capacidadTotal: v.capacidadTotal ?? 160,
      ocupacionActual: v.ocupacionActual ?? 0,
      estado: v.estado ?? 'en_ruta',
      proximaParadaId: v.proximaParadaId,
      minutosParaProximaParada: v.minutosParaProximaParada,
      ultimaActualizacion: new Date(v.ultimaActualizacion ?? Date.now()),
    };
  }

  private mapIncidenteApi(i: any): Incidente {
    return {
      id: i.id,
      tipo: i.tipo,
      severidad: i.severidad,
      titulo: i.titulo,
      descripcion: i.descripcion,
      coordenada: i.coordenada,
      rutasAfectadas: i.rutasAfectadas ?? [],
      reportadoPor: i.reportadoPor,
      fechaReporte: new Date(i.fechaReporte),
      fechaResolucion: i.fechaResolucion ? new Date(i.fechaResolucion) : undefined,
      activo: i.activo,
    };
  }

  // ── Métodos públicos ──────────────────────────────────

  /** Obtiene una ruta por su ID */
  getRutaPorId(id: string): Ruta | undefined {
    return this._rutas().find(r => r.id === id);
  }

  /** Obtiene vehículos de una ruta específica */
  getVehiculosPorRuta(rutaId: string): Vehiculo[] {
    return this._vehiculos().filter(v => v.rutaId === rutaId);
  }

  /** Calcula tiempos de llegada para una parada */
  getTiemposLlegada(paradaId: string): Observable<TiempoLlegada[]> {
    return of(null).pipe(
      map(() => {
        const vehiculosCercanos = this._vehiculos()
          .filter(v => v.proximaParadaId === paradaId || v.minutosParaProximaParada !== undefined)
          .slice(0, 3);

        return vehiculosCercanos.map((v, idx) => ({
          vehiculoId: v.id,
          paradaId,
          minutosEstimados: (v.minutosParaProximaParada ?? 0) + idx * 4,
          confiabilidad: idx === 0 ? 'alta' : idx === 1 ? 'media' : 'baja',
        } as TiempoLlegada));
      })
    );
  }

  /** Reporta un nuevo incidente en el sistema (via backend) */
  reportarIncidente(dto: NuevoIncidenteDTO): Observable<Incidente> {
    this._cargando.set(true);
    return this.http.post<any>(`${API_BASE}/incidentes`, {
      tipo: dto.tipo,
      severidad: dto.severidad,
      titulo: dto.titulo,
      descripcion: dto.descripcion,
      latitud: dto.coordenada.lat,
      longitud: dto.coordenada.lng,
    }).pipe(
      map(resp => {
        const nuevo: Incidente = this.mapIncidenteApi({
          ...resp,
          coordenada: { lat: resp.latitud, lng: resp.longitud },
          rutasAfectadas: dto.rutasAfectadas,
        });
        this._incidentes.update(list => [nuevo, ...list]);
        this._nuevaNovedadSubject.next(nuevo);
        this._cargando.set(false);
        return nuevo;
      }),
      catchError(err => {
        console.error('Error reportando incidente:', err);
        this._cargando.set(false);
        // Fallback: crear localmente
        const nuevo: Incidente = {
          id: `inc-${Date.now()}`,
          ...dto,
          reportadoPor: 'usuario',
          fechaReporte: new Date(),
          activo: true,
        };
        this._incidentes.update(list => [nuevo, ...list]);
        this._nuevaNovedadSubject.next(nuevo);
        return of(nuevo);
      })
    );
  }

  /** Resuelve/cierra un incidente */
  resolverIncidente(id: string): void {
    this.http.patch(`${API_BASE}/incidentes/${id}/resolver`, {}).pipe(
      catchError(() => of(null))
    ).subscribe(() => {
      this._incidentes.update(list =>
        list.map(i => i.id === id ? { ...i, activo: false, fechaResolucion: new Date() } : i)
      );
    });
  }

  // ── Métodos privados ──────────────────────────────────

  private calcularTiempoProximoBus(rutaId: string): number {
    const vehiculos = this.getVehiculosPorRuta(rutaId);
    if (!vehiculos.length) return 999;
    return Math.min(...vehiculos.map(v => v.minutosParaProximaParada ?? 999));
  }
}
