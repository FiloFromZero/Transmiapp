package com.transmiapp.patterns.adapter;

import java.util.Map;

/**
 * PATRÓN 6: ADAPTER
 * 
 * Interfaz del sistema interno de geolocalización de TransMilenio.
 * Define el contrato que el sistema espera para consultar ubicaciones.
 */
public interface GeoLocationService {

    /**
     * Obtiene las coordenadas de una estación por su nombre.
     *
     * @param stationName Nombre de la estación
     * @return Mapa con "latitude" y "longitude"
     */
    Map<String, Double> getStationCoordinates(String stationName);

    /**
     * Calcula la distancia entre dos estaciones en kilómetros.
     */
    double calculateDistance(String originStation, String destinationStation);

    /**
     * Estima el tiempo de viaje entre dos estaciones en minutos.
     */
    int estimateTravelTime(String originStation, String destinationStation);
}
