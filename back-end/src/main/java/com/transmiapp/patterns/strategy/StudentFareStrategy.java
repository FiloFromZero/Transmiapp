package com.transmiapp.patterns.strategy;

/**
 * Estrategia de tarifa estudiantil: 40% de descuento sobre la tarifa base.
 */
public class StudentFareStrategy implements FareStrategy {

    private static final double DISCOUNT = 0.40;

    @Override
    public int calculateFare(int baseFare) {
        return (int) Math.round(baseFare * (1 - DISCOUNT));
    }

    @Override
    public String getStrategyName() {
        return "TARIFA_ESTUDIANTE";
    }
}
