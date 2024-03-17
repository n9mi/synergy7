package com.synergy.utils;

import com.synergy.model.Menu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MenuReader {

    public static Map<String, Menu> readFromCSV(String menuFilePath) throws RuntimeException {
        final Map<String, Menu> menus = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(menuFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Menu menu = getMenu(line);
                menus.put(menu.id, menu);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file not found : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("failed to read csv : " + e.getMessage());
        }

        return menus;
    }

    private static Menu getMenu(String line) {
        String[] values = line.split(",");
        if (values.length != 3) {
            throw new RuntimeException("invalid menu .csv file");
        }
        if (values[0].isEmpty() || values[1].isEmpty() || values[2].isEmpty()) {
            throw new RuntimeException("invalid menu .csv file");
        }

        if (values[0].equals("O")) {
            throw new RuntimeException("menu id can't be O");
        }

        if (values[0].equals("X")) {
            throw new RuntimeException("menu id can't be X");
        }

        return new Menu(values[0].trim(), values[1].trim(), Integer.parseInt(values[2].trim()));
    }
}
