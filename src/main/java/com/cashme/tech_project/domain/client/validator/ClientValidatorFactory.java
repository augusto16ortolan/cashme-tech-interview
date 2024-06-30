package com.cashme.tech_project.domain.client.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ClientValidatorFactory {

    private final Set<ClientValidator> validators;

    public Set<ClientValidator> getClientValidators() {
        return validators;
    }
}
