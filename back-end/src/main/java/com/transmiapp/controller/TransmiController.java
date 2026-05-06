package com.transmiapp.controller;

import com.transmiapp.patterns.builder.Route;
import com.transmiapp.patterns.command.AssignVehicleCommand;
import com.transmiapp.patterns.command.CommandInvoker;
import com.transmiapp.patterns.command.StationAlertCommand;
import com.transmiapp.patterns.decorator.BasicNotificationService;
import com.transmiapp.patterns.decorator.EmailNotificationDecorator;
import com.transmiapp.patterns.decorator.NotificationService;
import com.transmiapp.patterns.decorator.SmsNotificationDecorator;
import com.transmiapp.patterns.facade.TransitSystemFacade;
import com.transmiapp.patterns.factory.Vehicle;
import com.transmiapp.patterns.factory.VehicleFactory;
import com.transmiapp.patterns.observer.TransportEventNotifier;
import com.transmiapp.patterns.singleton.TransmiConfig;
import com.transmiapp.patterns.strategy.FareCalculator;
import com.transmiapp.patterns.templatemethod.FleetReport;
import com.transmiapp.patterns.templatemethod.PassengerReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST que expone endpoints para demostrar los 10 patrones de diseño.
 */
@RestController
@RequestMapping("/api/transmi")
@CrossOrigin(origins = "*")
public class TransmiController {

    private final TransmiConfig config;
    private final VehicleFactory vehicleFactory;
    private final FareCalculator fareCalculator;
    private final TransitSystemFacade facade;
    private final TransportEventNotifier eventNotifier;
    private final CommandInvoker commandInvoker;
    private final PassengerReport passengerReport;
    private final FleetReport fleetReport;

    public TransmiController(TransmiConfig config,
                              VehicleFactory vehicleFactory,
                              FareCalculator fareCalculator,
                              TransitSystemFacade facade,
                              TransportEventNotifier eventNotifier,
                              CommandInvoker commandInvoker,
                              PassengerReport passengerReport,
                              FleetReport fleetReport) {
        this.config = config;
        this.vehicleFactory = vehicleFactory;
        this.fareCalculator = fareCalculator;
        this.facade = facade;
        this.eventNotifier = eventNotifier;
        this.commandInvoker = commandInvoker;
        this.passengerReport = passengerReport;
        this.fleetReport = fleetReport;
    }

