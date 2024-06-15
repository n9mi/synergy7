package com.synergy.binarfood.core.service;

import com.synergy.binarfood.core.entity.User;
import com.synergy.binarfood.core.entity.UserRegistrationOtp;
import com.synergy.binarfood.core.model.auth.ValidateOtpRequest;
import com.synergy.binarfood.core.repository.UserRegistrationOtpRepository;
import com.synergy.binarfood.core.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserRepository userRepository;
    private final UserRegistrationOtpRepository userRegistrationOtpRepository;

    @Transactional
    public void validateUserRegistrationOtp(ValidateOtpRequest request) {
        User user = this.userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "user not found"));
        if (user.isVerified()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already verified");
        }

        UserRegistrationOtp userOtp = this.userRegistrationOtpRepository
                .findByUser_Email(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "something wrong"));
        if (userOtp.getExpiredAt().before(new Date()) || !BCrypt.checkpw(request.getOtpCode(), userOtp.getOtpCode())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "expired code");
        }

        user.setVerified(true);
        this.userRepository.save(user);
    }
}
