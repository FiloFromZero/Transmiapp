package com.transmiapp.patterns.decorator;

/**
 * PATRÓN 7: DECORATOR
 * 
 * Interfaz base del servicio de notificaciones.
 */
public interface NotificationService {

    /**
     * Envía una notificación al usuario.
     *
     * @param userId  ID del usuario destino
     * @param message Mensaje a enviar
     * @return true si la notificación se envió correctamente
     */
    boolean sendNotification(String userId, String message);

    /**
     * Retorna la descripción del servicio y sus decoradores activos.
     */
    String getDescription();
}
