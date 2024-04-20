package com.synergy.binfood;

import com.synergy.binfood.controller.AuthController;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.repository.UserRepository;
import com.synergy.binfood.service.AuthService;
import com.synergy.binfood.service.AuthServiceImpl;

import com.synergy.binfood.util.exception.UnauthorizedException;
import com.synergy.binfood.util.exception.ValidationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthTest extends AppTest {
    private final UserRepository userRepository = new UserRepository();
    private final AuthService authService = new AuthServiceImpl(userRepository);
    private final AuthController authController = new AuthController(authService);

    @DisplayName("AUTH TEST - LOGIN - SUCCESS")
    @Test
    void testLoginSuccess() {
        assertDoesNotThrow(() -> {
            AuthData authData = this.authController.login("naomi", "password");

            assertTrue(authData.getUserId() > 0);
            assertEquals("naomi", authData.getUserName());
        });
    }

    @DisplayName("AUTH TEST - LOGIN - FAILED -> VALIDATION")
    @Test
    void testLoginValidationFailed() {
        assertThrows(ValidationException.class, () -> {
            AuthData authData = this.authController.login(null, "password");
        });
        assertThrows(ValidationException.class, () -> {
            AuthData authData = this.authController.login("", "password");
        });
        assertThrows(ValidationException.class, () -> {
            AuthData authData = this.authController.login("naomi", null);
        });
        assertThrows(ValidationException.class, () -> {
            AuthData authData = this.authController.login("naomi", "");
        });
    }

    @DisplayName("AUTH TEST - LOGIN - FAILED -> UNAUTHORIZED")
    @Test
    void testLoginUnauthorizedFailed() {
        assertThrows(UnauthorizedException.class, () -> {
            AuthData authData = this.authController.login("randomusername", "password");
        });
        assertThrows(UnauthorizedException.class, () -> {
            AuthData authData = this.authController.login("naomi", "notapassword");
        });
    }
}
