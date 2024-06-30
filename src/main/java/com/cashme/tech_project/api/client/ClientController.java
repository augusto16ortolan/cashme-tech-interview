package com.cashme.tech_project.api.client;

import com.cashme.tech_project.domain.PageFilter;
import com.cashme.tech_project.domain.client.ClientService;
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
@RequestMapping("/v1/client")
@Tag(name = "ClientController", description = "Collection of client's endpoints")
public class ClientController {

    private final ClientMapper mapper;
    private final ClientService service;

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client based on the provided request.")
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody final ClientRequest request) {
        final var clientEntity = service.createClient(mapper.toEntity(request));
        final var response = mapper.toResponse(clientEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{clientId}")
    @Operation(summary = "Update a client", description = "Updates an existing client identified by clientId.")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable final UUID clientId,
                                                       @Valid @RequestBody final ClientRequest request) {
        final var clientEntity = service.updateClient(mapper.toUpdateEntity(clientId, request));
        final var response = mapper.toResponse(clientEntity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Delete a client", description = "Deletes an existing client identified by clientId.")
    public ResponseEntity<Void> deleteClient(@PathVariable final UUID clientId) {
        service.deleteClient(clientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Get a client by ID", description = "Retrieves a client based on the provided clientId.")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable final UUID clientId) {
        final var clientEntity = service.findClientById(clientId);
        final var response = mapper.toResponse(clientEntity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @Operation(summary = "Get paged clients", description = "Retrieves a paginated list of all clients.")
    public Page<ClientResponse> getPagedClients(final PageFilter pageFilter) {
        final var clientPage = service.findPagedClients(pageFilter.toPageable());
        return clientPage.map(mapper::toResponse);
    }
}
