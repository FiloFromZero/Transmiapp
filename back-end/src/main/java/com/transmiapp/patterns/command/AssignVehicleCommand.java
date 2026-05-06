package com.transmiapp.patterns.command;

import com.transmiapp.patterns.factory.Vehicle;
import com.transmiapp.patterns.factory.VehicleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comando concreto: asigna un vehículo a una ruta.
 */
public class AssignVehicleCommand implements TransitCommand {

    private static final Logger log = LoggerFactory.getLogger(AssignVehicleCommand.class);

    private final VehicleFactory factory;
    private final String vehicleType;
    private final String routeCode;
    private Vehicle createdVehicle;

    public AssignVehicleCommand(VehicleFactory factory, String vehicleType, String routeCode) {
        this.factory = factory;
        this.vehicleType = vehicleType;
        this.routeCode = routeCode;
    }

    @Override
    public void execute() {
        createdVehicle = factory.createVehicle(vehicleType, routeCode);
        createdVehicle.setStatus("ASIGNADO");
        log.info("Vehículo {} asignado a ruta {}", createdVehicle.getType(), routeCode);
    }

    @Override
    public void undo() {
        if (createdVehicle != null) {
            createdVehicle.setStatus("DESASIGNADO");
            createdVehicle.setRoute(null);
            log.info("Asignación de vehículo {} a ruta {} revertida", createdVehicle.getType(), routeCode);
        }
    }

    @Override
    public String getDescription() {
        return String.format("Asignar vehículo %s a ruta %s", vehicleType, routeCode);
    }

    public Vehicle getCreatedVehicle() {
        return createdVehicle;
    }
}
