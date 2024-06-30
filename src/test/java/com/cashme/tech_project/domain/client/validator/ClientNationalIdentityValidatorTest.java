package com.cashme.tech_project.domain.client.validator;

import com.cashme.tech_project.domain.client.ClientEntity;
import com.cashme.tech_project.domain.client.ClientRepository;
import com.cashme.tech_project.domain.client.NationalIdentityType;
import com.cashme.tech_project.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientNationalIdentityValidatorTest {

    private static final UUID CLIENT_ID = UUID.randomUUID();

    @Mock
    private ClientRepository repository;

    @Mock
    private ValidatorNationalIdentityClient validatorClient;

    @InjectMocks
    private ClientNationalIdentityValidator validator;

    @Test
    public void shouldThrowExceptionWhenClientAlreadyExistsOnCreate() {
        final var entity = ClientEntity.builder()
                .nationalIdentity("12345678901")
                .nationalIdentityType(NationalIdentityType.CPF)
                .build();

        when(repository.findByNationalIdentity(anyString())).thenReturn(Optional.of(entity));

        final var thrown = assertThrows(BusinessException.class, () -> validator.validate(entity, true));
        assertEquals("Client with national identity already exists", thrown.getMessage());
    }

    @Test
    public void shouldNotThrowExceptionWhenUpdatingSameClient() {
        final var existingEntity = ClientEntity.builder()
                .id(CLIENT_ID)
                .nationalIdentity("12345678901")
                .nationalIdentityType(NationalIdentityType.CPF)
                .build();

        final var entity = ClientEntity.builder()
                .id(CLIENT_ID)
                .nationalIdentity("12345678901")
                .nationalIdentityType(NationalIdentityType.CPF)
                .build();

        when(repository.findByNationalIdentity(anyString())).thenReturn(Optional.of(existingEntity));
        when(validatorClient.isNationalIdentityValid(eq("12345678901"), eq(NationalIdentityType.CPF)))
                .thenReturn(true);

        validator.validate(entity, false);
    }

    @Test
    public void shouldThrowExceptionWhenNationalIdentityLengthIsInvalidForCNPJ() {
        final var entity = ClientEntity.builder()
                .nationalIdentity("12345678901")
                .nationalIdentityType(NationalIdentityType.CNPJ)
                .build();

        final var thrown = assertThrows(BusinessException.class, () -> validator.validate(entity, true));
        assertEquals("National Identity is invalid", thrown.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNationalIdentityLengthIsInvalidForCPF() {
        final var entity = ClientEntity.builder()
                .nationalIdentity("1234567890123")
                .nationalIdentityType(NationalIdentityType.CPF)
                .build();

        final var thrown = assertThrows(BusinessException.class, () -> validator.validate(entity, true));
        assertEquals("National Identity is invalid", thrown.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNationalIdentityIsInvalidAccordingToValidator() {
        final var entity = ClientEntity.builder()
                .nationalIdentity("12345678901")
                .nationalIdentityType(NationalIdentityType.CPF)
                .build();

        when(validatorClient.isNationalIdentityValid(eq("12345678901"), eq(NationalIdentityType.CPF)))
                .thenReturn(false);

        final var thrown = assertThrows(BusinessException.class, () -> validator.validate(entity, true));
        assertEquals("National Identity is invalid", thrown.getMessage());
    }

    @Test
    public void shouldNotThrowExceptionForValidNationalIdentity() {
        final var entity = ClientEntity.builder()
                .nationalIdentity("12345678901")
                .nationalIdentityType(NationalIdentityType.CPF)
                .build();

        when(repository.findByNationalIdentity(anyString())).thenReturn(Optional.empty());
        when(validatorClient.isNationalIdentityValid(eq("12345678901"), eq(NationalIdentityType.CPF)))
                .thenReturn(true);

        validator.validate(entity, true);
    }
}
