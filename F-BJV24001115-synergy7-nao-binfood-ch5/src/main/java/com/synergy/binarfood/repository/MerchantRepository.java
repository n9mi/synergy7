package com.synergy.binarfood.repository;

import com.synergy.binarfood.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    @Query(value = "select * from merchants m where m.open is true", nativeQuery = true)
    Page<Merchant> findOpenedMerchants(Pageable pageable);

    Page<Merchant> findAllByUser_Username(String username, Pageable pageable);

    boolean existsByIdAndOpen(UUID id, boolean open);

    @Query(value =
            """
                    select p.id, p.name, p.price, sum(od.quantity) as quantity, sum(od.total_price) from products p
                    inner join order_details od on od.product_id = p.id
                    inner join merchants m on m.id = p.merchant_id
                    where m.id = :merchantId
                    and od.created_date >= :dateFrom
                    and od.created_date <= :dateTo
                    group by p.id order by quantity desc""", nativeQuery = true)
    List<Object[]> getIncomeByProductWithinDateRange(
            @Param("merchantId") UUID merchantId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);

    @Modifying
    @Query(value = "delete from merchants", nativeQuery = true)
    void hardDeleteAll();
}
