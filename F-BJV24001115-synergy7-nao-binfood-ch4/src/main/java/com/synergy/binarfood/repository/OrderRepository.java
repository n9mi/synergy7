package com.synergy.binarfood.repository;

import com.synergy.binarfood.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByUserId(Pageable pageable, UUID userId);

    @Modifying
    @Query(value = "delete from orders", nativeQuery = true)
    void hardDeleteAll();
}
