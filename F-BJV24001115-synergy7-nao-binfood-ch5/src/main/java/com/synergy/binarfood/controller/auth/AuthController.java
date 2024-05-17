package com.synergy.binarfood.controller.auth;

import com.synergy.binarfood.entity.Role;
import com.synergy.binarfood.model.auth.LoginRequest;
import com.synergy.binarfood.model.auth.RegisterRequest;
import com.synergy.binarfood.model.auth.TokenResponse;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/merchant/register")
    public WebResponse<String> merchantRegister(@RequestBody RegisterRequest request) {
        request.setRole(Role.MERCHANT);
        this.authService.register(request);

        return WebResponse.<String>builder().data(null).build();
    }

    @PostMapping("/buyer/register")
    public WebResponse<String> buyerRegister(@RequestBody RegisterRequest request) {
        request.setRole(Role.BUYER);
        this.authService.register(request);

        return WebResponse.<String>builder().data("").build();
    }

    @PostMapping("/merchant/login")
    public WebResponse<TokenResponse> merchantLogin(@RequestBody LoginRequest request) {
        request.setRole(Role.MERCHANT);
        TokenResponse response = this.authService.login(request);

        return WebResponse.<TokenResponse>builder().data(response).build();
    }

    @PostMapping("/buyer/login")
    public WebResponse<TokenResponse> buyerLogin(@RequestBody LoginRequest request) {
        request.setRole(Role.BUYER);
        TokenResponse tokenResponse = this.authService.login(request);

        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }
}
