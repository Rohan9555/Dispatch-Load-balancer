package com.dispatch.dispatch_load_balancer.Repository;

import com.dispatch.dispatch_load_balancer.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}