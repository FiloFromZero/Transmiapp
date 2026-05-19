import { Injectable, signal } from '@angular/core';

export type AppRole = 'user' | 'admin';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  // Global state for the user's role
  readonly currentRole = signal<AppRole>('user');

  setRole(role: AppRole): void {
    this.currentRole.set(role);
  }

  toggleRole(): void {
    this.currentRole.update(r => r === 'user' ? 'admin' : 'user');
  }
}
