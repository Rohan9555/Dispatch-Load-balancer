package com.dispatch.dispatch_load_balancer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

// Response wrapper for the generated dispatch plan
@Data
@AllArgsConstructor
public class DispatchResponse {
    private List<VehiclePlan> dispatchPlan;
}