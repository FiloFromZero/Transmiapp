package com.transmiapp.patterns.singleton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * PATRÓN 1: SINGLETON
 * 
 * Gestiona la configuración centralizada del sistema TransMilenio.
 * Spring garantiza una única instancia mediante el scope por defecto (singleton).
 * Adicionalmente se implementa el patrón clásico con double-checked locking
 * para demostración del patrón puro.
 */
@Component
public class TransmiConfig {

    private static volatile TransmiConfig instance;

    @Value("${transmiapp.system.name:TransMilenio}")
    private String systemName;

    @Value("${transmiapp.system.city:Bogota}")
    private String city;

    @Value("${transmiapp.fare.base:2950}")
    private int baseFare;

    @Value("${transmiapp.fare.currency:COP}")
    private String currency;

    private boolean maintenanceMode = false;

    /**
     * Obtiene la instancia única de TransmiConfig (patrón Singleton clásico).
     * En el contexto de Spring, se usa la inyección de dependencias.
     * Este método se ofrece como alternativa para uso fuera del contexto de Spring.
     */
    public static TransmiConfig getInstance() {
        if (instance == null) {
            synchronized (TransmiConfig.class) {
                if (instance == null) {
                    instance = new TransmiConfig();
                }
            }
        }
        return instance;
    }

    /**
     * Registra la instancia de Spring como la instancia singleton.
     */
    @jakarta.annotation.PostConstruct
    private void registerInstance() {
        instance = this;
    }

    // --- Getters ---

    public String getSystemName() {
        return systemName;
    }

    public String getCity() {
        return city;
    }

    public int getBaseFare() {
        return baseFare;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    @Override
    public String toString() {
        return String.format("TransmiConfig{system='%s', city='%s', fare=%d %s, maintenance=%s}",
                systemName, city, baseFare, currency, maintenanceMode);
    }
}
