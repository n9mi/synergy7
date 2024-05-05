package com.synergy.binarfood.service;

import com.synergy.binarfood.model.user.UserCreateRequest;
import com.synergy.binarfood.model.user.UserUpdateRequest;
import com.synergy.binarfood.model.user.UserWithOrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public Page<UserWithOrderResponse> findAll(int page, int pageSize);
    public void create(UserCreateRequest request);
    public void update(UserUpdateRequest request);
    public void delete(String id);
    public void generateDummyUser();
    public void authorizeUser(String id);
    public void cleanDummyUser();
}
