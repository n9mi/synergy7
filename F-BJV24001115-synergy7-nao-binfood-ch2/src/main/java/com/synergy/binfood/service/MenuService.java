package com.synergy.binfood.service;

import com.synergy.binfood.model.menu.*;
import com.synergy.binfood.utils.exception.NotFoundError;
import com.synergy.binfood.utils.exception.ValidationError;

import java.util.List;

public interface MenuService {
    List<MenuResponse> findMenus();
    MenuWithVariantsResponse findMenuWithVariants(MenuWithVariantRequest request) throws ValidationError, NotFoundError;
    boolean isMenuHasAnyVariant(MenuIdRequest request) throws ValidationError, NotFoundError;
    boolean isMenuHasVariant(MenuVariantRequest request) throws ValidationError, NotFoundError;
}
