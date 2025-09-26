package com.dti.drone.service;

import com.dti.drone.model.Delivery;
import com.dti.drone.model.Drone;
import com.dti.drone.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryRulesServiceTest {

    private DeliveryRulesService deliveryRulesService;
    private Drone drone;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        deliveryRulesService = new DeliveryRulesService();
        
        Location baseLocation = new Location(-23.5505, -46.6333, "Base");
        Location pickupLocation = new Location(-23.5489, -46.6388, "Pickup");
        Location deliveryLocation = new Location(-23.5558, -46.6396, "Delivery");
        
        drone = new Drone("DRONE-001", 5.0, 50.0, 60.0, baseLocation);
        delivery = new Delivery("DEL-001", pickupLocation, deliveryLocation, 
                2.5, Delivery.Priority.NORMAL, LocalDateTime.now().plusHours(4));
    }

    @Test
    void testCanAssignDelivery() {
        // Test with valid conditions
        assertTrue(deliveryRulesService.canAssignDelivery(drone, delivery));
        
        // Test with low battery
        drone.setCurrentBatteryLevel(10);
        assertFalse(deliveryRulesService.canAssignDelivery(drone, delivery));
    }

    @Test
    void testHasAdequateBattery() {
        assertTrue(deliveryRulesService.hasAdequateBattery(drone, delivery));
        
        drone.setCurrentBatteryLevel(25); // Should still be adequate
        assertTrue(deliveryRulesService.hasAdequateBattery(drone, delivery));
        
        drone.setCurrentBatteryLevel(10); // Not adequate
        assertFalse(deliveryRulesService.hasAdequateBattery(drone, delivery));
    }

    @Test
    void testCalculateRequiredBattery() {
        double requiredBattery = deliveryRulesService.calculateRequiredBattery(drone, delivery);
        assertTrue(requiredBattery > 0);
        assertTrue(requiredBattery < 100); // Should be reasonable
    }

    @Test
    void testPriorityValidation() {
        // Normal priority should always be valid
        assertTrue(deliveryRulesService.isPriorityValid(delivery));
        
        // Urgent delivery with reasonable deadline
        Delivery urgentDelivery = new Delivery("DEL-URGENT", 
                delivery.getPickupLocation(), delivery.getDeliveryLocation(),
                2.0, Delivery.Priority.URGENT, LocalDateTime.now().plusMinutes(90));
        
        assertTrue(deliveryRulesService.isPriorityValid(urgentDelivery));
    }
}