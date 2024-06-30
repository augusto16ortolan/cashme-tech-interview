package com.cashme.tech_project.domain.client.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ClientValidatorFactoryTest {

    @Mock
    private ClientNationalIdentityValidator validator;

    private Set<ClientValidator> mockValidators;

    @InjectMocks
    private ClientValidatorFactory validatorFactory;

    @BeforeEach
    void setUp() {
        mockValidators = new HashSet<>();
        mockValidators.add(validator);
        validatorFactory = new ClientValidatorFactory(mockValidators);
    }

    @Test
    void shouldGetClientValidators() {
        final var resultValidators = validatorFactory.getClientValidators();
        assertEquals(mockValidators.size(), resultValidators.size());
        assertEquals(mockValidators, resultValidators);
    }
}