    // ==========================================
    // 1. SINGLETON - Configuración del sistema
    // ==========================================
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        return ResponseEntity.ok(Map.of(
                "pattern", "Singleton",
                "data", config.toString(),
                "hashCode", config.hashCode()
        ));
    }

    // ==========================================
    // 2. FACTORY METHOD - Crear vehículos
    // ==========================================
    @PostMapping("/vehicles")
    public ResponseEntity<Map<String, Object>> createVehicle(
            @RequestParam String type,
            @RequestParam(required = false) String route) {
        Vehicle vehicle = vehicleFactory.createVehicle(type, route != null ? route : "SIN_RUTA");
        return ResponseEntity.ok(Map.of(
                "pattern", "Factory Method",
                "vehicle", vehicle.getInfo()
        ));
    }

    // ==========================================
    // 3. BUILDER - Construir rutas
    // ==========================================
    @PostMapping("/routes")
    public ResponseEntity<Map<String, Object>> createRoute(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false, defaultValue = "false") boolean express) {
        Route route = new Route.RouteBuilder(code, name)
                .origin(origin)
                .destination(destination)
                .addStation(origin)
                .addStation("Estación Intermedia 1")
                .addStation("Estación Intermedia 2")
                .addStation(destination)
                .frequency(express ? 5 : 10)
                .vehicleType(express ? "BIARTICULADO" : "ARTICULADO")
                .express(express)
                .build();

        return ResponseEntity.ok(Map.of(
                "pattern", "Builder",
                "route", route.toString(),
                "stations", route.getStations(),
                "frequency", route.getFrequencyMinutes() + " min"
        ));
    }

    // ==========================================
    // 4. OBSERVER - Notificar eventos
    // ==========================================
    @PostMapping("/events/arrival")
    public ResponseEntity<Map<String, Object>> notifyArrival(
            @RequestParam String station,
            @RequestParam String route) {
        eventNotifier.notifyBusArrival(station, route);
        return ResponseEntity.ok(Map.of(
                "pattern", "Observer",
                "action", "Bus arrival notified",
                "station", station,
                "route", route
        ));
    }

    // ==========================================
    // 5. STRATEGY - Calcular tarifas
    // ==========================================
    @GetMapping("/fare")
    public ResponseEntity<Map<String, Object>> calculateFare(
            @RequestParam(defaultValue = "NORMAL") String userType) {
        fareCalculator.setStrategyByUserType(userType);
        int fare = fareCalculator.calculate(config.getBaseFare());
        return ResponseEntity.ok(Map.of(
                "pattern", "Strategy",
                "userType", userType,
                "strategy", fareCalculator.getCurrentStrategyName(),
                "baseFare", config.getBaseFare(),
                "calculatedFare", fare,
                "currency", config.getCurrency()
        ));
    }

    // ==========================================
    // 6. ADAPTER - Geolocalización
    // ==========================================
    @GetMapping("/geo/distance")
    public ResponseEntity<Map<String, Object>> getDistance(
            @RequestParam String origin,
            @RequestParam String destination) {
        return ResponseEntity.ok(Map.of(
                "pattern", "Adapter",
                "origin", origin,
                "destination", destination,
                "distanceKm", facade.planTrip(origin, destination, "NORMAL").get("distanceKm"),
                "estimatedMinutes", facade.planTrip(origin, destination, "NORMAL").get("estimatedMinutes")
        ));
    }

    // ==========================================
    // 7. DECORATOR - Notificaciones
    // ==========================================
    @PostMapping("/notifications")
    public ResponseEntity<Map<String, Object>> sendNotification(
            @RequestParam String userId,
            @RequestParam String message,
            @RequestParam(defaultValue = "false") boolean sms,
            @RequestParam(defaultValue = "false") boolean email) {

        NotificationService service = new BasicNotificationService();
        if (sms) service = new SmsNotificationDecorator(service);
        if (email) service = new EmailNotificationDecorator(service);

        boolean sent = service.sendNotification(userId, message);
        return ResponseEntity.ok(Map.of(
                "pattern", "Decorator",
                "channels", service.getDescription(),
                "sent", sent,
                "userId", userId
        ));
    }

    // ==========================================
    // 8. FACADE - Planificar viaje
    // ==========================================
    @GetMapping("/trip")
    public ResponseEntity<Map<String, Object>> planTrip(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "NORMAL") String userType) {
        Map<String, Object> trip = facade.planTrip(origin, destination, userType);
        trip = new java.util.HashMap<>(trip);
        trip.put("pattern", "Facade");
        return ResponseEntity.ok(trip);
    }

    // ==========================================
    // 9. TEMPLATE METHOD - Generar reportes
    // ==========================================
    @GetMapping("/reports/{type}")
    public ResponseEntity<Map<String, Object>> generateReport(@PathVariable String type) {
        String report = switch (type.toUpperCase()) {
            case "PASSENGERS", "PASAJEROS" -> passengerReport.generateReport();
            case "FLEET", "FLOTA" -> fleetReport.generateReport();
            default -> throw new IllegalArgumentException("Tipo de reporte no reconocido: " + type);
        };
        return ResponseEntity.ok(Map.of(
                "pattern", "Template Method",
                "reportType", type,
                "report", report
        ));
    }

    // ==========================================
    // 10. COMMAND - Ejecutar operaciones
    // ==========================================
    @PostMapping("/commands/assign-vehicle")
    public ResponseEntity<Map<String, Object>> assignVehicle(
            @RequestParam String vehicleType,
            @RequestParam String routeCode) {
        AssignVehicleCommand command = new AssignVehicleCommand(vehicleFactory, vehicleType, routeCode);
        commandInvoker.executeCommand(command);
        return ResponseEntity.ok(Map.of(
                "pattern", "Command",
                "action", "assign-vehicle",
                "vehicle", command.getCreatedVehicle().getInfo(),
                "history", commandInvoker.getHistory()
        ));
    }

    @PostMapping("/commands/station-alert")
    public ResponseEntity<Map<String, Object>> stationAlert(
            @RequestParam String station,
            @RequestParam String message) {
        StationAlertCommand command = new StationAlertCommand(eventNotifier, station, message);
        commandInvoker.executeCommand(command);
        return ResponseEntity.ok(Map.of(
                "pattern", "Command",
                "action", "station-alert",
                "station", station,
                "history", commandInvoker.getHistory()
        ));
    }

    @PostMapping("/commands/undo")
    public ResponseEntity<Map<String, Object>> undoLastCommand() {
        commandInvoker.undoLastCommand();
        return ResponseEntity.ok(Map.of(
                "pattern", "Command",
                "action", "undo",
                "remainingHistory", commandInvoker.getHistory()
        ));
    }

    // ==========================================
    // Estado general del sistema
    // ==========================================
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        return ResponseEntity.ok(facade.getSystemStatus());
    }

    @GetMapping("/patterns")
    public ResponseEntity<List<Map<String, String>>> listPatterns() {
        return ResponseEntity.ok(List.of(
                Map.of("id", "1", "name", "Singleton", "endpoint", "GET /api/transmi/config"),
                Map.of("id", "2", "name", "Factory Method", "endpoint", "POST /api/transmi/vehicles?type=ARTICULADO"),
                Map.of("id", "3", "name", "Builder", "endpoint", "POST /api/transmi/routes?code=B23&name=Calle80&origin=Portal80&destination=PortalNorte"),
                Map.of("id", "4", "name", "Observer", "endpoint", "POST /api/transmi/events/arrival?station=PortalNorte&route=B23"),
                Map.of("id", "5", "name", "Strategy", "endpoint", "GET /api/transmi/fare?userType=ESTUDIANTE"),
                Map.of("id", "6", "name", "Adapter", "endpoint", "GET /api/transmi/geo/distance?origin=Calle80&destination=PortalNorte"),
                Map.of("id", "7", "name", "Decorator", "endpoint", "POST /api/transmi/notifications?userId=123&message=Hola&sms=true&email=true"),
                Map.of("id", "8", "name", "Facade", "endpoint", "GET /api/transmi/trip?origin=Calle80&destination=PortalNorte&userType=ESTUDIANTE"),
                Map.of("id", "9", "name", "Template Method", "endpoint", "GET /api/transmi/reports/pasajeros"),
                Map.of("id", "10", "name", "Command", "endpoint", "POST /api/transmi/commands/assign-vehicle?vehicleType=ARTICULADO&routeCode=B23")
        ));
    }
}
