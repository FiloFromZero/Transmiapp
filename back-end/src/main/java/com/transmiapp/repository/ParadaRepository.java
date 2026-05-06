package com.transmiapp.repository;

import com.transmiapp.model.ParadaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ParadaRepository extends JpaRepository<ParadaEntity, String> {

    @Query(value = "SELECT p.* FROM paradas p " +
            "JOIN ruta_paradas rp ON p.id = rp.parada_id " +
            "WHERE rp.ruta_id = :rutaId ORDER BY rp.orden", nativeQuery = true)
    List<ParadaEntity> findByRutaIdOrdered(String rutaId);
}
