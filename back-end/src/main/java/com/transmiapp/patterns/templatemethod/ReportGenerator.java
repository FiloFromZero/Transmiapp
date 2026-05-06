package com.transmiapp.patterns.templatemethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * PATRÓN 9: TEMPLATE METHOD
 * 
 * Clase abstracta que define el esqueleto del algoritmo de generación
 * de reportes. Los pasos concretos se implementan en las subclases.
 */
public abstract class ReportGenerator {

    protected static final Logger log = LoggerFactory.getLogger(ReportGenerator.class);

    /**
     * Método template que define el algoritmo de generación del reporte.
     * El orden de los pasos es fijo; las subclases implementan los pasos concretos.
     */
    public final String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append(buildHeader());
        report.append("\n");

        Map<String, Object> data = collectData();
        report.append(formatData(data));
        report.append("\n");

        report.append(buildSummary(data));
        report.append("\n");

        report.append(buildFooter());

        log.info("Reporte generado: {}", getReportName());
        return report.toString();
    }

    /**
     * Construye el encabezado del reporte.
     */
    protected String buildHeader() {
        return String.format("=== %s ===\nFecha: %s\n",
                getReportName(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * Construye el pie del reporte.
     */
    protected String buildFooter() {
        return "=== Fin del Reporte ===\nSistema TransMilenio - Bogotá";
    }

    // --- Métodos abstractos que las subclases deben implementar ---

    /**
     * Retorna el nombre del tipo de reporte.
     */
    protected abstract String getReportName();

    /**
     * Recopila los datos necesarios para el reporte.
     */
    protected abstract Map<String, Object> collectData();

    /**
     * Formatea los datos recopilados en texto legible.
     */
    protected abstract String formatData(Map<String, Object> data);

    /**
     * Construye el resumen al final del reporte.
     */
    protected abstract String buildSummary(Map<String, Object> data);
}
