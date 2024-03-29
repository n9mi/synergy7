package com.synergy.binfood.service;

import com.synergy.binfood.entity.Menu;
import com.synergy.binfood.entity.Variant;
import com.synergy.binfood.model.menu.*;
import com.synergy.binfood.repository.MenuRepository;
import com.synergy.binfood.repository.VariantRepository;
import com.synergy.binfood.utils.ExceptionUtil;
import com.synergy.binfood.utils.exception.NotFoundError;
import com.synergy.binfood.utils.exception.ValidationError;
import jakarta.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuServiceImpl extends Service implements MenuService {
    private final MenuRepository menuRepository;
    private final VariantRepository variantRepository;

    public MenuServiceImpl(MenuRepository menuRepository, VariantRepository variantRepository) {
        this.menuRepository = menuRepository;
        this.variantRepository = variantRepository;
    }

    public List<MenuResponse> findMenus() {
        List<MenuResponse> response = new ArrayList<>();

        List<Menu> menus = this.menuRepository.findAll();
        for (Menu menu: menus) {
            response.add(new MenuResponse(menu.getCode(), menu.getName(), menu.getPrice()));
        }

        return response;
    }

    public MenuWithVariantsResponse findMenuWithVariants(MenuWithVariantRequest request) throws ValidationError, NotFoundError {
        Set<ConstraintViolation<MenuWithVariantRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        if (!this.menuRepository.isExistsByCode(request.getMenuCode())) {
            throw new NotFoundError(String.format("menu with code %s doesn't exists", request.getMenuCode()));
        }

        Menu menu = this.menuRepository.findByCode(request.getMenuCode());
        List<Variant> variants = this.menuRepository.findMenuVariants(menu);
        /*
         If request.variantCode is not empty, exclude this variant code from variants
         This feature is used if user want to change variant in order
        */
        if (request.getVariantCode() != null) {
            variants = variants.stream().filter(v -> !v.getCode().equals(request.getVariantCode())).
                collect(Collectors.toList());
        }

        List<VariantResponse> variantResponses = new ArrayList<>();
        for (Variant variant: variants) {
            variantResponses.add(new VariantResponse(variant.getCode(), variant.getName()));
        }

        return new MenuWithVariantsResponse(menu.getCode(), menu.getName(), menu.getPrice(), variantResponses);
    }

    public boolean isMenuHasAnyVariant(MenuIdRequest request) throws ValidationError, NotFoundError {
        Set<ConstraintViolation<MenuIdRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        if (this.menuRepository.isExistsByCode(request.getMenuCode())) {
            throw new NotFoundError("menu is not exists");
        }

        return this.menuRepository.isMenuHasAnyVariant(request.getMenuCode());
    }

    public boolean isMenuHasVariant(MenuVariantRequest request) throws ValidationError, NotFoundError {
        Set<ConstraintViolation<MenuVariantRequest>> violations = Service.validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationError(ExceptionUtil.getViolationsMessage(violations));
        }

        if (this.menuRepository.isExistsByCode(request.getMenuCode())) {
            throw new NotFoundError("menu is not exists");
        }

        if (this.menuRepository.isMenuHasAnyVariant(request.getMenuCode()) && this.variantRepository.
                isExistsByCode(request.getVariantCode())) {
            throw new NotFoundError("variant is not exists");
        }

        return this.menuRepository.isVariantExistsOnMenu(request.getMenuCode(),
                request.getVariantCode());
    }
}
