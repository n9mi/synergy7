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
@Table(name = "users")
@SQLDelete(sql = "update users set deleted_date = now() where id = ?")
@SQLRestriction("deleted_date is null")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    private String email;

    private String password;

    private String token;

    @OneToMany(mappedBy = "user", cascade = { CascadeType.ALL })
    private List<Order> orders;
}
