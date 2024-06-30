package com.cashme.tech_project.api.simulation;

import com.cashme.tech_project.api.client.ClientMapper;
import com.cashme.tech_project.domain.client.ClientEntity;
import com.cashme.tech_project.domain.simulation.SimulationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SimulationMapper {

    private final ClientMapper clientMapper;

    public SimulationEntity toEntity(final SimulationRequest request) {
        return SimulationEntity.builder()
                .client(ClientEntity.builder().id(request.getClientId()).build())
                .financingMonths(request.getFinancingMonths())
                .simulationDate(request.getSimulationDate())
                .requestedValue(request.getRequestedValue())
                .warrantyValue(request.getWarrantyValue())
                .monthlyInterestRate(request.getMonthlyInterestRate())
                .build();
    }

    public SimulationEntity toUpdateEntity(final UUID simulationId, final SimulationRequest request) {
        final var simulation = toEntity(request);
        return simulation.withId(simulationId);
    }

    public SimulationResponse toResponse(final SimulationEntity entity) {
        return SimulationResponse.builder()
                .id(entity.getId())
                .simulationDate(entity.getSimulationDate())
                .financingMonths(entity.getFinancingMonths())
                .monthlyInterestRate(entity.getMonthlyInterestRate())
                .requestedValue(entity.getRequestedValue())
                .warrantyValue(entity.getWarrantyValue())
                .client(clientMapper.toResponse(entity.getClient()))
                .build();
    }
}
