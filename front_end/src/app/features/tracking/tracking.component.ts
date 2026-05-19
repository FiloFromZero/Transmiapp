import {
  Component, inject, signal, computed, OnInit, OnDestroy, PLATFORM_ID
} from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TransmiDataService } from '../../core/services/transmi-data.service';
import { RoleService } from '../../core/services/role.service';
import { Vehiculo, Ruta } from '../../core/models/transmi.models';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-tracking',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './tracking.component.html',
  styleUrl: './tracking.component.scss',
})
export class TrackingComponent implements OnInit, OnDestroy {
  private readonly svc        = inject(TransmiDataService);
  private readonly route      = inject(ActivatedRoute);
  private readonly platformId = inject(PLATFORM_ID);
  readonly roleSvc            = inject(RoleService);

  // ── Estado local ───────────────────────────────────────
  readonly rutaSeleccionadaId = signal<string | null>(null);
  readonly vehiculoSeleccionado = signal<Vehiculo | null>(null);
  readonly modoVista = signal<'mapa' | 'lista'>('mapa');
  readonly mapaCargado = signal<boolean>(false);

  // ── Signals del servicio ───────────────────────────────
  readonly rutas = this.svc.rutas;
  readonly vehiculos = this.svc.vehiculos;
  readonly incidentes = this.svc.incidentesActivos;

  // ── Computed ───────────────────────────────────────────
  readonly rutaActual = computed<Ruta | undefined>(() => {
    const id = this.rutaSeleccionadaId();
    return id ? this.svc.getRutaPorId(id) : undefined;
  });

  readonly vehiculosRutaActual = computed<Vehiculo[]>(() => {
    const id = this.rutaSeleccionadaId();
    return id ? this.svc.getVehiculosPorRuta(id) : this.vehiculos();
  });

  readonly vehiculosOrdenados = computed(() =>
    [...this.vehiculosRutaActual()].sort(
      (a, b) => (a.minutosParaProximaParada ?? 99) - (b.minutosParaProximaParada ?? 99)
    )
  );

  readonly paradaSeleccionadaId = signal<string | null>(null);

  readonly paradaActual = computed(() => {
    const id = this.paradaSeleccionadaId();
    const ruta = this.rutaActual();
    if (!id || !ruta) return undefined;
    return ruta.paradas.find(p => p.id === id);
  });

  readonly vehiculosEnParada = computed(() => {
    const paradaId = this.paradaSeleccionadaId();
    if (!paradaId) return [];
    return this.vehiculosRutaActual()
      .filter(v => v.proximaParadaId === paradaId)
      .sort((a, b) => (a.minutosParaProximaParada ?? 99) - (b.minutosParaProximaParada ?? 99));
  });

  // Simula posiciones de buses en el canvas del mapa
  readonly posicionesEnMapa = computed(() =>
    this.vehiculosRutaActual().slice(0, 8).map((v, idx) => ({
      vehiculo: v,
      x: 10 + (idx % 4) * 22,
      y: 20 + Math.floor(idx / 4) * 40,
    }))
  );

  private sub?: Subscription;

  ngOnInit(): void {
    this.sub = this.route.queryParams.subscribe(params => {
      if (params['ruta']) {
        this.rutaSeleccionadaId.set(params['ruta']);
      }
    });
    if (isPlatformBrowser(this.platformId)) {
      setTimeout(() => this.mapaCargado.set(true), 600);
    }
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  // ── Métodos ────────────────────────────────────────────
  seleccionarRuta(id: string): void {
    this.rutaSeleccionadaId.set(this.rutaSeleccionadaId() === id ? null : id);
    this.vehiculoSeleccionado.set(null);
    this.paradaSeleccionadaId.set(null);
  }

  seleccionarVehiculo(v: Vehiculo | null): void {
    this.vehiculoSeleccionado.set(v);
  }

  seleccionarParada(id: string | null): void {
    this.paradaSeleccionadaId.set(id);
  }

  getOcupacionPct(v: Vehiculo): number {
    return Math.round((v.ocupacionActual / v.capacidadTotal) * 100);
  }

  getColorOcupacion(pct: number): string {
    if (pct < 50) return '#4CAF50';
    if (pct < 80) return '#FF9800';
    return '#F44336';
  }

  getEtiquetaOcupacion(pct: number): string {
    if (pct < 50) return 'Libre';
    if (pct < 80) return 'Lleno';
    return 'Saturado';
  }

  getRumboLabel(rumbo: number): string {
    const dirs = ['N', 'NE', 'E', 'SE', 'S', 'SO', 'O', 'NO'];
    return dirs[Math.round(rumbo / 45) % 8];
  }

  setModo(m: 'mapa' | 'lista'): void {
    this.modoVista.set(m);
  }
}
