package com.transmiapp.patterns.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio externo de mapas con una interfaz incompatible con el sistema interno.
 * Simula una API externa (ej. Google Maps, OpenStreetMap).
 */
public class ExternalMapApi {

    /**
     * Busca un punto de interés por texto y retorna datos en formato propio.
     */
    public Map<String, Object> searchPlace(String query) {
        Map<String, Object> result = new HashMap<>();
        result.put("lat", 4.6097);
        result.put("lng", -74.0817);
        result.put("formatted_address", query + ", Bogotá, Colombia");
        result.put("place_id", "ext_" + query.hashCode());
        return result;
    }

    /**
     * Calcula una ruta entre dos puntos y retorna datos en metros y segundos.
     */
    public Map<String, Object> getDirections(double latOrig, double lngOrig, double latDest, double lngDest) {
        Map<String, Object> result = new HashMap<>();
        result.put("distance_meters", 5200);
        result.put("duration_seconds", 1080);
        result.put("polyline", "encoded_polyline_data");
        return result;
    }
}
