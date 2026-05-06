package com.transmiapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculos")
public class VehiculoEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "capacidad_total", nullable = false)
    private int capacidadTotal;

    @Column(name = "ruta_id")
    private String rutaId;

    @Column(nullable = false)
    private String estado;

    private Double latitud;
    private Double longitud;
    private Integer rumbo;

    @Column(name = "velocidad_kmh")
    private Integer velocidadKmh;

    @Column(name = "ocupacion_actual")
    private Integer ocupacionActual;

    @Column(name = "proxima_parada_id")
    private String proximaParadaId;

    @Column(name = "minutos_proxima")
    private Double minutosProxima;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Getters y Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public void setCapacidadTotal(int capacidadTotal) { this.capacidadTotal = capacidadTotal; }
    public String getRutaId() { return rutaId; }
    public void setRutaId(String rutaId) { this.rutaId = rutaId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public Integer getRumbo() { return rumbo; }
    public void setRumbo(Integer rumbo) { this.rumbo = rumbo; }
    public Integer getVelocidadKmh() { return velocidadKmh; }
    public void setVelocidadKmh(Integer velocidadKmh) { this.velocidadKmh = velocidadKmh; }
    public Integer getOcupacionActual() { return ocupacionActual; }
    public void setOcupacionActual(Integer ocupacionActual) { this.ocupacionActual = ocupacionActual; }
    public String getProximaParadaId() { return proximaParadaId; }
    public void setProximaParadaId(String proximaParadaId) { this.proximaParadaId = proximaParadaId; }
    public Double getMinutosProxima() { return minutosProxima; }
    public void setMinutosProxima(Double minutosProxima) { this.minutosProxima = minutosProxima; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
