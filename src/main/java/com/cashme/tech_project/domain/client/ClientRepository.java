package com.cashme.tech_project.domain.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {

    @Query("SELECT c FROM ClientEntity c WHERE c.nationalIdentity = :nationalIdentity")
    Optional<ClientEntity> findByNationalIdentity(@Param("nationalIdentity") final String nationalIdentity);
}
