package com.synergy.binfood.util.seeder;

import com.synergy.binfood.util.exception.InvalidSeederException;

import com.synergy.binfood.util.seeder.data.MerchantSeedData;
import com.synergy.binfood.util.seeder.data.ProductSeedData;
import com.synergy.binfood.util.seeder.data.UserSeedData;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

@Getter
public class CSVSeeder {
    private final String pathFormat = "%s/%s.csv";

    private final String folderPath;
    private final String userFileName;
    private final String merchantFileName;
    private final String productFileName;

    private final ArrayList<UserSeedData> userSeedData;
    private final ArrayList<MerchantSeedData> merchantSeedData;
    private final ArrayList<ProductSeedData> productSeedData;

    public CSVSeeder(String folderPath, String userSeederFileName, String merchantSeederFileName,
                     String productSeederFileName) {
        this.folderPath = folderPath;

        this.userFileName = userSeederFileName;
        this.merchantFileName = merchantSeederFileName;
        this.productFileName = productSeederFileName;

        this.userSeedData = new ArrayList<>();
        this.merchantSeedData = new ArrayList<>();
        this.productSeedData = new ArrayList<>();
    }

    public void seedUsers() throws InvalidSeederException {
        String userFilePath = String.format(this.pathFormat, this.folderPath, this.userFileName);
        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 3) {
                    throw new InvalidSeederException("invalid user seeder");
                }
                this.userSeedData.add(new UserSeedData(values[0], values[1], values[2]));
            }
        } catch (FileNotFoundException e) {
            throw new InvalidSeederException("user seeder not found");
        } catch (Exception e) {
            throw new InvalidSeederException("failed to read user csv : " + e.getMessage());
        }
    }

    public void seedMerchants() throws InvalidSeederException {
        String merchantFilePath = String.format(this.pathFormat, this.folderPath, this.merchantFileName);
        try (BufferedReader br = new BufferedReader(new FileReader(merchantFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 3) {
                    throw new InvalidSeederException("invalid merchant seeder");
                }
                this.merchantSeedData.add(new MerchantSeedData(values[0], values[1], values[2].equals("1")));
            }
        } catch (FileNotFoundException e) {
            throw new InvalidSeederException("merchant seeder not found");
        } catch (Exception e) {
            throw new InvalidSeederException("failed to read merchant csv : " + e.getMessage());
        }
    }

    public void seedProducts() throws InvalidSeederException {
        String productFilePath = String.format(this.pathFormat, this.folderPath, this.productFileName);
        try (BufferedReader br = new BufferedReader(new FileReader(productFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 3) {
                    throw new InvalidSeederException("invalid product seeder");
                }
                this.productSeedData.add(new ProductSeedData(values[0], Integer.parseInt(values[1]),
                        Integer.parseInt(values[2])));
            }
        } catch (FileNotFoundException e) {
            throw new InvalidSeederException("product seeder not found");
        } catch (Exception e) {
            throw new InvalidSeederException("failed to read product csv : " + e.getMessage());
        }
    }
}
