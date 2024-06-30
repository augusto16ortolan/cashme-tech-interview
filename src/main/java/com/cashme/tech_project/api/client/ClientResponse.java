package com.cashme.tech_project.api.client;

import com.cashme.tech_project.api.client.address.AddressResponse;
import com.cashme.tech_project.domain.client.NationalIdentityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    @Schema(description = "Client ID")
    private UUID id;

    @Schema(description = "Client name")
    private String name;

    @Schema(description = "National identity type of the client")
    private NationalIdentityType nationalIdentityType;

    @Schema(description = "National identity number of the client")
    private String nationalIdentity;

    @Schema(description = "Client's address")
    private AddressResponse address;
}
