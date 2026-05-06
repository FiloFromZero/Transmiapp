import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TransmiDataService } from '../../core/services/transmi-data.service';
import {
  Incidente, NuevoIncidenteDTO, TipoIncidente,
  SeveridadIncidente, Coordenada
} from '../../core/models/transmi.models';

type FiltroIncidente = 'todos' | 'activos' | 'resueltos';

@Component({
  selector: 'app-novedades',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './novedades.component.html',
  styleUrl: './novedades.component.scss',
})
export class NovedadesComponent {
  private readonly svc = inject(TransmiDataService);
  private readonly fb  = inject(FormBuilder);

  // ── Estado UI ──────────────────────────────────────────
  readonly mostrarFormulario = signal<boolean>(false);
  readonly filtro = signal<FiltroIncidente>('todos');
  readonly enviando = signal<boolean>(false);
  readonly exitoEnvio = signal<boolean>(false);
  readonly incidenteExpandido = signal<string | null>(null);

  // ── Signals del servicio ───────────────────────────────
  readonly incidentes = this.svc.incidentes;
  readonly rutas      = this.svc.rutas;
  readonly cargando   = this.svc.cargando;

  // ── Computed ───────────────────────────────────────────
  readonly incidentesFiltrados = computed(() => {
    const f = this.filtro();
    const todos = this.incidentes();
    if (f === 'activos')   return todos.filter(i => i.activo);
    if (f === 'resueltos') return todos.filter(i => !i.activo);
    return todos;
  });

  readonly totalActivos   = computed(() => this.incidentes().filter(i => i.activo).length);
  readonly totalResueltos = computed(() => this.incidentes().filter(i => !i.activo).length);

  // ── Formulario reactivo ────────────────────────────────
  readonly form = this.fb.group({
    tipo:        ['congestion' as TipoIncidente, Validators.required],
    severidad:   ['media' as SeveridadIncidente, Validators.required],
    titulo:      ['', [Validators.required, Validators.minLength(5), Validators.maxLength(80)]],
    descripcion: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(300)]],
    rutasAfectadas: [[] as string[]],
  });

  // ── Catálogos para el form ─────────────────────────────
  readonly TIPOS: { value: TipoIncidente; label: string; icon: string }[] = [
    { value: 'accidente',        label: 'Accidente',          icon: '💥' },
    { value: 'congestion',       label: 'Congestión',         icon: '🚦' },
    { value: 'falla_mecanica',   label: 'Falla mecánica',     icon: '🔧' },
    { value: 'desorden_publico', label: 'Desorden público',   icon: '🚨' },
    { value: 'via_bloqueada',    label: 'Vía bloqueada',      icon: '🚧' },
    { value: 'otro',             label: 'Otro',               icon: '📋' },
  ];

  readonly SEVERIDADES: { value: SeveridadIncidente; label: string; color: string }[] = [
    { value: 'baja',    label: 'Baja',    color: '#4CAF50' },
    { value: 'media',   label: 'Media',   color: '#FF9800' },
    { value: 'alta',    label: 'Alta',    color: '#F44336' },
    { value: 'critica', label: 'Crítica', color: '#9C27B0' },
  ];

  // ── Métodos ────────────────────────────────────────────
  setFiltro(f: FiltroIncidente): void { this.filtro.set(f); }

  toggleFormulario(): void {
    this.mostrarFormulario.update(v => !v);
    this.exitoEnvio.set(false);
    this.form.reset({ tipo: 'congestion', severidad: 'media', rutasAfectadas: [] });
  }

  toggleExpand(id: string): void {
    this.incidenteExpandido.update(v => v === id ? null : id);
  }

  toggleRutaAfectada(id: string): void {
    const ctrl = this.form.controls.rutasAfectadas;
    const actual = ctrl.value ?? [];
    const nuevo = actual.includes(id) ? actual.filter(r => r !== id) : [...actual, id];
    ctrl.setValue(nuevo);
  }

  submitIncidente(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.enviando.set(true);

    const dto: NuevoIncidenteDTO = {
      tipo:          this.form.value.tipo!,
      severidad:     this.form.value.severidad!,
      titulo:        this.form.value.titulo!,
      descripcion:   this.form.value.descripcion!,
      rutasAfectadas: this.form.value.rutasAfectadas ?? [],
      coordenada:    { lat: 4.6290, lng: -74.0838 }, // Bogotá centro como default
    };

    this.svc.reportarIncidente(dto).subscribe({
      next: () => {
        this.enviando.set(false);
        this.exitoEnvio.set(true);
        setTimeout(() => {
          this.mostrarFormulario.set(false);
          this.exitoEnvio.set(false);
        }, 2500);
      },
      error: () => this.enviando.set(false),
    });
  }

  resolverIncidente(id: string, event: Event): void {
    event.stopPropagation();
    this.svc.resolverIncidente(id);
  }

  // ── Helpers de presentación ────────────────────────────
  getIconTipo(tipo: TipoIncidente): string {
    return this.TIPOS.find(t => t.value === tipo)?.icon ?? '📋';
  }

  getLabelTipo(tipo: TipoIncidente): string {
    return this.TIPOS.find(t => t.value === tipo)?.label ?? tipo;
  }

  getColorSeveridad(sev: SeveridadIncidente): string {
    return this.SEVERIDADES.find(s => s.value === sev)?.color ?? '#9E9E9E';
  }

  getLabelSeveridad(sev: SeveridadIncidente): string {
    return this.SEVERIDADES.find(s => s.value === sev)?.label ?? sev;
  }

  getRutaNombre(id: string): string {
    return this.rutas().find(r => r.id === id)?.codigo ?? id;
  }

  calcularTiempo(fecha: Date): string {
    const diff = Math.floor((Date.now() - new Date(fecha).getTime()) / 60000);
    if (diff < 1)  return 'Hace un momento';
    if (diff < 60) return `Hace ${diff} min`;
    return `Hace ${Math.floor(diff / 60)} h`;
  }

  trackById(_: number, inc: Incidente): string { return inc.id; }
}
