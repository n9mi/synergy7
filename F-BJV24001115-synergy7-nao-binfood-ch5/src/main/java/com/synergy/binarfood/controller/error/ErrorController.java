package com.synergy.binarfood.controller.error;

import com.synergy.binarfood.model.web.WebResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity
                .badRequest()
                .body(WebResponse
                        .<String>builder()
                        .errors(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(value = { ResponseStatusException.class })
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(WebResponse
                        .<String>builder()
                        .errors(exception
                                .getReason())
                        .build());
    }

    @ExceptionHandler(value = { ExpiredJwtException.class })
    public ResponseEntity<WebResponse<String>> apiException(ExpiredJwtException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(WebResponse
                        .<String>builder()
                        .errors("invalid authorization header")
                        .build());
    }
}
