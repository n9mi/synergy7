package com.synergy.binfood.model.order;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class OrderResponse {
    private int id;
    private String destinationAddress;
    private String createdAt;
    private boolean completed;
    private String completedAt;
    private List<OrderDetailResponse> orderDetails;

    public OrderResponse(int id, String destinationAddress, Date createdAt,
                         boolean completed, Date completedAt) {
        this.id = id;
        this.destinationAddress = destinationAddress;
        this.createdAt = new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(createdAt);
        this.completed = completed;
        this.completedAt = Optional.of(new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(createdAt)).get();
    }
}
