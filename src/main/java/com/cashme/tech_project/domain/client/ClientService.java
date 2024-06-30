package com.cashme.tech_project.domain.client;

import com.cashme.tech_project.domain.client.validator.ClientValidatorFactory;
import com.cashme.tech_project.domain.exception.BusinessException;
import com.cashme.tech_project.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ClientValidatorFactory validatorFactory;

    public ClientEntity createClient(final ClientEntity clientEntity) {
        validateClient(clientEntity, TRUE);
        return repository.save(clientEntity);
    }

    private void validateClient(final ClientEntity clientEntity, final boolean isCreate) {
        validatorFactory.getClientValidators().forEach(validator -> validator.validate(clientEntity, isCreate));
    }

    public ClientEntity updateClient(final ClientEntity clientEntity) {
        final var existingClient = repository.findById(clientEntity.getId())
                .orElseThrow(() -> new BusinessException(format("Client with id %s does not exist", clientEntity.getId())));

        validateClient(clientEntity, FALSE);

        existingClient.setName(clientEntity.getName());
        existingClient.setNationalIdentity(clientEntity.getNationalIdentity());
        existingClient.setNationalIdentityType(clientEntity.getNationalIdentityType());

        if (clientEntity.getAddress() != null) {
            existingClient.updateAddress(clientEntity.getAddress());
        }

        return repository.save(existingClient);
    }

    public void deleteClient(final UUID clientId) {
        final var existingClient = repository.findById(clientId)
                .orElseThrow(() -> new BusinessException(format("Client with id %s does not exist", clientId)));

        repository.deleteById(existingClient.getId());
    }

    public ClientEntity findClientById(final UUID clientId) {
        return repository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(format("Client with id %s does not exist", clientId)));
    }

    public Page<ClientEntity> findPagedClients(final Pageable pageable) {
        return repository.findAll(pageable);
    }
}
