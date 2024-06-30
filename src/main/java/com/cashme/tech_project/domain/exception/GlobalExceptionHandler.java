package com.cashme.tech_project.domain.exception;

import com.cashme.tech_project.domain.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Generated
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> methodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final var httpStatus = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });
        return ResponseEntity.status(httpStatus)
                .body(new Error(httpStatus,
                        httpStatus.value(),
                        errors));
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<Error> businessExceptionHandler(final BusinessException exception) {
        final var httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus)
                .body(new Error(httpStatus,
                        httpStatus.value(),
                        List.of(exception.getMessage())));
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<Error> notFoundExceptionHandler(final NotFoundException exception) {
        final var httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus)
                .body(new Error(httpStatus,
                        httpStatus.value(),
                        List.of(exception.getMessage())));
    }
}
