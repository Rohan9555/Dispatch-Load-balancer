package com.dispatch.dispatch_load_balancer.Repository;

import com.dispatch.dispatch_load_balancer.Model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<DeliveryOrder, String> {
}