package com.transmiapp.patterns.factory;

import org.springframework.stereotype.Component;

/**
 * PATRÓN 2: FACTORY METHOD
 * 
 * Fábrica que crea instancias de vehículos según el tipo solicitado.
 * Encapsula la lógica de creación y desacopla al cliente del tipo concreto.
 */
@Component
public class VehicleFactory {

    /**
     * Crea un vehículo del tipo especificado.
     *
     * @param type Tipo de vehículo: TRONCAL, ARTICULADO, BIARTICULADO, ALIMENTADOR
     * @return Instancia concreta del vehículo solicitado
     * @throws IllegalArgumentException si el tipo no es reconocido
     */
    public Vehicle createVehicle(String type) {
        return switch (type.toUpperCase()) {
            case "TRONCAL" -> new BusTroncal();
            case "ARTICULADO" -> new BusArticulado();
            case "BIARTICULADO" -> new BusBiarticulado();
            case "ALIMENTADOR" -> new BusAlimentador();
            default -> throw new IllegalArgumentException("Tipo de vehículo no reconocido: " + type);
        };
    }

    /**
     * Crea un vehículo y le asigna una ruta.
     */
    public Vehicle createVehicle(String type, String route) {
        Vehicle vehicle = createVehicle(type);
        vehicle.setRoute(route);
        return vehicle;
    }
}
