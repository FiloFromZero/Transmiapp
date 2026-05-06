package com.transmiapp.patterns.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación base del servicio de notificaciones (push).
 */
public class BasicNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(BasicNotificationService.class);

    @Override
    public boolean sendNotification(String userId, String message) {
        log.info("[PUSH] Enviando a usuario {}: {}", userId, message);
        return true;
    }

    @Override
    public String getDescription() {
        return "Notificación Push";
    }
}
