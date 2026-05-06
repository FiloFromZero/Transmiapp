import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
    title: 'Inicio · TransmiApp',
  },
  {
    path: 'tracking',
    loadComponent: () =>
      import('./features/tracking/tracking.component').then(m => m.TrackingComponent),
    title: 'Mapa en vivo · TransmiApp',
  },
  {
    path: 'novedades',
    loadComponent: () =>
      import('./features/novedades/novedades.component').then(m => m.NovedadesComponent),
    title: 'Novedades · TransmiApp',
  },
  {
    path: '**',
    redirectTo: 'dashboard',
  },
];
