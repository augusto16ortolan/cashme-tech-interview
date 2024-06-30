package com.cashme.tech_project.domain.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NationalIdentityType {

    CPF(11, "cpf"),
    CNPJ(14, "cnpj");

    private final int size;
    private final String urlType;
}
