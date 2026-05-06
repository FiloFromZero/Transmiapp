package com.transmiapp.patterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PATRÓN 4: OBSERVER
 * 
 * Sujeto observable que gestiona los observadores y emite notificaciones
 * cuando ocurren eventos en el sistema de transporte TransMilenio.
 * Permite suscripción por tipo de evento específico.
 */
@Component
public class TransportEventNotifier {

    private static final Logger log = LoggerFactory.getLogger(TransportEventNotifier.class);

    /**
     * Mapa de observadores por tipo de evento.
     */
    private final Map<TransportEvent.EventType, List<TransportObserver>> observers = new ConcurrentHashMap<>();

    /**
     * Lista de observadores globales que reciben todos los eventos.
     */
    private final List<TransportObserver> globalObservers = new CopyOnWriteArrayList<>();

    /**
     * Registra un observador para un tipo de evento específico.
     */
    public void subscribe(TransportEvent.EventType eventType, TransportObserver observer) {
        observers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(observer);
        log.info("Observador registrado para evento: {}", eventType);
    }

    /**
     * Registra un observador global que recibe todos los eventos.
     */
    public void subscribeAll(TransportObserver observer) {
        globalObservers.add(observer);
        log.info("Observador global registrado");
    }

    /**
     * Elimina un observador de un tipo de evento.
     */
    public void unsubscribe(TransportEvent.EventType eventType, TransportObserver observer) {
        List<TransportObserver> list = observers.get(eventType);
        if (list != null) {
            list.remove(observer);
        }
    }

    /**
     * Notifica a todos los observadores suscritos al tipo de evento.
     */
    public void notifyEvent(TransportEvent event) {
        log.info("Emitiendo evento: {}", event);

        // Notificar observadores específicos del tipo de evento
        List<TransportObserver> specific = observers.getOrDefault(event.getType(), new ArrayList<>());
        for (TransportObserver observer : specific) {
            observer.onEvent(event);
        }

        // Notificar observadores globales
        for (TransportObserver observer : globalObservers) {
            observer.onEvent(event);
        }
    }

    /**
     * Emite un evento de llegada de bus.
     */
    public void notifyBusArrival(String station, String route) {
        notifyEvent(new TransportEvent(
                TransportEvent.EventType.BUS_ARRIVAL,
                station, route,
                String.format("Bus de la ruta %s llegó a la estación %s", route, station)
        ));
    }

    /**
     * Emite un evento de retraso en ruta.
     */
    public void notifyRouteDelay(String station, String route, int delayMinutes) {
        notifyEvent(new TransportEvent(
                TransportEvent.EventType.ROUTE_DELAY,
                station, route,
                String.format("Ruta %s con retraso de %d minutos en estación %s", route, delayMinutes, station)
        ));
    }
}
