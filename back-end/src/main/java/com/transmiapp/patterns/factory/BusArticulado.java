package com.transmiapp.patterns.factory;

/**
 * Bus articulado del sistema TransMilenio.
 */
public class BusArticulado implements Vehicle {

    private String route;
    private String status = "EN_SERVICIO";

    @Override
    public String getType() {
        return "ARTICULADO";
    }

    @Override
    public int getCapacity() {
        return 160;
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
