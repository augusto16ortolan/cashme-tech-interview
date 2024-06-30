package com.cashme.tech_project.domain.client.validator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationResponse {

    @JsonProperty("valid")
    private boolean valid;

    @JsonProperty("formatted")
    private String formatted;
}