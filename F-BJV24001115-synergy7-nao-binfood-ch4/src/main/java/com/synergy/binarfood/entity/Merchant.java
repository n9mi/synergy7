package com.synergy.binarfood.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchants")
@SQLDelete(sql = "update merchants set deleted_date = now() where id = ?")
@SQLRestriction("deleted_date is null")
public class Merchant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    private boolean isOpen;

    @OneToMany(mappedBy = "merchant", cascade = { CascadeType.ALL })
    private List<Product> products;
}
