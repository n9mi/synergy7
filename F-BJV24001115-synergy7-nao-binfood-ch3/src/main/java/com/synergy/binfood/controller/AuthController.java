package com.synergy.binfood.controller;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.auth.LoginRequest;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;
import lombok.AllArgsConstructor;

import com.synergy.binfood.service.AuthService;

@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    public AuthData getUserInfoByUsername(String username) {
        return this.authService.getUserInfoByUsername(username);
    }

    public AuthData login(String username, String password) throws ValidationException, UnauthorizedException {
        return this.authService.login(new LoginRequest(username, password));
    }
}
