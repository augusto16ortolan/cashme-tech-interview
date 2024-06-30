package com.cashme.tech_project.domain.client.validator;

import com.cashme.tech_project.domain.client.ClientEntity;

public interface ClientValidator {

    void validate(final ClientEntity entity, final boolean isCreate);
}
