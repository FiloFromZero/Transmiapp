import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TransmiPatternsService } from '../../core/services/transmi-patterns.service';
import { RoleService } from '../../core/services/role.service';
import { TransmiDataService } from '../../core/services/transmi-data.service';

@Component({
  selector: 'app-admin-control',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-control.component.html',
  styleUrl: './admin-control.component.scss',
})
export class AdminControlComponent {
  private readonly patternsSvc = inject(TransmiPatternsService);
  private readonly dataSvc = inject(TransmiDataService);
  readonly roleSvc = inject(RoleService);

  readonly rutasDisponibles = this.dataSvc.rutas;

  // Command Pattern State
  vehicleTypeCommand = signal('ARTICULADO');
  routeCodeCommand = signal('H75');
  commandHistory = signal<string[]>([]);
  lastCommandResult = signal<any>(null);

  // Template Method Pattern State
  reportContent = signal<string | null>(null);

  // Factory Pattern State
  factoryVehicleType = signal('BIARTICULADO');
  lastFactoryResult = signal<any>(null);

  // ── Command Pattern Actions ──────────────────────────────
  asignarVehiculo() {
    this.patternsSvc.assignVehicle(this.vehicleTypeCommand(), this.routeCodeCommand())
      .subscribe(res => {
        this.lastCommandResult.set(res.vehicle);
        this.commandHistory.set(res.history);
      });
  }

  deshacerAccion() {
    this.patternsSvc.undoLastCommand()
      .subscribe(res => {
        this.lastCommandResult.set(null);
        this.commandHistory.set(res.remainingHistory);
        alert('Última acción deshecha correctamente (Command Pattern).');
      });
  }

  // ── Template Method Pattern Actions ──────────────────────
  generarReporte(tipo: 'pasajeros' | 'flota') {
    this.patternsSvc.generateReport(tipo).subscribe(res => {
      this.reportContent.set(res.report);
    });
  }

  // ── Factory Pattern Actions ──────────────────────────────
  crearVehiculoFactory() {
    this.patternsSvc.createVehicle(this.factoryVehicleType()).subscribe(res => {
      this.lastFactoryResult.set(res.vehicle);
    });
  }

  // ── Singleton Pattern State & Actions ────────────────────
  singletonConfig = signal<any>(null);

  cargarConfig() {
    this.patternsSvc.getConfig().subscribe(res => {
      this.singletonConfig.set(res);
    });
  }

  // ── Builder Pattern State & Actions ──────────────────────
  builderCode = signal('B99');
  builderName = signal('Ruta Bogotá Centro');
  builderOrigin = signal('Portal Norte');
  builderDestination = signal('Portal Sur');
  builderExpress = signal(false);
  builderResult = signal<any>(null);

  construirRuta() {
    this.patternsSvc.buildRoute(
      this.builderCode(), this.builderName(),
      this.builderOrigin(), this.builderDestination(),
      this.builderExpress()
    ).subscribe(res => {
      this.builderResult.set(res);
    });
  }

  // ── Observer Pattern State & Actions ─────────────────────
  observerStation = signal('');
  observerRoute = signal('');
  observerEventLog = signal<string[]>([]);

  notificarLlegada() {
    const station = this.observerStation();
    const route = this.observerRoute();
    if (!station || !route) return;
    this.patternsSvc.notifyArrival(station, route).subscribe(res => {
      const entry = `[${new Date().toLocaleTimeString()}] Bus de ${res.route} llegó a ${res.station}`;
      this.observerEventLog.update(log => [entry, ...log.slice(0, 9)]);
    });
  }
}
