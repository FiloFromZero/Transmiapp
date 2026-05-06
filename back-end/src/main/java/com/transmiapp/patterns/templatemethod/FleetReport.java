package com.transmiapp.patterns.templatemethod;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reporte concreto: estadísticas de flota vehicular.
 */
@Component
public class FleetReport extends ReportGenerator {

    @Override
    protected String getReportName() {
        return "Reporte de Flota Vehicular";
    }

    @Override
    protected Map<String, Object> collectData() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalVehiculos", 1_420);
        data.put("articulados", 680);
        data.put("biarticulados", 240);
        data.put("alimentadores", 500);
        data.put("enOperacion", 1_280);
        data.put("enMantenimiento", 140);
        return data;
    }

    @Override
    protected String formatData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Datos de Flota ---\n");
        data.forEach((key, value) -> sb.append(String.format("  %-28s: %s\n", key, value)));
        return sb.toString();
    }

    @Override
    protected String buildSummary(Map<String, Object> data) {
        int total = (int) data.get("totalVehiculos");
        int enOp = (int) data.get("enOperacion");
        double pct = (enOp * 100.0) / total;
        return String.format("RESUMEN: %d de %d vehículos operativos (%.1f%%)", enOp, total, pct);
    }
}
