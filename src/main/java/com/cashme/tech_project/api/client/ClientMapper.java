package com.cashme.tech_project.api.client;

import com.cashme.tech_project.api.client.address.AddressResponse;
import com.cashme.tech_project.domain.address.AddressEntity;
import com.cashme.tech_project.domain.client.ClientEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientMapper {

    public ClientEntity toEntity(final ClientRequest request) {
        final var addressRequest = request.getAddress();

        final var address = AddressEntity.builder()
                .addressType(addressRequest.getAddressType())
                .address(addressRequest.getAddress())
                .number(addressRequest.getNumber())
                .neighborhood(addressRequest.getNeighborhood())
                .zipCode(addressRequest.getZipCode())
                .city(addressRequest.getCity())
                .uf(addressRequest.getUf())
                .build();

        final var client = ClientEntity.builder()
                .name(request.getName())
                .nationalIdentityType(request.getNationalIdentityType())
                .nationalIdentity(request.getNationalIdentity())
                .build();

        address.setClient(client);
        client.setAddress(address);
        return client;
    }

    public ClientEntity toUpdateEntity(final UUID clientId, final ClientRequest request) {
        return toEntity(request).withId(clientId);
    }

    public ClientResponse toResponse(final ClientEntity entity) {
        final var address = entity.getAddress();

        final var addressResponse = AddressResponse.builder()
                .id(address.getId())
                .addressType(address.getAddressType())
                .address(address.getAddress())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .zipCode(address.getZipCode())
                .city(address.getCity())
                .uf(address.getUf())
                .build();

        return ClientResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .nationalIdentityType(entity.getNationalIdentityType())
                .nationalIdentity(entity.getNationalIdentity())
                .address(addressResponse)
                .build();
    }
}
