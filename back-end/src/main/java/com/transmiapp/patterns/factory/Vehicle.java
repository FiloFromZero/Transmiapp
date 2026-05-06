package com.transmiapp.patterns.factory;

/**
 * PATRÓN 2: FACTORY METHOD
 * 
 * Interfaz base para todos los vehículos del sistema TransMilenio.
 */
public interface Vehicle {

    String getType();

    int getCapacity();

    String getRoute();

    void setRoute(String route);

    String getStatus();

    void setStatus(String status);

    /**
     * Retorna la información completa del vehículo.
     */
    default String getInfo() {
        return String.format("[%s] Capacidad: %d | Ruta: %s | Estado: %s",
                getType(), getCapacity(), getRoute(), getStatus());
    }
}
