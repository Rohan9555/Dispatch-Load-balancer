package com.dispatch.dispatch_load_balancer.dto;

import com.dispatch.dispatch_load_balancer.Model.Vehicle;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

// DTO for receiving a list of vehicles in the request body
@Getter
@Setter
public class VehicleRequest {
    @NotEmpty(message = "vehicles list cannot be empty")
    private List<Vehicle> vehicles;
}