package com.dispatch.dispatch_load_balancer.Service;

import com.dispatch.dispatch_load_balancer.Model.DeliveryOrder;
import com.dispatch.dispatch_load_balancer.Model.Vehicle;
import com.dispatch.dispatch_load_balancer.Repository.OrderRepository;
import com.dispatch.dispatch_load_balancer.Repository.VehicleRepository;
import com.dispatch.dispatch_load_balancer.dto.VehiclePlan;
import com.dispatch.dispatch_load_balancer.util.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DispatchService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public void addOrders(List<DeliveryOrder> deliveryOrderList) {
        // upsert — clears old data and saves new
        orderRepository.deleteAll();
        orderRepository.saveAll(deliveryOrderList);
    }

    public void addVehicles(List<Vehicle> vehicleList) {
        // upsert — clears old data and saves new
        vehicleRepository.deleteAll();
        vehicleRepository.saveAll(vehicleList);
    }

    public List<VehiclePlan> generateDispatchPlan() {
        List<DeliveryOrder> deliveryOrders = orderRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();

        if (deliveryOrders.isEmpty())
            throw new IllegalArgumentException("No orders found. Please add orders first.");
        if (vehicles.isEmpty())
            throw new IllegalArgumentException("No vehicles found. Please add vehicles first.");

        // Sort orders by priority: HIGH → MEDIUM → LOW
        List<DeliveryOrder> sortedOrders = deliveryOrders.stream()
                .sorted(Comparator.comparing(this::priorityValue))
                .toList();

        for (DeliveryOrder order : sortedOrders) {
            Vehicle bestVehicle = null;
            double minDistance = Double.MAX_VALUE;

            // Find the nearest vehicle with enough remaining capacity
            for (Vehicle vehicle : vehicles) {
                if (vehicle.getCurrentLoad() + order.getPackageWeight() > vehicle.getCapacity())
                    continue;

                double distance = DistanceUtil.calculateDistance(
                        vehicle.getCurrentLatitude(),
                        vehicle.getCurrentLongitude(),
                        order.getLatitude(),
                        order.getLongitude()
                );

                if (distance < minDistance) {
                    minDistance = distance;
                    bestVehicle = vehicle;
                }
            }

            if (bestVehicle != null) {
                bestVehicle.getAssignedOrders().add(order);
                bestVehicle.setCurrentLoad(bestVehicle.getCurrentLoad() + order.getPackageWeight());
                bestVehicle.setCurrentLatitude(order.getLatitude());
                bestVehicle.setCurrentLongitude(order.getLongitude());
            }
        }

        // Save updated vehicle state to DB
        vehicleRepository.saveAll(vehicles);

        // Build the response plan for each vehicle
        List<VehiclePlan> result = new ArrayList<>();

        for (Vehicle v : vehicles) {
            VehiclePlan vp = new VehiclePlan();
            vp.setVehicleId(v.getVehicleId());
            vp.setTotalLoad(v.getCurrentLoad());

            // Calculate total route distance using Haversine
            double totalDistance = 0;
            double prevLat = v.getCurrentLatitude();
            double prevLon = v.getCurrentLongitude();

            for (DeliveryOrder o : v.getAssignedOrders()) {
                totalDistance += DistanceUtil.calculateDistance(
                        prevLat, prevLon,
                        o.getLatitude(), o.getLongitude()
                );
                prevLat = o.getLatitude();
                prevLon = o.getLongitude();
            }

            vp.setTotalDistance(String.format("%.2f km", totalDistance));
            vp.setAssignedOrders(v.getAssignedOrders());
            result.add(vp);
        }

        return result;
    }

    // Maps priority string to sort order
    private int priorityValue(DeliveryOrder order) {
        switch (order.getPriority()) {
            case "HIGH": return 1;
            case "MEDIUM": return 2;
            default: return 3;
        }
    }
}