package com.transmiapp.patterns.command;

/**
 * PATRÓN 10: COMMAND
 * 
 * Interfaz que encapsula una operación del sistema de transporte
 * como un objeto, permitiendo parametrizar, encolar y deshacer operaciones.
 */
public interface TransitCommand {

    /**
     * Ejecuta la operación encapsulada.
     */
    void execute();

    /**
     * Deshace la operación ejecutada (si aplica).
     */
    void undo();

    /**
     * Retorna la descripción de la operación.
     */
    String getDescription();
}
