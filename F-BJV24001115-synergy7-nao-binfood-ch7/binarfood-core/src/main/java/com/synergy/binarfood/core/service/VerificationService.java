package com.synergy.binarfood.core.service;

import com.synergy.binarfood.core.model.auth.ValidateOtpRequest;

public interface VerificationService {
    public void validateUserRegistrationOtp(ValidateOtpRequest request);
}
