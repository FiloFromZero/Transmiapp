package com.transmiapp.patterns.command;

import com.transmiapp.patterns.observer.TransportEvent;
import com.transmiapp.patterns.observer.TransportEventNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comando concreto: activa una alerta en una estación.
 */
public class StationAlertCommand implements TransitCommand {

    private static final Logger log = LoggerFactory.getLogger(StationAlertCommand.class);

    private final TransportEventNotifier notifier;
    private final String station;
    private final String alertMessage;
    private boolean executed = false;

    public StationAlertCommand(TransportEventNotifier notifier, String station, String alertMessage) {
        this.notifier = notifier;
        this.station = station;
        this.alertMessage = alertMessage;
    }

    @Override
    public void execute() {
        notifier.notifyEvent(new TransportEvent(
                TransportEvent.EventType.STATION_ALERT,
                station, "N/A", alertMessage
        ));
        executed = true;
        log.info("Alerta activada en estación {}: {}", station, alertMessage);
    }

    @Override
    public void undo() {
        if (executed) {
            notifier.notifyEvent(new TransportEvent(
                    TransportEvent.EventType.STATION_ALERT,
                    station, "N/A", "CANCELADA: " + alertMessage
            ));
            executed = false;
            log.info("Alerta cancelada en estación {}", station);
        }
    }

    @Override
    public String getDescription() {
        return String.format("Alerta en estación %s: %s", station, alertMessage);
    }
}
