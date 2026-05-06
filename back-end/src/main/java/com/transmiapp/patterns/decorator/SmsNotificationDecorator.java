package com.transmiapp.patterns.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decorador que añade envío por SMS al servicio de notificaciones.
 */
public class SmsNotificationDecorator extends NotificationDecorator {

    private static final Logger log = LoggerFactory.getLogger(SmsNotificationDecorator.class);

    public SmsNotificationDecorator(NotificationService wrappedService) {
        super(wrappedService);
    }

    @Override
    public boolean sendNotification(String userId, String message) {
        boolean result = super.sendNotification(userId, message);
        sendSms(userId, message);
        return result;
    }

    private void sendSms(String userId, String message) {
        log.info("[SMS] Enviando SMS a usuario {}: {}", userId, message);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + SMS";
    }
}
