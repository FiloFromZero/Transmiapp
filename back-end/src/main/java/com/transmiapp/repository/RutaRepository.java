package com.transmiapp.repository;

import com.transmiapp.model.RutaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RutaRepository extends JpaRepository<RutaEntity, String> {
    Optional<RutaEntity> findByCodigo(String codigo);
}
