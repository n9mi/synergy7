package com.synergy.binarfood.core.security.service;

import com.synergy.binarfood.core.entity.User;
import com.synergy.binarfood.core.repository.UserRepository;
import com.synergy.binarfood.core.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("user with email %s doesn't exists", username)
                ));

        return UserDetailsImpl.build(user);
    }
}

