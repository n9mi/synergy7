package com.synergy.binarfood.model.auth;

import com.synergy.binarfood.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(max=50)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private Role role;
}
