package com.synergy.binarfood.repository;

import com.synergy.binarfood.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findFirstByToken(String token);

    @Query(value = "select u.id, u.username, u.email, u.created_date, count(o.id) as total_order from users u\n"
            + "inner join orders o on o.user_id = u.id\n"
            + "where u.token is not null\n"
            + "group by u.id\n"
            + "order by total_order desc",
            nativeQuery = true)
    Page<Object[]> findAllWithOrderCount(Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findFirstById(UUID id);

    @Modifying
    @Query(value = "delete from users", nativeQuery = true)
    void hardDeleteAll();

    @Procedure(procedureName = "generate_dummy_user")
    void generateDummyUser();

    @Procedure(procedureName = "authorize_user")
    void authorizeUser(UUID id);

    @Procedure(procedureName = "clean_dummy_users")
    void cleanDummyUsers();
}
