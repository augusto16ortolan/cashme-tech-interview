package com.cashme.tech_project.domain.client.validator;

import com.cashme.tech_project.domain.client.ClientEntity;
import com.cashme.tech_project.domain.client.ClientRepository;
import com.cashme.tech_project.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.cashme.tech_project.domain.client.NationalIdentityType.CNPJ;
import static com.cashme.tech_project.domain.client.NationalIdentityType.CPF;

@Component
@RequiredArgsConstructor
public class ClientNationalIdentityValidator implements ClientValidator {

    private final ClientRepository repository;
    private final ValidatorNationalIdentityClient validatorClient;

    @Override
    public void validate(final ClientEntity entity, final boolean isCreate) {
        validateIfAlreadyExists(entity, isCreate);
        validateTypeAndLength(entity);
        validateIfIsValid(entity);
    }

    private void validateIfAlreadyExists(final ClientEntity entity, final boolean isCreate) {
        final var existingClient = repository.findByNationalIdentity(entity.getNationalIdentity());

        if (existingClient.isPresent()) {
            if (isCreate || !existingClient.get().getId().equals(entity.getId())) {
                throw new BusinessException("Client with national identity already exists");
            }
        }
    }

    private void validateTypeAndLength(final ClientEntity entity) {
        final var nationalIdentityType = entity.getNationalIdentityType();
        final var nationalIdentity = entity.getNationalIdentity();
        final var nationalIdentityLength = nationalIdentity.length();

        if (CNPJ.equals(nationalIdentityType)) {
            if (CNPJ.getSize() != nationalIdentityLength) {
                throw new BusinessException("National Identity is invalid");
            }
        } else {
            if (CPF.getSize() != nationalIdentityLength) {
                throw new BusinessException("National Identity is invalid");
            }
        }
    }

    private void validateIfIsValid(final ClientEntity entity) {
        final var nationalIdentityType = entity.getNationalIdentityType();
        final var nationalIdentity = entity.getNationalIdentity();

        final var isValid = validatorClient.isNationalIdentityValid(nationalIdentity, nationalIdentityType);

        if (!isValid) {
            throw new BusinessException("National Identity is invalid");
        }
    }
}
