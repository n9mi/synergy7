package com.synergy.binfood.service;

import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.auth.LoginRequest;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;

public interface AuthService {
    public AuthData login(LoginRequest request) throws ValidationException, UnauthorizedException;
}
