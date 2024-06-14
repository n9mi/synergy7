package com.synergy.binarfood.core.service;

import com.synergy.binarfood.core.model.user.UserUpdateRequest;

public interface UserService {
    public boolean isUserVerifiedByEmail(String email);
    public void update(UserUpdateRequest request);
    public void delete(String email);
}
