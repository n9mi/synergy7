package com.synergy.utils;

import com.synergy.data.Menu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MenuReader {
    private final String MENU_PATH;
    private final List<Menu> menus;

    public MenuReader(String menuFileName) {
        this.MENU_PATH = System.getProperty("user.dir") + "/" + menuFileName;
        this.menus = new ArrayList<>();
    }

    public void readMenuFromCSV() throws RuntimeException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.MENU_PATH))) {
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 2) {
                    throw new RuntimeException("Invalid menu vile");
                }

                this.menus.add(new Menu(counter, values[0], Integer.parseInt(values[1])));
                counter += 1;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found : " + e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println("Can't read csv : " + e.getMessage());
            throw new RuntimeException();
        }
    }

    public List<Menu> getMenus() {
        return Collections.unmodifiableList(this.menus);
    }
}
