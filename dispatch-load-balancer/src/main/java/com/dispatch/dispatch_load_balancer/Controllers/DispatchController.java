package com.dispatch.dispatch_load_balancer.Controllers;

import com.dispatch.dispatch_load_balancer.Service.DispatchService;
import com.dispatch.dispatch_load_balancer.dto.ApiResponse;
import com.dispatch.dispatch_load_balancer.dto.DispatchResponse;
import com.dispatch.dispatch_load_balancer.dto.OrderRequest;
import com.dispatch.dispatch_load_balancer.dto.VehicleRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    @Autowired
    private DispatchService service;

    // Accept incoming orders
    @PostMapping("/orders")
    public ApiResponse addOrders(@Valid @RequestBody OrderRequest request) {
        service.addOrders(request.getOrders());
        return new ApiResponse("Delivery orders accepted.", "success");
    }

    // Register available vehicles
    @PostMapping("/vehicles")
    public ApiResponse addVehicles(@Valid @RequestBody VehicleRequest request) {
        service.addVehicles(request.getVehicles());
        return new ApiResponse("Vehicle details accepted.", "success");
    }

    // Generate and return the dispatch plan
    @GetMapping("/plan")
    public DispatchResponse getPlan() {
        return service.generateDispatchPlan();
    }
}