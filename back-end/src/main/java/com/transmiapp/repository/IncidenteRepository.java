package com.transmiapp.repository;

import com.transmiapp.model.IncidenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidenteRepository extends JpaRepository<IncidenteEntity, String> {
    List<IncidenteEntity> findByActivoTrue();
}
