package com.transmiapp.controller;

import com.transmiapp.model.*;
import com.transmiapp.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller REST que expone los datos del sistema TransMilenio
 * desde la base de datos PostgreSQL.
 */
@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class DataController {

    private final RutaRepository rutaRepo;
    private final VehiculoRepository vehiculoRepo;
    private final IncidenteRepository incidenteRepo;
    private final ParadaRepository paradaRepo;

    public DataController(RutaRepository rutaRepo,
                           VehiculoRepository vehiculoRepo,
                           IncidenteRepository incidenteRepo,
                           ParadaRepository paradaRepo) {
        this.rutaRepo = rutaRepo;
        this.vehiculoRepo = vehiculoRepo;
        this.incidenteRepo = incidenteRepo;
        this.paradaRepo = paradaRepo;
    }

    // ── Rutas ────────────────────────────────────────────────

    @GetMapping("/rutas")
    public ResponseEntity<List<Map<String, Object>>> getRutas() {
        List<RutaEntity> rutas = rutaRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (RutaEntity r : rutas) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", r.getId());
            map.put("codigo", r.getCodigo());
            map.put("nombre", r.getNombre());
            map.put("tipo", r.getTipo());
            map.put("color", r.getColor());
            map.put("origen", r.getOrigen());
            map.put("destino", r.getDestino());
            map.put("estado", r.getEstado());
            map.put("frecuenciaMinutos", r.getFrecuenciaMinutos());

            // Incluir paradas ordenadas
            List<ParadaEntity> paradas = paradaRepo.findByRutaIdOrdered(r.getId());
            List<Map<String, Object>> paradasList = new ArrayList<>();
            for (ParadaEntity p : paradas) {
                paradasList.add(Map.of(
                        "id", p.getId(),
                        "nombre", p.getNombre(),
                        "coordenada", Map.of("lat", p.getLatitud(), "lng", p.getLongitud()),
                        "esTroncal", p.getEsTroncal()
                ));
            }
            map.put("paradas", paradasList);
            map.put("ultimaActualizacion", r.getUpdatedAt());
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rutas/{id}")
    public ResponseEntity<?> getRutaById(@PathVariable String id) {
        return rutaRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Vehículos ────────────────────────────────────────────

    @GetMapping("/vehiculos")
    public ResponseEntity<List<Map<String, Object>>> getVehiculos() {
        List<VehiculoEntity> vehiculos = vehiculoRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (VehiculoEntity v : vehiculos) {
            result.add(mapVehiculo(v));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vehiculos/ruta/{rutaId}")
    public ResponseEntity<List<Map<String, Object>>> getVehiculosByRuta(@PathVariable String rutaId) {
        List<VehiculoEntity> vehiculos = vehiculoRepo.findByRutaId(rutaId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (VehiculoEntity v : vehiculos) {
            result.add(mapVehiculo(v));
        }
        return ResponseEntity.ok(result);
    }

    private Map<String, Object> mapVehiculo(VehiculoEntity v) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", v.getId());
        map.put("placa", v.getPlaca());
        map.put("rutaId", v.getRutaId());
        map.put("coordenada", Map.of("lat", v.getLatitud(), "lng", v.getLongitud()));
        map.put("rumbo", v.getRumbo());
        map.put("velocidadKmh", v.getVelocidadKmh());
        map.put("capacidadTotal", v.getCapacidadTotal());
        map.put("ocupacionActual", v.getOcupacionActual());
        map.put("estado", v.getEstado());
        map.put("proximaParadaId", v.getProximaParadaId());
        map.put("minutosParaProximaParada", v.getMinutosProxima());
        map.put("ultimaActualizacion", v.getUpdatedAt());
        return map;
    }

    // ── Incidentes ───────────────────────────────────────────

    @GetMapping("/incidentes")
    public ResponseEntity<List<Map<String, Object>>> getIncidentes() {
        List<IncidenteEntity> incidentes = incidenteRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (IncidenteEntity i : incidentes) {
            result.add(mapIncidente(i));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/incidentes/activos")
    public ResponseEntity<List<Map<String, Object>>> getIncidentesActivos() {
        List<IncidenteEntity> incidentes = incidenteRepo.findByActivoTrue();
        List<Map<String, Object>> result = new ArrayList<>();
        for (IncidenteEntity i : incidentes) {
            result.add(mapIncidente(i));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/incidentes")
    public ResponseEntity<IncidenteEntity> crearIncidente(@RequestBody IncidenteEntity incidente) {
        incidente.setId("inc-" + System.currentTimeMillis());
        incidente.setFechaReporte(LocalDateTime.now());
        incidente.setActivo(true);
        if (incidente.getReportadoPor() == null) incidente.setReportadoPor("usuario");
        return ResponseEntity.ok(incidenteRepo.save(incidente));
    }

    @PatchMapping("/incidentes/{id}/resolver")
    public ResponseEntity<?> resolverIncidente(@PathVariable String id) {
        return incidenteRepo.findById(id).map(inc -> {
            inc.setActivo(false);
            inc.setFechaResolucion(LocalDateTime.now());
            return ResponseEntity.ok(incidenteRepo.save(inc));
        }).orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> mapIncidente(IncidenteEntity i) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", i.getId());
        map.put("tipo", i.getTipo());
        map.put("severidad", i.getSeveridad());
        map.put("titulo", i.getTitulo());
        map.put("descripcion", i.getDescripcion());
        map.put("coordenada", Map.of("lat", i.getLatitud(), "lng", i.getLongitud()));
        map.put("reportadoPor", i.getReportadoPor());
        map.put("activo", i.getActivo());
        map.put("fechaReporte", i.getFechaReporte());
        map.put("fechaResolucion", i.getFechaResolucion());
        return map;
    }

    // ── Paradas ──────────────────────────────────────────────

    @GetMapping("/paradas")
    public ResponseEntity<List<ParadaEntity>> getParadas() {
        return ResponseEntity.ok(paradaRepo.findAll());
    }

    // ── Health check ─────────────────────────────────────────

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "database", "connected",
                "timestamp", LocalDateTime.now(),
                "rutas", rutaRepo.count(),
                "vehiculos", vehiculoRepo.count(),
                "incidentes", incidenteRepo.count()
        ));
    }
}
