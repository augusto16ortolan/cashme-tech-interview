package com.cashme.tech_project.domain.simulation;

import com.cashme.tech_project.domain.client.ClientService;
import com.cashme.tech_project.domain.exception.BusinessException;
import com.cashme.tech_project.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SimulationRepository repository;
    private final ClientService clientService;

    public SimulationEntity createSimulation(final SimulationEntity entityToCreate) {
        final var client = clientService.findClientById(entityToCreate.getClient().getId());

        entityToCreate.setClient(client);

        return repository.save(entityToCreate);
    }

    public SimulationEntity updateSimulation(final SimulationEntity entityToUpdate) {
        final var simulation = repository.findById(entityToUpdate.getId())
                .orElseThrow(() -> new BusinessException(format("Simulation with id %s does not exist", entityToUpdate.getId())));

        final var client = clientService.findClientById(entityToUpdate.getClient().getId());

        entityToUpdate.setClient(client);
        simulation.updateSimulation(entityToUpdate);

        return repository.save(simulation);
    }

    public void deleteSimulation(final UUID simulationId) {
        final var simulation = repository.findById(simulationId)
                .orElseThrow(() -> new NotFoundException(format("Simulation with id %s does not exist", simulationId)));

        repository.deleteById(simulation.getId());
    }

    public SimulationEntity findSimulationById(final UUID simulationId) {
        return repository.findById(simulationId)
                .orElseThrow(() -> new NotFoundException(format("Simulation with id %s does not exist", simulationId)));
    }

    public Page<SimulationEntity> findPagedSimulations(final Pageable pageable) {
        return repository.findAll(pageable);
    }
}
