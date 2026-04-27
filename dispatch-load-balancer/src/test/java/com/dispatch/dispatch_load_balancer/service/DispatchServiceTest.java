package com.dispatch.dispatch_load_balancer.service;

import com.dispatch.dispatch_load_balancer.Model.DeliveryOrder;
import com.dispatch.dispatch_load_balancer.Model.Vehicle;
import com.dispatch.dispatch_load_balancer.Repository.OrderRepository;
import com.dispatch.dispatch_load_balancer.Repository.VehicleRepository;
import com.dispatch.dispatch_load_balancer.Service.DispatchService;
import com.dispatch.dispatch_load_balancer.dto.VehiclePlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
        import java.util.*;
        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class DispatchServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DispatchService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private DeliveryOrder createOrder(String id, double weight, String priority, double lat, double lon) {
        DeliveryOrder o = new DeliveryOrder();
        o.setOrderId(id);
        o.setPackageWeight(weight);
        o.setPriority(priority);
        o.setLatitude(lat);
        o.setLongitude(lon);
        return o;
    }

    private Vehicle createVehicle(String id, double capacity, double lat, double lon) {
        Vehicle v = new Vehicle();
        v.setVehicleId(id);
        v.setCapacity(capacity);
        v.setCurrentLatitude(lat);
        v.setCurrentLongitude(lon);
        return v;
    }

    @Test
    void shouldAssignOrderToVehicle() {
        DeliveryOrder order = createOrder("ORD1", 10, "HIGH", 28.6139, 77.2090);
        Vehicle vehicle = createVehicle("VEH1", 50, 28.6139, 77.2090);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>(List.of(vehicle)));
        when(vehicleRepository.saveAll(any())).thenReturn(List.of(vehicle));

        List<VehiclePlan> result = service.generateDispatchPlan();

        assertEquals(1, result.get(0).getAssignedOrders().size());
        verify(vehicleRepository).saveAll(any());
    }

    @Test
    void shouldNotAssignIfCapacityExceeded() {
        DeliveryOrder order = createOrder("ORD1", 100, "HIGH", 28.6139, 77.2090);
        Vehicle vehicle = createVehicle("VEH1", 50, 28.6139, 77.2090);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>(List.of(vehicle)));

        List<VehiclePlan> result = service.generateDispatchPlan();

        assertEquals(0, result.get(0).getAssignedOrders().size());
    }

    @Test
    void shouldAssignHighPriorityFirst() {
        DeliveryOrder high = createOrder("ORD1", 10, "HIGH", 28.6139, 77.2090);
        DeliveryOrder low = createOrder("ORD2", 10, "LOW", 28.6139, 77.2090);

        Vehicle vehicle = createVehicle("VEH1", 50, 28.6139, 77.2090);

        when(orderRepository.findAll()).thenReturn(List.of(low, high));
        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>(List.of(vehicle)));

        List<VehiclePlan> result = service.generateDispatchPlan();

        assertEquals("HIGH", result.get(0).getAssignedOrders().get(0).getPriority());
    }

    @Test
    void shouldAssignToNearestVehicle() {
        DeliveryOrder order = createOrder("ORD1", 10, "HIGH", 28.6139, 77.2090);

        Vehicle far = createVehicle("FAR", 50, 28.7041, 77.1025);
        Vehicle near = createVehicle("NEAR", 50, 28.6139, 77.2090);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>(List.of(far, near)));

        List<VehiclePlan> result = service.generateDispatchPlan();

        VehiclePlan assigned = result.stream()
                .filter(v -> !v.getAssignedOrders().isEmpty())
                .findFirst()
                .orElse(null);

        assertNotNull(assigned);
        assertEquals("NEAR", assigned.getVehicleId());
    }

    @Test
    void shouldUpdateVehicleLocationAfterAssignment() {
        DeliveryOrder order = createOrder("ORD1", 10, "HIGH", 28.6139, 77.2090);
        Vehicle vehicle = createVehicle("VEH1", 50, 28.7041, 77.1025);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>(List.of(vehicle)));

        service.generateDispatchPlan();

        assertEquals(28.6139, vehicle.getCurrentLatitude());
        assertEquals(77.2090, vehicle.getCurrentLongitude());
    }

    @Test
    void shouldThrowExceptionWhenNoOrders() {
        when(orderRepository.findAll()).thenReturn(List.of());
        when(vehicleRepository.findAll()).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> service.generateDispatchPlan());
    }

    @Test
    void shouldThrowExceptionWhenNoVehicles() {
        DeliveryOrder order = createOrder("ORD1", 10, "HIGH", 28.6139, 77.2090);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(vehicleRepository.findAll()).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> service.generateDispatchPlan());
    }

    @Test
    void shouldCalculateDistance() {
        DeliveryOrder order = createOrder("ORD1", 10, "HIGH", 28.6139, 77.2090);
        Vehicle vehicle = createVehicle("VEH1", 50, 28.7041, 77.1025);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(vehicleRepository.findAll()).thenReturn(new ArrayList<>(List.of(vehicle)));

        List<VehiclePlan> result = service.generateDispatchPlan();

        String distance = result.get(0).getTotalDistance();
        assertTrue(distance.contains("km"));
    }
}