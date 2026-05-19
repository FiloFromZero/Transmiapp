import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransmiPatternsService {
  private readonly http = inject(HttpClient);
  private readonly BASE_URL = '/api/transmi';

  // ── Strategy Pattern ──────────────────────────────────────
  getFare(userType: string): Observable<any> {
    return this.http.get(`${this.BASE_URL}/fare`, { params: { userType } });
  }

  // ── Adapter Pattern ───────────────────────────────────────
  getDistance(origin: string, destination: string): Observable<any> {
    return this.http.get(`${this.BASE_URL}/geo/distance`, { params: { origin, destination } });
  }

  // ── Facade Pattern ────────────────────────────────────────
  planTrip(origin: string, destination: string, userType: string): Observable<any> {
    return this.http.get(`${this.BASE_URL}/trip`, { params: { origin, destination, userType } });
  }

  // ── Template Method Pattern ───────────────────────────────
  generateReport(type: 'pasajeros' | 'flota'): Observable<any> {
    return this.http.get(`${this.BASE_URL}/reports/${type}`);
  }

  // ── Command Pattern ───────────────────────────────────────
  assignVehicle(vehicleType: string, routeCode: string): Observable<any> {
    return this.http.post(`${this.BASE_URL}/commands/assign-vehicle`, null, {
      params: { vehicleType, routeCode }
    });
  }

  undoLastCommand(): Observable<any> {
    return this.http.post(`${this.BASE_URL}/commands/undo`, null);
  }

  // ── Factory Method Pattern ────────────────────────────────
  createVehicle(type: string): Observable<any> {
    return this.http.post(`${this.BASE_URL}/vehicles`, null, {
      params: { type }
    });
  }

  // ── Singleton Pattern ─────────────────────────────────────
  getConfig(): Observable<any> {
    return this.http.get(`${this.BASE_URL}/config`);
  }

  // ── Builder Pattern ───────────────────────────────────────
  buildRoute(code: string, name: string, origin: string, destination: string, express: boolean): Observable<any> {
    return this.http.post(`${this.BASE_URL}/routes`, null, {
      params: { code, name, origin, destination, express: String(express) }
    });
  }

  // ── Observer Pattern ──────────────────────────────────────
  notifyArrival(station: string, route: string): Observable<any> {
    return this.http.post(`${this.BASE_URL}/events/arrival`, null, {
      params: { station, route }
    });
  }

  // ── Decorator Pattern ─────────────────────────────────────
  sendNotification(userId: string, message: string, sms: boolean, email: boolean): Observable<any> {
    return this.http.post(`${this.BASE_URL}/notifications`, null, {
      params: { userId, message, sms: String(sms), email: String(email) }
    });
  }
}
