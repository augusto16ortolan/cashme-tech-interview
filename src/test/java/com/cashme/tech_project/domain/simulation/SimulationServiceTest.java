package com.cashme.tech_project.domain.simulation;

import com.cashme.tech_project.domain.client.ClientEntity;
import com.cashme.tech_project.domain.client.ClientService;
import com.cashme.tech_project.domain.exception.BusinessException;
import com.cashme.tech_project.domain.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    private static final UUID SIMULATION_ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();

    @Mock
    private SimulationRepository repository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private SimulationService service;

    @Test
    void shouldCreateSimulation() {
        final var simulationEntity = createMockSimulationEntity();
        final var clientEntity = createMockClientEntity();

        when(clientService.findClientById(CLIENT_ID)).thenReturn(clientEntity);
        when(repository.save(any())).thenReturn(simulationEntity);

        final var savedSimulation = service.createSimulation(simulationEntity);

        assertEquals(SIMULATION_ID, savedSimulation.getId());
        assertEquals(simulationEntity.getClient(), savedSimulation.getClient());

        verify(clientService).findClientById(CLIENT_ID);
        verify(repository).save(simulationEntity);

        verifyNoMoreInteractions(clientService, repository);
    }

    @Test
    void shouldUpdateSimulation() {
        final var simulationEntity = createMockSimulationEntity();
        final var clientEntity = createMockClientEntity();

        when(clientService.findClientById(CLIENT_ID)).thenReturn(clientEntity);
        when(repository.findById(SIMULATION_ID)).thenReturn(Optional.of(simulationEntity));
        when(repository.save(any())).thenReturn(simulationEntity);

        final var updatedSimulation = service.updateSimulation(simulationEntity);

        assertEquals(simulationEntity.getId(), updatedSimulation.getId());
        assertEquals(simulationEntity.getClient(), updatedSimulation.getClient());

        verify(clientService).findClientById(CLIENT_ID);
        verify(repository).findById(SIMULATION_ID);
        verify(repository).save(simulationEntity);

        verifyNoMoreInteractions(clientService, repository);
    }

    @Test
    void shouldNotUpdateSimulationBecauseNotFound() {
        final var simulationEntity = createMockSimulationEntity();

        when(repository.findById(SIMULATION_ID)).thenReturn(Optional.empty());

        final var exception = assertThrows(BusinessException.class, () -> {
            service.updateSimulation(simulationEntity);
        });

        final var expectedMessage = "Simulation with id " + SIMULATION_ID + " does not exist";
        final var actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(repository).findById(SIMULATION_ID);
        verifyNoMoreInteractions(repository, clientService);
    }

    @Test
    void shouldDeleteSimulation() {
        final var simulationEntity = createMockSimulationEntity();

        when(repository.findById(SIMULATION_ID)).thenReturn(Optional.of(simulationEntity));
        doNothing().when(repository).deleteById(SIMULATION_ID);

        service.deleteSimulation(SIMULATION_ID);

        verify(repository).findById(SIMULATION_ID);
        verify(repository).deleteById(SIMULATION_ID);

        verifyNoMoreInteractions(repository, clientService);
    }

    @Test
    void shouldNotDeleteSimulationBecauseNotFound() {
        when(repository.findById(SIMULATION_ID)).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> {
            service.deleteSimulation(SIMULATION_ID);
        });

        final var expectedMessage = "Simulation with id " + SIMULATION_ID + " does not exist";
        final var actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(repository).findById(SIMULATION_ID);
        verifyNoMoreInteractions(repository, clientService);
    }

    @Test
    void shouldFindSimulationById() {
        final var simulationEntity = createMockSimulationEntity();

        when(repository.findById(SIMULATION_ID)).thenReturn(Optional.of(simulationEntity));

        final var foundSimulation = service.findSimulationById(SIMULATION_ID);

        assertEquals(SIMULATION_ID, foundSimulation.getId());
        assertEquals(simulationEntity.getClient(), foundSimulation.getClient());

        verify(repository).findById(SIMULATION_ID);
        verifyNoMoreInteractions(repository, clientService);
    }

    @Test
    void shouldNotFindSimulationByIdBecauseNotFound() {
        when(repository.findById(SIMULATION_ID)).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> {
            service.findSimulationById(SIMULATION_ID);
        });

        final var expectedMessage = "Simulation with id " + SIMULATION_ID + " does not exist";
        final var actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(repository).findById(SIMULATION_ID);
        verifyNoMoreInteractions(repository, clientService);
    }

    @Test
    void shouldFindPagedSimulations() {
        final var simulations = Arrays.asList(SimulationEntity.builder().id(UUID.randomUUID()).build(), SimulationEntity.builder().id(UUID.randomUUID()).build(), SimulationEntity.builder().id(UUID.randomUUID()).build());

        Page<SimulationEntity> page = new PageImpl<>(simulations);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<SimulationEntity> result = service.findPagedSimulations(Pageable.unpaged());

        assertEquals(simulations.size(), result.getContent().size());
        assertEquals(simulations.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(simulations.get(1).getId(), result.getContent().get(1).getId());
        assertEquals(simulations.get(2).getId(), result.getContent().get(2).getId());

        verify(repository).findAll(page.getPageable());
    }

    private SimulationEntity createMockSimulationEntity() {
        return SimulationEntity.builder()
                .id(SIMULATION_ID)
                .client(createMockClientEntity())
                .build();
    }

    private ClientEntity createMockClientEntity() {
        return ClientEntity.builder().id(CLIENT_ID).name("Test Client").build();
    }
}
