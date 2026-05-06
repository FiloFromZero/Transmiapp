package com.transmiapp.patterns.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN 3: BUILDER
 * 
 * Representa una ruta completa del sistema TransMilenio.
 * Se construye paso a paso mediante el RouteBuilder interno.
 */
public class Route {

    private final String code;
    private final String name;
    private final String origin;
    private final String destination;
    private final List<String> stations;
    private final String scheduleStart;
    private final String scheduleEnd;
    private final int frequencyMinutes;
    private final String vehicleType;
    private final boolean isExpress;

    private Route(RouteBuilder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.origin = builder.origin;
        this.destination = builder.destination;
        this.stations = builder.stations;
        this.scheduleStart = builder.scheduleStart;
        this.scheduleEnd = builder.scheduleEnd;
        this.frequencyMinutes = builder.frequencyMinutes;
        this.vehicleType = builder.vehicleType;
        this.isExpress = builder.isExpress;
    }

    // --- Getters ---

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public List<String> getStations() { return stations; }
    public String getScheduleStart() { return scheduleStart; }
    public String getScheduleEnd() { return scheduleEnd; }
    public int getFrequencyMinutes() { return frequencyMinutes; }
    public String getVehicleType() { return vehicleType; }
    public boolean isExpress() { return isExpress; }

    @Override
    public String toString() {
        return String.format("Route{code='%s', name='%s', %s -> %s, stations=%d, express=%s}",
                code, name, origin, destination, stations.size(), isExpress);
    }

    /**
     * Builder interno para construir objetos Route paso a paso.
     */
    public static class RouteBuilder {

        private String code;
        private String name;
        private String origin;
        private String destination;
        private List<String> stations = new ArrayList<>();
        private String scheduleStart = "04:30";
        private String scheduleEnd = "23:00";
        private int frequencyMinutes = 10;
        private String vehicleType = "ARTICULADO";
        private boolean isExpress = false;

        public RouteBuilder(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public RouteBuilder origin(String origin) {
            this.origin = origin;
            return this;
        }

        public RouteBuilder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public RouteBuilder addStation(String station) {
            this.stations.add(station);
            return this;
        }

        public RouteBuilder schedule(String start, String end) {
            this.scheduleStart = start;
            this.scheduleEnd = end;
            return this;
        }

        public RouteBuilder frequency(int minutes) {
            this.frequencyMinutes = minutes;
            return this;
        }

        public RouteBuilder vehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        public RouteBuilder express(boolean isExpress) {
            this.isExpress = isExpress;
            return this;
        }

        /**
         * Construye el objeto Route inmutable.
         */
        public Route build() {
            if (code == null || name == null) {
                throw new IllegalStateException("El código y nombre de la ruta son obligatorios");
            }
            if (origin == null || destination == null) {
                throw new IllegalStateException("El origen y destino de la ruta son obligatorios");
            }
            return new Route(this);
        }
    }
}
