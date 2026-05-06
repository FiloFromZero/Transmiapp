package com.transmiapp.patterns.facade;

import com.transmiapp.patterns.builder.Route;
import com.transmiapp.patterns.factory.Vehicle;
import com.transmiapp.patterns.factory.VehicleFactory;
import com.transmiapp.patterns.observer.TransportEventNotifier;
import com.transmiapp.patterns.singleton.TransmiConfig;
import com.transmiapp.patterns.strategy.FareCalculator;
import com.transmiapp.patterns.adapter.GeoLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * PATRÓN 8: FACADE
 * 
 * Fachada que simplifica la interacción con los múltiples subsistemas
 * del sistema TransMilenio. Ofrece operaciones de alto nivel que
 * internamente coordinan la fábrica de vehículos, el calculador de tarifas,
 * el servicio de geolocalización y el notificador de eventos.
 */
@Component
public class TransitSystemFacade {

    private static final Logger log = LoggerFactory.getLogger(TransitSystemFacade.class);

    private final TransmiConfig config;
    private final VehicleFactory vehicleFactory;
    private final FareCalculator fareCalculator;
    private final GeoLocationService geoService;
    private final TransportEventNotifier eventNotifier;

    public TransitSystemFacade(TransmiConfig config,
                                VehicleFactory vehicleFactory,
                                FareCalculator fareCalculator,
                                GeoLocationService geoService,
                                TransportEventNotifier eventNotifier) {
        this.config = config;
        this.vehicleFactory = vehicleFactory;
        this.fareCalculator = fareCalculator;
        this.geoService = geoService;
        this.eventNotifier = eventNotifier;
    }

    /**
     * Planifica un viaje completo: calcula tarifa, estima tiempo y distancia.
     */
    public Map<String, Object> planTrip(String origin, String destination, String userType) {
        log.info("Planificando viaje: {} -> {} (usuario: {})", origin, destination, userType);

        // Calcular tarifa según tipo de usuario (Strategy)
        fareCalculator.setStrategyByUserType(userType);
        int fare = fareCalculator.calculate(config.getBaseFare());

        // Estimar distancia y tiempo (Adapter)
        double distance = geoService.calculateDistance(origin, destination);
        int travelTime = geoService.estimateTravelTime(origin, destination);

        return Map.of(
                "origin", origin,
                "destination", destination,
                "fare", fare,
                "currency", config.getCurrency(),
                "fareType", fareCalculator.getCurrentStrategyName(),
                "distanceKm", distance,
                "estimatedMinutes", travelTime
        );
    }

    /**
     * Despacha un vehículo a una ruta y notifica a los observadores.
     */
    public Vehicle dispatchVehicle(String vehicleType, String routeCode, String station) {
        log.info("Despachando vehículo {} a ruta {} desde estación {}", vehicleType, routeCode, station);

        // Crear vehículo (Factory)
        Vehicle vehicle = vehicleFactory.createVehicle(vehicleType, routeCode);

        // Notificar llegada (Observer)
        eventNotifier.notifyBusArrival(station, routeCode);

        return vehicle;
    }

    /**
     * Consulta el estado general del sistema.
     */
    public Map<String, Object> getSystemStatus() {
        return Map.of(
                "system", config.getSystemName(),
                "city", config.getCity(),
                "baseFare", config.getBaseFare(),
                "currency", config.getCurrency(),
                "maintenanceMode", config.isMaintenanceMode()
        );
    }
}
