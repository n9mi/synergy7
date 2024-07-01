package com.synergy.binarfood.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.synergy.binarfood.model.auth.LoginRequest;
import com.synergy.binarfood.model.auth.RegisterRequest;
import com.synergy.binarfood.model.auth.TokenResponse;

public interface AuthService {
    public void register(RegisterRequest request) throws JsonProcessingException;
    public TokenResponse login(LoginRequest request);
}
