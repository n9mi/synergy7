package com.synergy.binfood.util.seeder.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserSeedData {
    private String username;
    private String emailAddress;
    private String password;
}
