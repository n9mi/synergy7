package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.user.UserCreateRequest;
import com.synergy.binarfood.model.user.UserUpdateRequest;
import com.synergy.binarfood.model.user.UserWithOrderResponse;
import com.synergy.binarfood.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.function.Function;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ValidationService validationService;

    @Autowired
    UserRepository userRepository;

    public Page<UserWithOrderResponse> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Object[]> results = this.userRepository.findAllWithOrderCount(pageable);
        Page<UserWithOrderResponse> users = results.map(new Function<Object[], UserWithOrderResponse>() {
            @Override
            public UserWithOrderResponse apply(Object[] source) {
                return UserWithOrderResponse.builder()
                        .id(String.valueOf(source[0]))
                        .username(String.valueOf(source[1]))
                        .email(String.valueOf(source[2]))
                        .createdDate(String.valueOf(source[3]))
                        .totalOrder((int) (long) source[4])
                        .build();
            }
        });

        return users;
    }

    private UserWithOrderResponse userWithOrderMapper(String[] result) {
        return UserWithOrderResponse.builder()
                .id(result[0])
                .username(result[1])
                .email(result[2])
                .createdDate(result[3])
                .totalOrder(Integer.parseInt(result[4]))
                .build();
    }

    @Transactional
    public void create(UserCreateRequest request) {
        this.validationService.validate(request);

        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }

        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();
        this.userRepository.save(user);
    }

    @Transactional
    public void update(UserUpdateRequest request) {
        this.validationService.validate(request);

        User user = this.userRepository.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        this.userRepository.save(user);
    }

    @Transactional
    public void delete(String id) {
        User user = this.userRepository.findFirstById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        this.userRepository.delete(user);
    }

    @Transactional
    public void generateDummyUser() {
        this.userRepository.generateDummyUser();
    }

    @Transactional
    public void authorizeUser(String id) {
        if (!this.userRepository.existsById(UUID.fromString(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists");
        }

        this.userRepository.authorizeUser(UUID.fromString(id));
    }

    @Transactional
    public void cleanDummyUser() {
        this.userRepository.cleanDummyUsers();
    }

}
