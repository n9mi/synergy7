package com.synergy.binarfood.core.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOtpRequest {
    @JsonIgnore
    @NotBlank
    private String email;

    @NotBlank
    private String otpCode;
}
