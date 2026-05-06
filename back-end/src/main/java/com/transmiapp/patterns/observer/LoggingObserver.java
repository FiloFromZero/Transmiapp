package com.transmiapp.patterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observador concreto: registra todos los eventos en el log del sistema.
 */
@Component
public class LoggingObserver implements TransportObserver {

    private static final Logger log = LoggerFactory.getLogger(LoggingObserver.class);

    @Override
    public void onEvent(TransportEvent event) {
        log.info("[LOG OBSERVER] Evento recibido: {}", event);
    }
}
