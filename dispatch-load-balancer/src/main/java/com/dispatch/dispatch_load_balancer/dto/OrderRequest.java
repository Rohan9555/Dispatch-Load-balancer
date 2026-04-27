package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.Model.DeliveryOrder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

// DTO for receiving a list of delivery orders in the request body
@Getter
@Setter
public class OrderRequest {
    private List<DeliveryOrder> orders;
}