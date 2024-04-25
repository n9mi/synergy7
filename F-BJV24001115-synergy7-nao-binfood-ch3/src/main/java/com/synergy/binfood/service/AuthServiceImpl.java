package com.synergy.binfood.service;

import com.synergy.binfood.entity.User;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.auth.LoginRequest;
import com.synergy.binfood.repository.UserRepository;
import com.synergy.binfood.util.exception.ExceptionUtil;
import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthData getUserInfoByUsername(String username) {
        User user = this.userRepository.findByUserName(username);

        return new AuthData(user.getId(), user.getUsername());
    }

    public AuthData login(LoginRequest request) throws ValidationException, UnauthorizedException {
        Set<ConstraintViolation<LoginRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.userRepository.isExistsByUsername(request.getUsername())) {
            throw new UnauthorizedException("can't find user");
        }

        User user = this.userRepository.findByUserName(request.getUsername());
        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("can't find user");
        }

        return new AuthData(user.getId(), user.getUsername());
    }
}
