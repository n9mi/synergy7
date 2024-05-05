package com.synergy.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@SQLDelete(sql = "update orders set deleted_date = now() where id = ?")
@SQLRestriction("deleted_date is null")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date orderAt;

    private String destinationAddress;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @OneToMany(mappedBy = "order", cascade = { CascadeType.ALL })
    private List<OrderDetail> orderDetails;

    private Date completedAt;
}
