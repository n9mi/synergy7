package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.auth.TokenResponse;
import com.synergy.binarfood.model.user.UserRequest;
import com.synergy.binarfood.model.user.UserResponse;
import com.synergy.binarfood.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final JWTService jwtService;

    public UserResponse findCurrentUser(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));

        return UserResponse.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public TokenResponse updateProfile(UserRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository.findByUsername(request.getOldUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUsername(passwordEncoder.encode(request.getUsername()));
        user.setEmail(passwordEncoder.encode(request.getEmail()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        this.userRepository.save(user);

        String newToken = this.jwtService.generateToken(user);
        return TokenResponse.builder()
                .accessToken(newToken)
                .build();
    }

    public void deleteCurrentUser(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        this.userRepository.delete(user);
    }
}
