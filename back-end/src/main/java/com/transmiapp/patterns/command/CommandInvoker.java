package com.transmiapp.patterns.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * PATRÓN 10: COMMAND - Invoker
 * 
 * Gestiona la ejecución, historial y deshacer de comandos del sistema.
 */
@Component
public class CommandInvoker {

    private static final Logger log = LoggerFactory.getLogger(CommandInvoker.class);

    private final Stack<TransitCommand> history = new Stack<>();

    /**
     * Ejecuta un comando y lo registra en el historial.
     */
    public void executeCommand(TransitCommand command) {
        command.execute();
        history.push(command);
        log.info("Comando ejecutado: {}", command.getDescription());
    }

    /**
     * Deshace el último comando ejecutado.
     */
    public void undoLastCommand() {
        if (!history.isEmpty()) {
            TransitCommand lastCommand = history.pop();
            lastCommand.undo();
            log.info("Comando deshecho: {}", lastCommand.getDescription());
        } else {
            log.warn("No hay comandos en el historial para deshacer");
        }
    }

    /**
     * Retorna el historial de comandos ejecutados.
     */
    public List<String> getHistory() {
        List<String> descriptions = new ArrayList<>();
        for (TransitCommand cmd : history) {
            descriptions.add(cmd.getDescription());
        }
        return descriptions;
    }

    /**
     * Limpia el historial de comandos.
     */
    public void clearHistory() {
        history.clear();
    }
}
