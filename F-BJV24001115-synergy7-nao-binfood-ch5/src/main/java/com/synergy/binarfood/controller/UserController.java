package com.synergy.binarfood.controller;

import com.synergy.binarfood.model.auth.TokenResponse;
import com.synergy.binarfood.model.user.UserRequest;
import com.synergy.binarfood.model.user.UserResponse;
import com.synergy.binarfood.model.web.WebResponse;
import com.synergy.binarfood.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(
            path = "/profile",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> getCurrentProfile(
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponse response = this.userService.findCurrentUser(userDetails.getUsername());

        return WebResponse.<UserResponse>builder()
                .data(response)
                .build();
    }

    @PutMapping(
            path = "/profile",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> updateProfile(
            Authentication authentication,
            @RequestBody UserRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        request.setOldUsername(userDetails.getUsername());
        TokenResponse response = this.userService.updateProfile(request);

        return WebResponse.<TokenResponse>builder()
                .data(response)
                .build();
    }

    @DeleteMapping(
            path = "/delete",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> deleteCurrentUser(
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        this.userService.deleteCurrentUser(userDetails.getUsername());

        return WebResponse.<String>builder()
                .data(null)
                .build();
    }
}
