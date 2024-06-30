package com.cashme.tech_project.api.client.address;

import com.cashme.tech_project.domain.address.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @Schema(description = "Type of address", implementation = AddressType.class)
    @NotNull(message = "Address type cannot be null")
    private AddressType addressType;

    @Size(min = 2, max = 100, message = "Address size must be between 2 and 100")
    @Schema(description = "Address")
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Size(min = 1, max = 20, message = "Number's size must be between 1 and 20")
    @Schema(description = "Number")
    @NotBlank(message = "Number cannot be blank")
    private String number;

    @Size(min = 1, max = 100, message = "Neighborhood's Size must be between 1 and 100")
    @Schema(description = "Neighborhood")
    @NotBlank(message = "Neighborhood cannot be blank")
    private String neighborhood;

    @Size(min = 5, max = 20, message = "Zipcode's size must be between 5 and 20")
    @Schema(description = "ZIP code")
    @NotBlank(message = "ZIP code cannot be blank")
    private String zipCode;

    @Size(min = 1, max = 100, message = "City's size must be between 1 and 100")
    @Schema(description = "City")
    @NotBlank(message = "City cannot be blank")
    private String city;

    @Size(min = 2, max = 2, message = "Uf's size must be 2")
    @Schema(description = "State")
    @NotBlank(message = "State cannot be blank")
    private String uf;
}
