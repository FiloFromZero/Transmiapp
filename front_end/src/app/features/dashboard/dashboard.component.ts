import { Component, inject, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TransmiDataService } from '../../core/services/transmi-data.service';
import { RoleService } from '../../core/services/role.service';
import { ResumenRuta } from '../../core/models/transmi.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  private readonly svc = inject(TransmiDataService);
  readonly roleSvc = inject(RoleService);

  // ── Signals del servicio ───────────────────────────────
  readonly resumenes = this.svc.resumenesRutas;
  readonly incidentesActivos = this.svc.incidentesActivos;
  readonly vehiculos = this.svc.vehiculos;

  // ── Estado local ───────────────────────────────────────
  readonly filtroActivo = signal<string>('todas');
  readonly busqueda = signal<string>('');

  // ── Computed ───────────────────────────────────────────
  readonly resumenFiltrado = computed(() => {
    const filtro = this.filtroActivo();
    const texto = this.busqueda().toLowerCase();
    return this.resumenes().filter(r => {
      const coincideTipo = filtro === 'todas' || r.ruta.tipo === filtro;
      const coincideTexto = !texto ||
        r.ruta.nombre.toLowerCase().includes(texto) ||
        r.ruta.codigo.toLowerCase().includes(texto);
      return coincideTipo && coincideTexto;
    });
  });

  readonly totalVehiculos = computed(() => this.vehiculos().length);
  readonly totalIncidentes = computed(() => this.incidentesActivos().length);
  readonly rutasActivas = computed(() =>
    this.svc.rutas().filter(r => r.estado === 'activa').length
  );

  // ── Métodos ────────────────────────────────────────────
  setFiltro(tipo: string): void {
    this.filtroActivo.set(tipo);
  }

  onBusqueda(event: Event): void {
    this.busqueda.set((event.target as HTMLInputElement).value);
  }

  getColorEstado(estado: string): string {
    const mapa: Record<string, string> = {
      activa: '#4CAF50', demorada: '#FF9800',
      suspendida: '#F44336', fuera_de_servicio: '#9E9E9E',
    };
    return mapa[estado] ?? '#9E9E9E';
  }

  getLabelEstado(estado: string): string {
    const mapa: Record<string, string> = {
      activa: 'Activa', demorada: 'Demorada',
      suspendida: 'Suspendida', fuera_de_servicio: 'Fuera de servicio',
    };
    return mapa[estado] ?? estado;
  }

  getOcupacion(rutaId: string): number {
    const veh = this.svc.getVehiculosPorRuta(rutaId);
    if (!veh.length) return 0;
    const prom = veh.reduce((acc, v) => acc + (v.ocupacionActual / v.capacidadTotal), 0) / veh.length;
    return Math.round(prom * 100);
  }

  trackByRutaId(_: number, r: ResumenRuta): string {
    return r.ruta.id;
  }
}
