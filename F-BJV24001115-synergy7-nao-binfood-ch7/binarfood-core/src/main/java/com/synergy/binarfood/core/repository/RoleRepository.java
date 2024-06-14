package com.synergy.binarfood.core.repository;

import com.synergy.binarfood.core.entity.ERole;
import com.synergy.binarfood.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole name);

    boolean existsByName(String name);

    @Modifying
    @Query(value = "delete from roles", nativeQuery = true)
    void hardDeleteAll();
}

