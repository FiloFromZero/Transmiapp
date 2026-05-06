package com.transmiapp.patterns.strategy;

/**
 * Estrategia de tarifa normal: cobra el 100% de la tarifa base.
 */
public class NormalFareStrategy implements FareStrategy {

    @Override
    public int calculateFare(int baseFare) {
        return baseFare;
    }

    @Override
    public String getStrategyName() {
        return "TARIFA_NORMAL";
    }
}
