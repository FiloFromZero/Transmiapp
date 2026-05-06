package com.transmiapp.patterns.strategy;

/**
 * Estrategia de tarifa para adulto mayor: 100% de descuento (gratuito).
 */
public class ElderlyFareStrategy implements FareStrategy {

    @Override
    public int calculateFare(int baseFare) {
        return 0;
    }

    @Override
    public String getStrategyName() {
        return "TARIFA_ADULTO_MAYOR";
    }
}
