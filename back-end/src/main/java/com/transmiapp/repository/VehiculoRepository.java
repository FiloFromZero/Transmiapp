package com.transmiapp.repository;

import com.transmiapp.model.VehiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<VehiculoEntity, String> {
    List<VehiculoEntity> findByRutaId(String rutaId);
    List<VehiculoEntity> findByEstado(String estado);
}
