package com.synergy.binarfood.core.controller.verification;

import com.synergy.binarfood.core.model.auth.ValidateOtpRequest;
import com.synergy.binarfood.core.model.web.WebResponse;
import com.synergy.binarfood.core.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/core/verification")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/register/otp/verify")
    public ResponseEntity<WebResponse<String>> requestRegistrationOtp(
            Authentication authentication,
            @RequestBody ValidateOtpRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setEmail(userDetails.getUsername());

        this.verificationService.validateUserRegistrationOtp(request);
        WebResponse<String> response = WebResponse.<String>builder()
                .data("")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
