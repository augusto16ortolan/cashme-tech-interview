package com.cashme.tech_project.api.simulation;

import com.cashme.tech_project.domain.PageFilter;
import com.cashme.tech_project.domain.simulation.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/simulation")
@Tag(name = "SimulationController", description = "Collection of simulation's endpoints")
public class SimulationController {

    private final SimulationMapper mapper;
    private final SimulationService service;

    @PostMapping
    @Operation(summary = "Create a new simulation", description = "Creates a new simulation based on the provided request.")
    public ResponseEntity<SimulationResponse> createSimulation(@Valid @RequestBody final SimulationRequest request) {
        final var simulationEntity = service.createSimulation(mapper.toEntity(request));
        final var response = mapper.toResponse(simulationEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{simulationId}")
    @Operation(summary = "Update a simulation", description = "Updates an existing simulation identified by simulationId.")
    public ResponseEntity<SimulationResponse> updateSimulation(@PathVariable final UUID simulationId,
                                                       @Valid @RequestBody final SimulationRequest request) {
        final var simulationEntity = service.updateSimulation(mapper.toUpdateEntity(simulationId, request));
        final var response = mapper.toResponse(simulationEntity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{simulationId}")
    @Operation(summary = "Delete a simulation", description = "Deletes an existing simulation identified by simulationId.")
    public ResponseEntity<Void> deleteSimulation(@PathVariable final UUID simulationId) {
        service.deleteSimulation(simulationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{simulationId}")
    @Operation(summary = "Get a simulation by ID", description = "Retrieves a simulation based on the provided simulationId.")
    public ResponseEntity<SimulationResponse> getSimulationById(@PathVariable final UUID simulationId) {
        final var simulationEntity = service.findSimulationById(simulationId);
        final var response = mapper.toResponse(simulationEntity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @Operation(summary = "Get paged simulations", description = "Retrieves a paginated list of all simulations.")
    public Page<SimulationResponse> getPagedSimulations(final PageFilter pageFilter) {
        final var simulationPage = service.findPagedSimulations(pageFilter.toPageable());
        return simulationPage.map(mapper::toResponse);
    }
}
