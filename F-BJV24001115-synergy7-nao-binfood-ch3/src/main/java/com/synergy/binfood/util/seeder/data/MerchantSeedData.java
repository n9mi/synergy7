package com.synergy.binfood.util.seeder.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class MerchantSeedData {
    private String merchantName;
    private String merchantLocation;
    private boolean merchantOpen;
}
