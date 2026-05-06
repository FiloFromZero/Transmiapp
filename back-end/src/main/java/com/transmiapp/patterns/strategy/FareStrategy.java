package com.transmiapp.patterns.strategy;

/**
 * PATRÓN 5: STRATEGY
 * 
 * Interfaz que define la estrategia de cálculo de tarifa.
 * Cada implementación encapsula un algoritmo diferente.
 */
public interface FareStrategy {

    /**
     * Calcula la tarifa a cobrar.
     *
     * @param baseFare Tarifa base del sistema
     * @return Tarifa calculada según la estrategia
     */
    int calculateFare(int baseFare);

    /**
     * Retorna el nombre descriptivo de la estrategia.
     */
    String getStrategyName();
}
