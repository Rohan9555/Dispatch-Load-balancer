package com.dispatch.dispatch_load_balancer.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Model representing a single delivery order
@Getter
@Setter
@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {

    @Id
    private String orderId;
    private double latitude;
    private double longitude;
    private String address;
    private double packageWeight;
    private String priority; // HIGH, MEDIUM, LOW
}