package com.cashme.tech_project.api.client.address;

import com.cashme.tech_project.domain.address.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddressResponse {

    @Schema(description = "Address ID")
    private UUID id;

    @Schema(description = "Type of address")
    private AddressType addressType;

    @Schema(description = "Street address")
    private String address;

    @Schema(description = "Address number")
    private String number;

    @Schema(description = "Neighborhood")
    private String neighborhood;

    @Schema(description = "ZIP code")
    private String zipCode;

    @Schema(description = "City")
    private String city;

    @Schema(description = "State abbreviation (UF)")
    private String uf;
}
