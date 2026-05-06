package com.transmiapp.patterns.adapter;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN 6: ADAPTER
 * 
 * Adaptador que traduce la interfaz del servicio externo de mapas
 * a la interfaz interna GeoLocationService del sistema TransMilenio.
 */
@Component
public class ExternalMapAdapter implements GeoLocationService {

    private final ExternalMapApi externalApi;

    public ExternalMapAdapter() {
        this.externalApi = new ExternalMapApi();
    }

    @Override
    public Map<String, Double> getStationCoordinates(String stationName) {
        // Adapta la respuesta del API externo al formato interno
        Map<String, Object> externalResult = externalApi.searchPlace("Estación " + stationName);

        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("latitude", ((Number) externalResult.get("lat")).doubleValue());
        coordinates.put("longitude", ((Number) externalResult.get("lng")).doubleValue());
        return coordinates;
    }

    @Override
    public double calculateDistance(String originStation, String destinationStation) {
        // Obtiene coordenadas de ambas estaciones
        Map<String, Double> origin = getStationCoordinates(originStation);
        Map<String, Double> dest = getStationCoordinates(destinationStation);

        // Usa el API externo (que retorna metros) y convierte a kilómetros
        Map<String, Object> directions = externalApi.getDirections(
                origin.get("latitude"), origin.get("longitude"),
                dest.get("latitude"), dest.get("longitude")
        );

        int distanceMeters = ((Number) directions.get("distance_meters")).intValue();
        return distanceMeters / 1000.0;
    }

    @Override
    public int estimateTravelTime(String originStation, String destinationStation) {
        // Obtiene coordenadas de ambas estaciones
        Map<String, Double> origin = getStationCoordinates(originStation);
        Map<String, Double> dest = getStationCoordinates(destinationStation);

        // Usa el API externo (que retorna segundos) y convierte a minutos
        Map<String, Object> directions = externalApi.getDirections(
                origin.get("latitude"), origin.get("longitude"),
                dest.get("latitude"), dest.get("longitude")
        );

        int durationSeconds = ((Number) directions.get("duration_seconds")).intValue();
        return (int) Math.ceil(durationSeconds / 60.0);
    }
}
