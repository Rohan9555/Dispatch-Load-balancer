package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.Model.DeliveryOrder;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.util.List;

// Represents the dispatch plan for a single vehicle
@JsonPropertyOrder({ "vehicleId", "totalLoad", "totalDistance", "assignedOrders" })
@Data
public class VehiclePlan {
    private String vehicleId;
    private double totalLoad;
    private String totalDistance;
    private List<DeliveryOrder> assignedOrders;
}