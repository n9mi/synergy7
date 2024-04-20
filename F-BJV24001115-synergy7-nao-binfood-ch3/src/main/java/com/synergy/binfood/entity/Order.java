package com.synergy.binfood.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class Order {
    private int id;
    private int userId;
    private String destinationAddress;
    private boolean completed;
    private Date createdAt;
    private Date completedAt;

    public Order(int userId, String destinationAddress) {
        this.userId = userId;
        this.destinationAddress = destinationAddress;
    }
}
