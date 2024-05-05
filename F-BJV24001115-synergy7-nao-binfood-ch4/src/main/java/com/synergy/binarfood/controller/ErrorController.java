package com.synergy.binarfood.controller;

import com.synergy.binarfood.model.DataResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<DataResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        log.error("Error code {} : {}", HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(DataResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(value = { ResponseStatusException.class })
    public ResponseEntity<DataResponse<String>> apiException(ResponseStatusException exception) {
        log.error("Error code {} : {}", exception.getStatusCode(), exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode())
                .body(DataResponse.<String>builder().errors(exception.getReason()).build());
    }

}