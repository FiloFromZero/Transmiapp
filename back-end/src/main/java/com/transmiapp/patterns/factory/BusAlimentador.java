package com.transmiapp.patterns.factory;

/**
 * Bus alimentador del sistema TransMilenio (SITP).
 */
public class BusAlimentador implements Vehicle {

    private String route;
    private String status = "EN_SERVICIO";

    @Override
    public String getType() {
        return "ALIMENTADOR";
    }

    @Override
    public int getCapacity() {
        return 40;
    }

    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
