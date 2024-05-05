package com.synergy.binarfood.repository;

import com.synergy.binarfood.entity.Order;
import com.synergy.binarfood.entity.OrderDetail;
import com.synergy.binarfood.entity.Product;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID>  {

    @Modifying
    @Query(value = "delete from order_details", nativeQuery = true)
    void hardDeleteAll();

    boolean existsByOrderAndProduct(Order order, Product product);

    Optional<OrderDetail> findByOrderAndId(Order order, UUID Id);
}
