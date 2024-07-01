package com.synergy.binarfood.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.synergy.binarfood.model.auth.LoginRequest;
import com.synergy.binarfood.model.auth.RegisterRequest;
import com.synergy.binarfood.model.auth.TokenResponse;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/core/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<WebResponse<String>> register(
            @RequestBody RegisterRequest request) throws JsonProcessingException {
        this.authService.register(request);
        WebResponse<String> response = WebResponse.<String>builder()
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = this.authService.login(request);
        WebResponse<TokenResponse> response = WebResponse.<TokenResponse>builder()
                .data(tokenResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
