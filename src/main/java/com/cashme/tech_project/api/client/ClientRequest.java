package com.cashme.tech_project.api.client;

import com.cashme.tech_project.api.client.address.AddressRequest;
import com.cashme.tech_project.domain.client.NationalIdentityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    @Size(min = 5, max = 100, message = "Client's name size must be between 5 and 100")
    @Schema(description = "Client's name")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(description = "Type of national identity", implementation = NationalIdentityType.class)
    @NotNull(message = "National identity type cannot be null")
    private NationalIdentityType nationalIdentityType;

    @Size(min = 11, max = 14, message = "National identity number size must be between 11 and 14")
    @Schema(description = "National identity number")
    @NotBlank(message = "National identity cannot be blank")
    private String nationalIdentity;

    @Schema(description = "Client's address")
    @NotNull(message = "Address cannot be null")
    private @Valid AddressRequest address;
}
