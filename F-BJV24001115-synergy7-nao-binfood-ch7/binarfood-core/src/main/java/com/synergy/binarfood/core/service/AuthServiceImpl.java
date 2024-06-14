package com.synergy.binarfood.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.synergy.binarfood.core.entity.ERole;
import com.synergy.binarfood.core.entity.Role;
import com.synergy.binarfood.core.entity.User;
import com.synergy.binarfood.core.entity.UserRegistrationOtp;
import com.synergy.binarfood.core.message.MailMessage;
import com.synergy.binarfood.core.message.MailType;
import com.synergy.binarfood.core.model.auth.RegisterRequest;
import com.synergy.binarfood.core.repository.RoleRepository;
import com.synergy.binarfood.core.repository.UserRegistrationOtpRepository;
import com.synergy.binarfood.core.repository.UserRepository;
import com.synergy.binarfood.core.security.service.JWTService;
import com.synergy.binarfood.core.util.random.Randomizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRegistrationOtpRepository userRegistrationOtpRepository;
    private final ValidationService validationService;
    private final MailPublisherService mailPublisherService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterRequest request) throws JsonProcessingException {
        this.validationService.validate(request);

        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("user with email %s already exists", request.getEmail()));
        }

        if (!EnumUtils.isValidEnum(ERole.class, request.getAsRole())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "role didn't match any records");
        }
        Role role = this.roleRepository.findByName(ERole.valueOf(request.getAsRole()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "role didn't match any records"));

        User user = User
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .roles(List.of(role))
                .password(this.passwordEncoder.encode(request.getPassword()))
                .isVerified(false)
                .build();
        this.userRepository.save(user);

        String otpCode = Randomizer.generateOtp(6);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);

        UserRegistrationOtp userOtp = this.userRegistrationOtpRepository
                .findByUser_Email(user.getEmail())
                .orElse(UserRegistrationOtp.builder()
                        .user(user)
                        .build());
        userOtp.setOtpCode(BCrypt.hashpw(otpCode, BCrypt.gensalt()));
        userOtp.setExpiredAt(calendar.getTime());
        this.userRegistrationOtpRepository.save(userOtp);

        MailMessage<String> mailMessage = MailMessage.<String>builder()
                .recipient(user.getEmail())
                .type(MailType.OTP_MAIL)
                .data(otpCode)
                .build();
        this.mailPublisherService.publish(mailMessage);
    }
}
