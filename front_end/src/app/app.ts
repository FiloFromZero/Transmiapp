import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RoleService, AppRole } from './core/services/role.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  template: `
    <router-outlet />

    <!-- Floating Role Switcher FAB -->
    <div class="role-fab-container">
      @if (expanded()) {
        <div class="role-fab-panel">
          <p class="fab-panel-title">Cambiar Rol</p>
          <button class="fab-role-btn" [class.fab-role-btn--active]="roleSvc.currentRole() === 'user'"
            (click)="setRole('user')">
            👤 Usuario
          </button>
          <button class="fab-role-btn" [class.fab-role-btn--active]="roleSvc.currentRole() === 'admin'"
            (click)="setRole('admin')">
            🛡️ Administrador
          </button>
        </div>
      }
      <button class="role-fab-btn" (click)="expanded.set(!expanded())"
        [class.role-fab-btn--admin]="roleSvc.currentRole() === 'admin'"
        [title]="'Rol: ' + roleSvc.currentRole()">
        {{ roleSvc.currentRole() === 'admin' ? '🛡️' : '👤' }}
        <span class="fab-label">{{ expanded() ? '✕' : roleSvc.currentRole() | uppercase }}</span>
      </button>
    </div>
  `,
  styles: [`
    :host { display: block; }

    .role-fab-container {
      position: fixed;
      bottom: 76px;
      right: 16px;
      z-index: 9999;
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      gap: 8px;
    }

    .role-fab-panel {
      background: rgba(14, 17, 25, 0.95);
      border: 1px solid rgba(255,255,255,0.15);
      backdrop-filter: blur(12px);
      border-radius: 14px;
      padding: 12px;
      display: flex;
      flex-direction: column;
      gap: 8px;
      box-shadow: 0 8px 24px rgba(0,0,0,0.6);
      animation: fadeUp 0.2s ease;
    }

    @keyframes fadeUp {
      from { opacity: 0; transform: translateY(8px); }
      to   { opacity: 1; transform: translateY(0); }
    }

    .fab-panel-title {
      font-size: 0.7rem;
      color: rgba(255,255,255,0.4);
      text-transform: uppercase;
      letter-spacing: 0.5px;
      margin: 0 0 4px;
    }

    .fab-role-btn {
      background: rgba(255,255,255,0.07);
      border: 1px solid rgba(255,255,255,0.12);
      color: rgba(255,255,255,0.7);
      padding: 8px 14px;
      border-radius: 10px;
      cursor: pointer;
      font-size: 0.85rem;
      font-weight: 600;
      transition: all 0.2s;
      text-align: left;

      &:hover { background: rgba(255,255,255,0.14); color: white; }

      &--active {
        background: rgba(232, 65, 85, 0.2);
        border-color: rgba(232, 65, 85, 0.5);
        color: #E84155;
      }
    }

    .fab-role-btn--active.fab-role-btn:last-child {
      background: rgba(76, 175, 80, 0.2);
      border-color: rgba(76, 175, 80, 0.5);
      color: #4CAF50;
    }

    .role-fab-btn {
      background: rgba(14, 17, 25, 0.92);
      border: 1px solid rgba(232, 65, 85, 0.5);
      color: #E84155;
      padding: 8px 14px;
      border-radius: 50px;
      cursor: pointer;
      font-size: 0.8rem;
      font-weight: 700;
      display: flex;
      align-items: center;
      gap: 6px;
      box-shadow: 0 4px 14px rgba(0,0,0,0.5);
      backdrop-filter: blur(8px);
      transition: all 0.2s;

      &:hover { transform: scale(1.05); }

      &--admin {
        border-color: rgba(76, 175, 80, 0.5);
        color: #4CAF50;
      }
    }

    .fab-label {
      font-size: 0.7rem;
      letter-spacing: 0.5px;
    }
  `],
})
export class App {
  roleSvc = inject(RoleService);
  expanded = signal(false);

  setRole(role: AppRole): void {
    this.roleSvc.setRole(role);
    this.expanded.set(false);
  }
}

