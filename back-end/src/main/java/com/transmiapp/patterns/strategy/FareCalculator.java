package com.transmiapp.patterns.strategy;

import org.springframework.stereotype.Component;

/**
 * PATRÓN 5: STRATEGY - Contexto
 * 
 * Calculadora de tarifas que utiliza la estrategia configurada
 * para determinar el monto a cobrar al usuario.
 */
@Component
public class FareCalculator {

    private FareStrategy strategy;

    public FareCalculator() {
        this.strategy = new NormalFareStrategy();
    }

    /**
     * Cambia la estrategia de cálculo de tarifa en tiempo de ejecución.
     */
    public void setStrategy(FareStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Selecciona la estrategia según el tipo de usuario.
     */
    public void setStrategyByUserType(String userType) {
        this.strategy = switch (userType.toUpperCase()) {
            case "ESTUDIANTE" -> new StudentFareStrategy();
            case "ADULTO_MAYOR" -> new ElderlyFareStrategy();
            case "DISCAPACIDAD" -> new ElderlyFareStrategy();
            default -> new NormalFareStrategy();
        };
    }

    /**
     * Calcula la tarifa usando la estrategia actual.
     */
    public int calculate(int baseFare) {
        return strategy.calculateFare(baseFare);
    }

    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }
}
