package com.synergy.binfood.repository;

import com.synergy.binfood.entity.Menu;
import com.synergy.binfood.entity.MenuVariant;
import com.synergy.binfood.entity.Variant;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
public class MenuRepository {
    public boolean isExistsByCode(String code) {
        return Repository.menus.containsKey(code);
    }

    public List<Menu> findAll() {
        return new ArrayList<>(Repository.menus.values());
    }

    public Menu findByCode(String code) {
        return Repository.menus.get(code);
    }

    public int count() {
        return Repository.menus.size();
    }

    public void create(Menu menu) {
        menu.setId(UUID.randomUUID().toString());
        Repository.menus.put(menu.getCode(), menu);
    }

    public void update(Menu menu) {
        Repository.menus.put(menu.getCode(), menu);
    }

    public void delete(Menu menu) {
        Repository.menus.remove(menu.getCode());
    }

    public List<Variant> findMenuVariants(Menu menu) {
        List<Variant> variantsFromMenu = new ArrayList<>();

        for (MenuVariant menuVariant: Repository.menuVariants.values()) {
            if (menuVariant.getMenuCode().equals(menu.getCode())) {
                Variant variant = Repository.variants.get(menuVariant.getVariantCode());
                variantsFromMenu.add(variant);
            }
        }

        return variantsFromMenu;
    }

    public boolean isMenuHasAnyVariant(String menuCode) {
        for (Map.Entry<String, MenuVariant> menuVariant: Repository.menuVariants.entrySet()) {
            if (menuVariant.getValue().getMenuCode().equals(menuCode)) {
                return true;
            }
        }

        return false;
    }

    public boolean isVariantExistsOnMenu(String menuCode, String variantCode) {
        return Repository.menuVariants.containsKey(String.format(Repository.composite2KeyFormat,
                menuCode, variantCode));
    }

    public void addVariantToMenu(Menu menu, Variant variant) {
        MenuVariant menuVariant = new MenuVariant(menu.getCode(), variant.getCode());
        Repository.menuVariants.put(String.format(Repository.composite2KeyFormat, menu.getCode(), variant.getCode()), menuVariant);
    }

    public void removeVariantFromMenu(Menu menu, Variant variant) {
        Repository.menuVariants.remove(String.format(Repository.composite2KeyFormat, menu.getCode(), variant.getCode()));
    }
}
