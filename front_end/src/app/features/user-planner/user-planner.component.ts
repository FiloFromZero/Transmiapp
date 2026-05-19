import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TransmiPatternsService } from '../../core/services/transmi-patterns.service';

@Component({
  selector: 'app-user-planner',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './user-planner.component.html',
  styleUrl: './user-planner.component.scss',
})
export class UserPlannerComponent {
  private readonly patternsSvc = inject(TransmiPatternsService);

  // Form State
  origen = signal('Portal Norte');
  destino = signal('Portal 80');
  userType = signal('NORMAL');

  // UI State
  loading = signal(false);
  tripResult = signal<any>(null);

  planearViaje() {
    this.loading.set(true);
    this.patternsSvc.planTrip(this.origen(), this.destino(), this.userType())
      .subscribe({
        next: (res) => {
          this.tripResult.set(res);
          this.loading.set(false);
        },
        error: (err) => {
          console.error(err);
          this.loading.set(false);
        }
      });
  }

  // To display the Strategy Pattern fare separately if needed
  verificarTarifa() {
    this.patternsSvc.getFare(this.userType()).subscribe(res => {
      alert(`Tarifa con estrategia ${res.strategy}: ${res.calculatedFare} ${res.currency}`);
    });
  }

  // ── Decorator Pattern ─────────────────────────────────────
  notifUserId = signal('usuario-123');
  notifMessage = signal('Tu bus llega en 3 minutos.');
  notifSms = signal(false);
  notifEmail = signal(false);
  notifResult = signal<any>(null);

  enviarNotificacion() {
    this.patternsSvc.sendNotification(
      this.notifUserId(), this.notifMessage(),
      this.notifSms(), this.notifEmail()
    ).subscribe(res => {
      this.notifResult.set(res);
    });
  }
}
