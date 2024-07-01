package com.cashme.tech_project.domain.client;

import com.cashme.tech_project.domain.address.AddressEntity;
import com.cashme.tech_project.domain.address.AddressType;
import com.cashme.tech_project.domain.client.validator.ClientNationalIdentityValidator;
import com.cashme.tech_project.domain.client.validator.ClientValidatorFactory;
import com.cashme.tech_project.domain.exception.BusinessException;
import com.cashme.tech_project.domain.exception.NotFoundException;
import com.cashme.tech_project.domain.simulation.SimulationRepository;
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
import java.util.Set;
import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    private static final UUID CLIENT_ID = UUID.randomUUID();

    @Mock
    private ClientRepository repository;

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private ClientValidatorFactory validatorFactory;

    @Mock
    private ClientNationalIdentityValidator validator;

    @InjectMocks
    private ClientService service;

    @Test
    void shouldCreateClient() {
        when(validatorFactory.getClientValidators()).thenReturn(Set.of(validator));
        doNothing().when(validator).validate(any(), anyBoolean());

        final var address = AddressEntity.builder().addressType(AddressType.STREET).address("Address").build();
        final var clientEntity = ClientEntity.builder().name("Name").nationalIdentityType(NationalIdentityType.CPF).nationalIdentity("12345678900").address(address).build();
        when(repository.save(any())).thenReturn(clientEntity.withId(CLIENT_ID));

        final var savedClient = service.createClient(clientEntity);

        assertEquals(CLIENT_ID, savedClient.getId());
        assertEquals(clientEntity.getName(), savedClient.getName());
        assertEquals(clientEntity.getNationalIdentityType(), savedClient.getNationalIdentityType());
        assertEquals(clientEntity.getNationalIdentity(), savedClient.getNationalIdentity());

        verify(validatorFactory).getClientValidators();
        verify(validator).validate(clientEntity, TRUE);
        verify(repository).save(clientEntity);

        verifyNoMoreInteractions(validatorFactory, validator, repository);
    }

    @Test
    void shouldUpdateClient() {
        when(validatorFactory.getClientValidators()).thenReturn(Set.of(validator));
        doNothing().when(validator).validate(any(), anyBoolean());

        final var clientEntity = ClientEntity.builder().id(CLIENT_ID).name("Name").nationalIdentityType(NationalIdentityType.CPF).nationalIdentity("12345678900").build();

        when(repository.findById(any())).thenReturn(Optional.of(clientEntity));

        final var address = AddressEntity.builder().addressType(AddressType.STREET).address("Address").build();
        final var updatedClientEntity = ClientEntity.builder().id(CLIENT_ID).name("Name Updated").nationalIdentityType(NationalIdentityType.CPF).nationalIdentity("12345678900").address(address).build();

        when(repository.save(any())).thenReturn(updatedClientEntity);

        final var savedClient = service.updateClient(updatedClientEntity);

        assertEquals(updatedClientEntity.getId(), savedClient.getId());
        assertEquals(updatedClientEntity.getName(), savedClient.getName());
        assertEquals(updatedClientEntity.getNationalIdentityType(), savedClient.getNationalIdentityType());
        assertEquals(updatedClientEntity.getNationalIdentity(), savedClient.getNationalIdentity());

        verify(validatorFactory).getClientValidators();
        verify(validator).validate(updatedClientEntity, FALSE);
        verify(repository).save(updatedClientEntity);
        verify(repository).findById(CLIENT_ID);

        verifyNoMoreInteractions(validatorFactory, validator, repository);
    }

    @Test
    void shouldNotUpdateClientBecauseDoesNotExist() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        final var updatedClientEntity = ClientEntity.builder().id(CLIENT_ID).name("Name Updated").nationalIdentityType(NationalIdentityType.CPF).nationalIdentity("12345678900").build();

        final var exception = assertThrows(BusinessException.class, () -> {
            service.updateClient(updatedClientEntity);
        });

        final var expectedMessage = "Client with id " + CLIENT_ID + " does not exist";
        final var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(repository).findById(CLIENT_ID);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteClient() {
        final var clientEntity = ClientEntity.builder().id(CLIENT_ID).name("Name").nationalIdentityType(NationalIdentityType.CPF).nationalIdentity("12345678900").build();

        when(repository.findById(any())).thenReturn(Optional.of(clientEntity));

        when(simulationRepository.existsSimulationByClientId(any())).thenReturn(false);

        doNothing().when(repository).deleteById(any());

        service.deleteClient(CLIENT_ID);

        verify(repository).findById(CLIENT_ID);
        verify(repository).deleteById(CLIENT_ID);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldNotDeleteClientBecauseDoesNotExist() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        final var exception = assertThrows(BusinessException.class, () -> {
            service.deleteClient(CLIENT_ID);
        });

        final var expectedMessage = "Client with id " + CLIENT_ID + " does not exist";
        final var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(repository).findById(CLIENT_ID);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void shouldNotDeleteClientBecauseExistsSimulations() {
        final var clientEntity = ClientEntity.builder().id(CLIENT_ID).name("Name").nationalIdentityType(NationalIdentityType.CPF).nationalIdentity("12345678900").build();
        when(repository.findById(CLIENT_ID)).thenReturn(Optional.of(clientEntity));

        when(simulationRepository.existsSimulationByClientId(CLIENT_ID)).thenReturn(true);

        final var exception = assertThrows(BusinessException.class, () -> {
            service.deleteClient(CLIENT_ID);
        });

        final var expectedMessage = "There are simulations linked to the client with id " + CLIENT_ID;
        final var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(repository).findById(CLIENT_ID);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void shouldFindClientById() {
        final var clientEntity = ClientEntity.builder().id(CLIENT_ID).name("Name").nationalIdentityType(NationalIdentityType.CPF).nationalIdentity("12345678900").build();

        when(repository.findById(any())).thenReturn(Optional.of(clientEntity));

        final var databaseClient = service.findClientById(CLIENT_ID);

        assertEquals(CLIENT_ID, databaseClient.getId());
        assertEquals(clientEntity.getName(), databaseClient.getName());
        assertEquals(clientEntity.getNationalIdentityType(), databaseClient.getNationalIdentityType());
        assertEquals(clientEntity.getNationalIdentity(), databaseClient.getNationalIdentity());

        verify(repository).findById(CLIENT_ID);
    }

    @Test
    void shouldNotFindClientByIdBecauseDoesNotExist() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> {
            service.findClientById(CLIENT_ID);
        });

        final var expectedMessage = "Client with id " + CLIENT_ID + " does not exist";
        final var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(repository).findById(CLIENT_ID);
    }

    @Test
    void shouldFindPagedClients() {
        final var clients = Arrays.asList(ClientEntity.builder().id(UUID.randomUUID()).build(), ClientEntity.builder().id(UUID.randomUUID()).build(), ClientEntity.builder().id(UUID.randomUUID()).build());

        Page<ClientEntity> page = new PageImpl<>(clients);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ClientEntity> result = service.findPagedClients(Pageable.unpaged());

        assertEquals(clients.size(), result.getContent().size());
        assertEquals(clients.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(clients.get(1).getId(), result.getContent().get(1).getId());
        assertEquals(clients.get(2).getId(), result.getContent().get(2).getId());

        verify(repository).findAll(page.getPageable());
    }
}