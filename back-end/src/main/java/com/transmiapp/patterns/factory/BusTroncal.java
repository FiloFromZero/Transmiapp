package com.transmiapp.patterns.factory;

/**
 * Bus troncal estándar del sistema TransMilenio.
 */
public class BusTroncal implements Vehicle {

    private String route;
    private String status = "EN_SERVICIO";

    @Override
    public String getType() {
        return "BUS_TRONCAL";
    }

    @Override
    public int getCapacity() {
        return 80;
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
