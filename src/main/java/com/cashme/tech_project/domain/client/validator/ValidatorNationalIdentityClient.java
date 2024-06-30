package com.cashme.tech_project.domain.client.validator;

import com.cashme.tech_project.domain.client.NationalIdentityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidatorNationalIdentityClient {

    @Value("${api.invertexto.base-url}")
    private String baseUrl;

    @Value("${api.invertexto.token}")
    private String apiToken;

    private final RestTemplate restTemplate;

    public boolean isNationalIdentityValid(final String nationalIdentity, final NationalIdentityType type) {
        final var url = String.format("%s/validator?token=%s&value=%s&type=%s",
                baseUrl, apiToken, nationalIdentity, type.getUrlType());

        try {
            final var response = restTemplate.getForEntity(url, ValidationResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && Objects.nonNull(response.getBody())) {
                boolean isValid = response.getBody().isValid();
                log.info("Validation result for {} ({}) is: {}", nationalIdentity, type, isValid);
                return isValid;
            } else {
                log.warn("Unexpected response status: {} for national identity: {}", response.getStatusCode(), nationalIdentity);
                return false;
            }
        } catch (HttpClientErrorException e) {
            log.error("Client error occurred during validation: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (RestClientException e) {
            log.error("Error occurred during API call to validate national identity: {}", e.getMessage(), e);
            return false;
        }
    }
}
