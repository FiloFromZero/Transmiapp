package com.transmiapp.patterns.templatemethod;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reporte concreto: estadísticas de pasajeros diarios.
 */
@Component
public class PassengerReport extends ReportGenerator {

    @Override
    protected String getReportName() {
        return "Reporte de Pasajeros Diarios";
    }

    @Override
    protected Map<String, Object> collectData() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalPasajeros", 2_450_000);
        data.put("horasPico", "6:00-8:30 / 17:00-19:30");
        data.put("estacionMasConcurrida", "Portal Norte");
        data.put("rutaMasUsada", "B23 - Calle 80");
        data.put("promedioEspera", "4.5 minutos");
        return data;
    }

    @Override
    protected String formatData(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Datos de Pasajeros ---\n");
        data.forEach((key, value) -> sb.append(String.format("  %-28s: %s\n", key, value)));
        return sb.toString();
    }

    @Override
    protected String buildSummary(Map<String, Object> data) {
        return String.format("RESUMEN: Se transportaron %s pasajeros. Estación más concurrida: %s",
                data.get("totalPasajeros"), data.get("estacionMasConcurrida"));
    }
}
