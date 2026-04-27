package com.dispatch.dispatch_load_balancer.Controllers;

import com.dispatch.dispatch_load_balancer.Service.DispatchService;
import com.dispatch.dispatch_load_balancer.dto.ApiResponse;
import com.dispatch.dispatch_load_balancer.dto.DispatchResponse;
import com.dispatch.dispatch_load_balancer.dto.OrderRequest;
import com.dispatch.dispatch_load_balancer.dto.VehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    @Autowired
    private DispatchService service;

    // Accept incoming orders
    @PostMapping("/orders")
    public ApiResponse addOrders(@RequestBody OrderRequest request) {
        if (request.getOrders() == null || request.getOrders().isEmpty())
            throw new IllegalArgumentException("Orders list cannot be empty");
        service.addOrders(request.getOrders());
        return new ApiResponse("Delivery orders accepted.", "success");
    }

    // Register available vehicles
    @PostMapping("/vehicles")
    public ApiResponse addVehicles(@RequestBody VehicleRequest request) {
        if (request.getVehicles() == null || request.getVehicles().isEmpty())
            throw new IllegalArgumentException("Vehicles list cannot be empty");
        service.addVehicles(request.getVehicles());
        return new ApiResponse("Vehicle details accepted.", "success");
    }

    // Generate and return the dispatch plan
    @GetMapping("/plan")
    public DispatchResponse getPlan() {
        return new DispatchResponse(service.generateDispatchPlan());
    }
}