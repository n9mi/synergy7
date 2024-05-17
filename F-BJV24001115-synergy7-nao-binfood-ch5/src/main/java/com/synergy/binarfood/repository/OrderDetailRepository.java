package com.synergy.binarfood.repository;

import com.synergy.binarfood.entity.Order;
import com.synergy.binarfood.entity.OrderDetail;
import com.synergy.binarfood.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    boolean existsByOrderAndProduct(Order order, Product product);

    Optional<OrderDetail> findByOrderAndId(Order order, UUID Id);

    Optional<OrderDetail> findByOrderAndProduct(Order order, Product product);

    Page<OrderDetail> findAllByOrder(Order order, Pageable pageable);

    @Modifying
    @Query(value = "delete from order_details", nativeQuery = true)
    void hardDeleteAll();
}
