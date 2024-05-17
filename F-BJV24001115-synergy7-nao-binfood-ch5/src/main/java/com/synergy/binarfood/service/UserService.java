package com.synergy.binarfood.service;

import com.synergy.binarfood.model.auth.TokenResponse;
import com.synergy.binarfood.model.user.UserRequest;
import com.synergy.binarfood.model.user.UserResponse;

public interface UserService {
    public UserResponse findCurrentUser(String username);
    public TokenResponse updateProfile(UserRequest request);
    public void deleteCurrentUser(String username);
}
