package com.transmiapp.patterns.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decorador que añade envío por Email al servicio de notificaciones.
 */
public class EmailNotificationDecorator extends NotificationDecorator {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationDecorator.class);

    public EmailNotificationDecorator(NotificationService wrappedService) {
        super(wrappedService);
    }

    @Override
    public boolean sendNotification(String userId, String message) {
        boolean result = super.sendNotification(userId, message);
        sendEmail(userId, message);
        return result;
    }

    private void sendEmail(String userId, String message) {
        log.info("[EMAIL] Enviando email a usuario {}: {}", userId, message);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Email";
    }
}
