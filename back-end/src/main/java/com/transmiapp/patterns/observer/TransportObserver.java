package com.transmiapp.patterns.observer;

/**
 * PATRÓN 4: OBSERVER
 * 
 * Interfaz para los observadores que desean recibir notificaciones
 * sobre eventos del sistema de transporte (llegada de buses, alertas, etc.).
 */
public interface TransportObserver {

    /**
     * Método invocado cuando ocurre un evento en el sistema.
     *
     * @param event Evento del sistema de transporte
     */
    void onEvent(TransportEvent event);
}
