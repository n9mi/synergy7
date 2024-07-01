package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.user.UserUpdateRequest;
import com.synergy.binarfood.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public boolean isUserVerifiedByEmail(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            return false;
        }

        return user.isVerified();
    }

    public void update(UserUpdateRequest request) {
        User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "user didn't exists"));

        user.setEmail(request.getNewEmail());
        user.setName(request.getName());
        this.userRepository.save(user);
    }

    public void delete(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "user didn't exists"));

        this.userRepository.delete(user);
    }
}
