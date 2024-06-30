package com.cashme.tech_project.domain.client.validator;

import com.cashme.tech_project.domain.client.NationalIdentityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidatorNationalIdentityClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ValidatorNationalIdentityClient validator;

    @Test
    public void shouldTestValidNationalIdentity() {
        final var mockResponse = new ValidationResponse(true, "");

        when(restTemplate.getForEntity(anyString(), eq(ValidationResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        final var isValid = validator.isNationalIdentityValid("123456789", NationalIdentityType.CPF);

        assertTrue(isValid);

        verify(restTemplate).getForEntity(anyString(), eq(ValidationResponse.class));
    }

    @Test
    public void shouldTestInvalidNationalIdentity() {
        when(restTemplate.getForEntity(anyString(), eq(ValidationResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        final var isValid = validator.isNationalIdentityValid("123456789", NationalIdentityType.CPF);

        assertFalse(isValid);

        verify(restTemplate).getForEntity(anyString(), eq(ValidationResponse.class));
    }

    @Test
    public void shouldTestRestClientException() {
        when(restTemplate.getForEntity(anyString(), eq(ValidationResponse.class)))
                .thenThrow(new RestClientException("Internal Server Error"));

        final var isValid = validator.isNationalIdentityValid("123456789", NationalIdentityType.CPF);

        assertFalse(isValid);

        verify(restTemplate).getForEntity(anyString(), eq(ValidationResponse.class));
    }

    @Test
    public void shouldTestHttpStatusNotOk() {
        when(restTemplate.getForEntity(anyString(), eq(ValidationResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        final var isValid = validator.isNationalIdentityValid("123456789", NationalIdentityType.CPF);

        assertFalse(isValid);

        verify(restTemplate).getForEntity(anyString(), eq(ValidationResponse.class));
    }

    @Test
    public void shouldTestEmptyResponseBody() {
        when(restTemplate.getForEntity(anyString(), eq(ValidationResponse.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        final var isValid = validator.isNationalIdentityValid("123456789", NationalIdentityType.CPF);

        assertFalse(isValid);

        verify(restTemplate).getForEntity(anyString(), eq(ValidationResponse.class));
    }
}
