package com.synergy.binarfood.core.repository;

import com.synergy.binarfood.core.entity.Order;
import com.synergy.binarfood.core.entity.OrderDetail;
import com.synergy.binarfood.core.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    boolean existsByOrderAndProduct(Order order, Product product);

    Optional<OrderDetail> findByOrderAndProduct(Order order, Product product);

    Optional<OrderDetail> findByOrderAndId(Order order, UUID Id);

    List<OrderDetail> findAllByOrder(Order order);

    @Modifying
    @Query(value = "delete from order_details", nativeQuery = true)
    void hardDeleteAll();
}
