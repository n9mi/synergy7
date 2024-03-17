package com.synergy.repository;

import com.synergy.model.Menu;
import com.synergy.utils.MenuReader;

import java.util.Map;

public class MenuRepository {
    private final Map<String, Menu> menus;

    public MenuRepository(String menuFilePath) {
        this.menus = MenuReader.readFromCSV(menuFilePath);
    }

    public Menu[] findAll() {
        return this.menus.values().toArray(new Menu[0]);
    }

    public int count() {
        return this.menus.size();
    }

    public boolean isExists(String menuId) {
        return this.menus.containsKey(menuId);
    }

    public Menu findById(String menuId) throws RuntimeException {
        return this.menus.get(menuId);
    }
}
