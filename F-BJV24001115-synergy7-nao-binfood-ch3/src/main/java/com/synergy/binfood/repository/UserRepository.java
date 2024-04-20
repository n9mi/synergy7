package com.synergy.binfood.repository;

import lombok.NoArgsConstructor;

import com.synergy.binfood.entity.User;

@NoArgsConstructor
public class UserRepository {
    public boolean isExistsByUsername(String username) {
        return Repository.users.values().stream().anyMatch(user ->
                user.getUsername().equals(username));
    }

    public User findByUserName(String username) {
        return Repository.users.values().stream().filter(user ->
                user.getUsername().equals(username)).
                findFirst().orElse(null);
    }

    public boolean isUserExistByIdAndUsername(int id, String username) {
        return Repository.users.containsKey(id) &&
                (Repository.users.get(id).getUsername().equals(username));
    }
}
