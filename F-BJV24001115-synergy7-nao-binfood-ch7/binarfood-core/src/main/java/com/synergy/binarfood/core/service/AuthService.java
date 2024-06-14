package com.synergy.binarfood.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.synergy.binarfood.core.model.auth.RegisterRequest;

public interface AuthService {
    public void register(RegisterRequest request) throws JsonProcessingException;
}
