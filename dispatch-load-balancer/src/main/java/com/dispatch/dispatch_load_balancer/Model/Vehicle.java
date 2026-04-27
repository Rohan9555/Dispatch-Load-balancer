package com.dispatch.dispatch_load_balancer.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

// Model representing a vehicle in the fleet
@Getter
@Setter
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    private String vehicleId;
    private double capacity;
    private double currentLatitude;
    private double currentLongitude;
    private String currentAddress;
    private double currentLoad = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private List<DeliveryOrder> assignedOrders = new ArrayList<>();
}