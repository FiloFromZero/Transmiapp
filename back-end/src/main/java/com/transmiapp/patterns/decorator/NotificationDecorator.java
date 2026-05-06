package com.transmiapp.patterns.decorator;

/**
 * PATRÓN 7: DECORATOR
 * 
 * Clase abstracta base para los decoradores del servicio de notificaciones.
 * Envuelve una instancia de NotificationService y delega las llamadas.
 */
public abstract class NotificationDecorator implements NotificationService {

    protected final NotificationService wrappedService;

    public NotificationDecorator(NotificationService wrappedService) {
        this.wrappedService = wrappedService;
    }

    @Override
    public boolean sendNotification(String userId, String message) {
        return wrappedService.sendNotification(userId, message);
    }

    @Override
    public String getDescription() {
        return wrappedService.getDescription();
    }
}
