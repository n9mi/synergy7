package com.synergy.binarfood.service;

import com.synergy.binarfood.model.user.UserUpdateRequest;

public interface UserService {
    public boolean isUserVerifiedByEmail(String email);
    public void update(UserUpdateRequest request);
    public void delete(String email);
}
