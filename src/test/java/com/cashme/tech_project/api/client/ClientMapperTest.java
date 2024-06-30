package com.cashme.tech_project.api.client;

import com.cashme.tech_project.api.client.address.AddressRequest;
import com.cashme.tech_project.domain.address.AddressEntity;
import com.cashme.tech_project.domain.address.AddressType;
import com.cashme.tech_project.domain.client.ClientEntity;
import com.cashme.tech_project.domain.client.NationalIdentityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ClientMapperTest {

    @InjectMocks
    private ClientMapper clientMapper;

    @Test
    public void shouldMapClientRequestToClientEntity() {
        final var addressRequest = AddressRequest.builder()
                .addressType(AddressType.STREET)
                .address("123 Main St")
                .number("101")
                .neighborhood("Downtown")
                .zipCode("12345")
                .city("Sample City")
                .uf("SC")
                .build();

        final var clientRequest = ClientRequest.builder()
                .name("John Doe")
                .nationalIdentityType(NationalIdentityType.CPF)
                .nationalIdentity("12345678901")
                .address(addressRequest)
                .build();

        final var clientEntity = clientMapper.toEntity(clientRequest);

        assertEquals(clientRequest.getName(), clientEntity.getName());
        assertEquals(clientRequest.getNationalIdentityType(), clientEntity.getNationalIdentityType());
        assertEquals(clientRequest.getNationalIdentity(), clientEntity.getNationalIdentity());

        final var addressEntity = clientEntity.getAddress();
        assertEquals(addressRequest.getAddressType(), addressEntity.getAddressType());
        assertEquals(addressRequest.getAddress(), addressEntity.getAddress());
        assertEquals(addressRequest.getNumber(), addressEntity.getNumber());
        assertEquals(addressRequest.getNeighborhood(), addressEntity.getNeighborhood());
        assertEquals(addressRequest.getZipCode(), addressEntity.getZipCode());
        assertEquals(addressRequest.getCity(), addressEntity.getCity());
        assertEquals(addressRequest.getUf(), addressEntity.getUf());
    }

    @Test
    public void shouldMapClientEntityToClientResponse() {
        final var addressEntity = AddressEntity.builder()
                .id(UUID.randomUUID())
                .addressType(AddressType.STREET)
                .address("123 Main St")
                .number("101")
                .neighborhood("Downtown")
                .zipCode("12345")
                .city("Sample City")
                .uf("SC")
                .build();

        final var clientEntity = ClientEntity.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .nationalIdentityType(NationalIdentityType.CPF)
                .nationalIdentity("12345678901")
                .address(addressEntity)
                .build();

        addressEntity.setClient(clientEntity);

        final var clientResponse = clientMapper.toResponse(clientEntity);

        assertEquals(clientEntity.getId(), clientResponse.getId());
        assertEquals(clientEntity.getName(), clientResponse.getName());
        assertEquals(clientEntity.getNationalIdentityType(), clientResponse.getNationalIdentityType());
        assertEquals(clientEntity.getNationalIdentity(), clientResponse.getNationalIdentity());

        final var addressResponse = clientResponse.getAddress();
        assertEquals(addressEntity.getId(), addressResponse.getId());
        assertEquals(addressEntity.getAddressType(), addressResponse.getAddressType());
        assertEquals(addressEntity.getAddress(), addressResponse.getAddress());
        assertEquals(addressEntity.getNumber(), addressResponse.getNumber());
        assertEquals(addressEntity.getNeighborhood(), addressResponse.getNeighborhood());
        assertEquals(addressEntity.getZipCode(), addressResponse.getZipCode());
        assertEquals(addressEntity.getCity(), addressResponse.getCity());
        assertEquals(addressEntity.getUf(), addressResponse.getUf());
    }

    @Test
    public void shouldMapClientRequestToUpdatedClientEntity() {
        final var existingClientId = UUID.randomUUID();

        final var addressRequest = AddressRequest.builder()
                .addressType(AddressType.STREET)
                .address("123 Main St")
                .number("101")
                .neighborhood("Downtown")
                .zipCode("12345")
                .city("Sample City")
                .uf("SC")
                .build();

        final var clientRequest = ClientRequest.builder()
                .name("John Doe")
                .nationalIdentityType(NationalIdentityType.CPF)
                .nationalIdentity("12345678901")
                .address(addressRequest)
                .build();

        final var clientEntity = clientMapper.toUpdateEntity(existingClientId, clientRequest);

        assertEquals(existingClientId, clientEntity.getId());
        assertEquals(clientRequest.getName(), clientEntity.getName());
        assertEquals(clientRequest.getNationalIdentityType(), clientEntity.getNationalIdentityType());
        assertEquals(clientRequest.getNationalIdentity(), clientEntity.getNationalIdentity());

        final var addressEntity = clientEntity.getAddress();
        assertEquals(addressRequest.getAddressType(), addressEntity.getAddressType());
        assertEquals(addressRequest.getAddress(), addressEntity.getAddress());
        assertEquals(addressRequest.getNumber(), addressEntity.getNumber());
        assertEquals(addressRequest.getNeighborhood(), addressEntity.getNeighborhood());
        assertEquals(addressRequest.getZipCode(), addressEntity.getZipCode());
        assertEquals(addressRequest.getCity(), addressEntity.getCity());
        assertEquals(addressRequest.getUf(), addressEntity.getUf());
    }
}
