package com.synergy.binfood;

import com.synergy.binfood.controller.MerchantController;
import com.synergy.binfood.model.auth.AuthData;
import com.synergy.binfood.model.merchant.MerchantWithProductsResponse;
import com.synergy.binfood.repository.MerchantRepository;
import com.synergy.binfood.repository.UserRepository;
import com.synergy.binfood.service.MerchantService;
import com.synergy.binfood.service.MerchantServiceImpl;

import com.synergy.binfood.util.exception.NotFoundException;
import com.synergy.binfood.util.exception.UnauthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MerchantTest extends AppTest {
    private final MerchantRepository merchantRepository = new MerchantRepository();
    private final UserRepository userRepository = new UserRepository();
    private final MerchantService merchantService = new MerchantServiceImpl(merchantRepository, userRepository);
    private final MerchantController merchantController = new MerchantController(merchantService);

    // VALID or INVALID auth data depends on user seeder
    // VALID means user exists in seeder
    // INVALID means user doesn't exists in seeder
    private AuthData VALID_authData = new AuthData(1, "naomi");
    private AuthData INVALID_authData = new AuthData(99, "joko");
    private AuthData EMPTY_authData = new AuthData(0, null);


    @DisplayName("MERCHANT TEST - GET OPENED - SUCCESS")
    @Test
    void getOpenedMerchantSuccess() {
        assertFalse(this.merchantController.getOpenedMerchants().isEmpty());
    }

    @DisplayName("MERCHANT TEST - GET OPENED MERCHANT DETAIL BY ID - SUCCESS")
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    void getOpenedMerchantDetail(int merchantId) {
        assertDoesNotThrow(() -> {
            MerchantWithProductsResponse response = this.merchantController.getOpenedMerchantDetailById(merchantId);
            assertTrue(response.getMerchantId() > 0);
            assertFalse(response.getMerchantName().isBlank());
            assertFalse(response.getMerchantLocation().isBlank());
            assertTrue(response.isMerchantOpen());
            assertFalse(response.getMerchantProducts().isEmpty());
        });
    }

    @DisplayName("MERCHANT TEST - GET CLOSED/UNAVAILABLE MERCHANTS DETAIL BY ID - FAILED -> NOT FOUND/CLOSED MERCHANT")
    @ParameterizedTest
    @ValueSource(ints = { -99, 0, 4, 100 })
    void getClosedMerchantDetail(int merchantId) {
        assertThrows(NotFoundException.class,
                () -> this.merchantController.getOpenedMerchantDetailById(merchantId));
    }

    @DisplayName("MERCHANT TEST - SET CURRENT MERCHANT IN AUTH DATA - SUCCESS")
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    void setCurrentMerchantInAuthDataSuccess(int merchantId) {
        assertDoesNotThrow(() -> {
            this.VALID_authData = this.merchantController.setCurrentMerchant(VALID_authData, merchantId);
            assertTrue(this.VALID_authData.getUserId() > 0);
            assertFalse(this.VALID_authData.getUserName().isBlank());
            assertTrue(this.VALID_authData.getCurrMerchantId() > 0);
        });
    }

    @DisplayName("MERCHANT TEST - SET CURRENT MERCHANT IN AUTH DATA - FAILED -> MERCHANT NOT FOUND")
    @ParameterizedTest
    @ValueSource(ints = { -99, 0, 4, 100 })
    void setCurrentMerchantInAuthDataFailedMerchantNotFound(int merchantId) {
        assertThrows(NotFoundException.class, () -> {
            this.VALID_authData = this.merchantController.setCurrentMerchant(VALID_authData, merchantId);
        });
    }

    @DisplayName("MERCHANT TEST - SET CURRENT MERCHANT IN AUTH DATA - FAILED -> UNAUTHORIZED")
    @Test
    void setCurrentMerchantInAuthDataFailedUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> {
            this.merchantController.setCurrentMerchant(INVALID_authData, 1);
        });
        assertThrows(UnauthorizedException.class, () -> {
            this.merchantController.setCurrentMerchant(EMPTY_authData, 1);
        });
    }
}
