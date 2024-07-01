package com.cashme.tech_project.domain.simulation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SimulationRepository extends JpaRepository<SimulationEntity, UUID> {

    @Query("SELECT COUNT(s) > 0 FROM SimulationEntity s WHERE s.client.id = :clientId")
    boolean existsSimulationByClientId(final UUID clientId);
}
