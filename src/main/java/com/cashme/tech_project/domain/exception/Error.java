package com.cashme.tech_project.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class Error {
    private HttpStatus status;
    private int statusCode;
    private List<String> errors;
}
