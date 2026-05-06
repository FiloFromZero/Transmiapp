package com.transmiapp.patterns.observer;

import java.time.LocalDateTime;

/**
 * Evento del sistema de transporte que se notifica a los observadores.
 */
public class TransportEvent {

    public enum EventType {
        BUS_ARRIVAL,
        BUS_DEPARTURE,
        ROUTE_DELAY,
        STATION_ALERT,
        SYSTEM_MAINTENANCE
    }

    private final EventType type;
    private final String station;
    private final String route;
    private final String message;
    private final LocalDateTime timestamp;

    public TransportEvent(EventType type, String station, String route, String message) {
        this.type = type;
        this.station = station;
        this.route = route;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public EventType getType() { return type; }
    public String getStation() { return station; }
    public String getRoute() { return route; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s - Estación: %s, Ruta: %s - %s",
                timestamp, type, station, route, message);
    }
}
