package com.cashme.tech_project.api.client.address;

import com.cashme.tech_project.domain.address.AddressType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddressResponse {

    private UUID id;

    private AddressType addressType;

    private String address;

    private String number;

    private String neighborhood;

    private String zipCode;

    private String city;

    private String uf;
}
