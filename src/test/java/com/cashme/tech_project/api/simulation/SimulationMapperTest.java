package com.cashme.tech_project.api.simulation;

import com.cashme.tech_project.api.client.ClientMapper;
import com.cashme.tech_project.api.client.ClientResponse;
import com.cashme.tech_project.domain.client.ClientEntity;
import com.cashme.tech_project.domain.simulation.SimulationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimulationMapperTest {

    private static final UUID CLIENT_ID = UUID.randomUUID();

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private SimulationMapper simulationMapper;

    @Test
    public void shouldMapSimulationRequestToSimulationEntity() {
        final var request = SimulationRequest.builder()
                .simulationDate(LocalDateTime.now())
                .requestedValue(BigDecimal.valueOf(1000))
                .warrantyValue(BigDecimal.valueOf(500))
                .financingMonths(12)
                .monthlyInterestRate(BigDecimal.valueOf(0.01))
                .clientId(CLIENT_ID)
                .build();

        final var simulationEntity = simulationMapper.toEntity(request);

        assertEquals(request.getSimulationDate(), simulationEntity.getSimulationDate());
        assertEquals(request.getRequestedValue(), simulationEntity.getRequestedValue());
        assertEquals(request.getWarrantyValue(), simulationEntity.getWarrantyValue());
        assertEquals(request.getFinancingMonths(), simulationEntity.getFinancingMonths());
        assertEquals(request.getMonthlyInterestRate(), simulationEntity.getMonthlyInterestRate());
        assertEquals(CLIENT_ID, simulationEntity.getClient().getId());
    }

    @Test
    public void shouldMapSimulationEntityToSimulationResponse() {
        final var entity = SimulationEntity.builder()
                .id(UUID.randomUUID())
                .simulationDate(LocalDateTime.now())
                .requestedValue(BigDecimal.valueOf(1000))
                .warrantyValue(BigDecimal.valueOf(500))
                .financingMonths(12)
                .monthlyInterestRate(BigDecimal.valueOf(0.01))
                .client(ClientEntity.builder().id(CLIENT_ID).build())
                .build();

        when(clientMapper.toResponse(entity.getClient())).thenReturn(ClientResponse.builder().id(CLIENT_ID).build());

        final var response = simulationMapper.toResponse(entity);

        assertEquals(entity.getId(), response.getId());
        assertEquals(entity.getSimulationDate(), response.getSimulationDate());
        assertEquals(entity.getRequestedValue(), response.getRequestedValue());
        assertEquals(entity.getWarrantyValue(), response.getWarrantyValue());
        assertEquals(entity.getFinancingMonths(), response.getFinancingMonths());
        assertEquals(entity.getMonthlyInterestRate(), response.getMonthlyInterestRate());
        assertEquals(entity.getClient().getId(), response.getClient().getId());
    }

    @Test
    public void shouldMapSimulationRequestToUpdateEntity() {
        final var simulationId = UUID.randomUUID();
        final var request = SimulationRequest.builder()
                .simulationDate(LocalDateTime.now())
                .requestedValue(BigDecimal.valueOf(1500))
                .warrantyValue(BigDecimal.valueOf(700))
                .financingMonths(24)
                .monthlyInterestRate(BigDecimal.valueOf(0.015))
                .clientId(CLIENT_ID)
                .build();

        final var updatedEntity = simulationMapper.toUpdateEntity(simulationId, request);

        assertEquals(simulationId, updatedEntity.getId());
        assertEquals(request.getSimulationDate(), updatedEntity.getSimulationDate());
        assertEquals(request.getRequestedValue(), updatedEntity.getRequestedValue());
        assertEquals(request.getWarrantyValue(), updatedEntity.getWarrantyValue());
        assertEquals(request.getFinancingMonths(), updatedEntity.getFinancingMonths());
        assertEquals(request.getMonthlyInterestRate(), updatedEntity.getMonthlyInterestRate());
    }
}
