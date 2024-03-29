package com.synergy.binfood.utils;

import com.synergy.binfood.entity.Menu;
import com.synergy.binfood.entity.MenuVariant;
import com.synergy.binfood.entity.Variant;
import com.synergy.binfood.utils.exception.SeederError;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

@Getter
public class SeederUtil {
    private final String seedFolder;
    private final String menuSeedFileName;
    private final String variantSeedFileName;
    private final HashMap<String, Variant> variantsResult;
    private final HashMap<String, Menu> menusResults;
    private final HashMap<String, MenuVariant> menuVariantsResult;

    public SeederUtil(String seedFolder, String menuSeedFileName, String variantSeedFileName) {
        this.seedFolder = seedFolder;
        this.menuSeedFileName = menuSeedFileName;
        this.variantSeedFileName = variantSeedFileName;
        this.variantsResult = new HashMap<>();
        this.menusResults = new HashMap<>();
        this.menuVariantsResult = new HashMap<>();
    }

    @Getter
    @AllArgsConstructor
    private class MenuWithVariants {
        private Menu menu;
        private List<MenuVariant> menuVariants;
    }

    public void seedVariant() throws SeederError {
        String variantSeederFilePath = seedFolder + "/" + variantSeedFileName;

        try (BufferedReader br = new BufferedReader(new FileReader(variantSeederFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Variant variant = this.getVariant(line);
                this.variantsResult.put(variant.getCode(), variant);
            }
        } catch (FileNotFoundException e) {
            throw new SeederError("file not found : " + e.getMessage());
        } catch (Exception e) {
            throw new SeederError("failed to read csv : " + e.getMessage());
        }
    }

    private Variant getVariant(String line) throws SeederError {
        String[] values = line.split(",");
        if (values.length != 2) {
            throw new SeederError("invalid variant .csv file");
        }

        if (values[0].isEmpty() || values[1].isEmpty()) {
            throw new SeederError("invalid variant .csv file");
        }

        if (values[0].equals("O") || values[0].equals("X") || values[0].equals("P") || values[0].equals("Q") ||
                values[0].equals("V")) {
            throw new SeederError("variant code can't be 0, X, P, Q, or V");
        }

        return new Variant(UUID.randomUUID().toString(), values[0].trim(), values[1].trim());
    }

    public void seedMenuVariants() throws SeederError {
        String variantSeederFilePath = this.seedFolder + "/" + this.menuSeedFileName;

        try (BufferedReader br = new BufferedReader(new FileReader(variantSeederFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                MenuWithVariants menuWithVariants = this.getMenuVariants(line);
                Menu menu = menuWithVariants.getMenu();
                this.menusResults.put(menu.getCode(), menu);
                menuWithVariants.getMenuVariants().forEach(mv ->
                    this.menuVariantsResult.put(String.format("%s_%s", mv.getMenuCode(), mv.getVariantCode()), mv));
            }
        } catch (FileNotFoundException e) {
            throw new SeederError("file not found : " + e.getMessage());
        } catch (Exception e) {
            throw new SeederError("failed to read csv : " + e.getMessage());
        }
    }

    private MenuWithVariants getMenuVariants(String line) throws SeederError {
        String[] values = line.split(",");

        if (values.length < 3) {
            throw new SeederError("invalid menu .csv file");
        }

        if (values[0].isEmpty() || values[1].isEmpty() || values[2].isEmpty()) {
            throw new SeederError("invalid menu .csv file");
        }

        if (values[0].equals("O") || values[0].equals("X") || values[0].equals("P") || values[0].equals("Q") ||
                values[0].equals("V")) {
            throw new SeederError("menu code can't be 0, X, P, Q, or V");
        }

        Menu menu = new Menu(UUID.randomUUID().toString(), values[0].trim(), values[1].trim(),
                Integer.parseInt(values[2].trim()));
        List<MenuVariant> menuVariants = new ArrayList<>();
        if (values.length > 3) {
            for (int i=3; i<values.length; i++) {
                if (this.variantsResult.containsKey(values[i])) {
                    menuVariants.add(new MenuVariant(menu.getCode(), values[i].trim()));
                }
            }
        }

        return new MenuWithVariants(menu, menuVariants);
    }
}
