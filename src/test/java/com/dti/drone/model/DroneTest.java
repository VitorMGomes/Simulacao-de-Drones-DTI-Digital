package com.dti.drone.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DroneTest {

    @Test
    void testDroneCreation() {
        Location baseLocation = new Location(-23.5505, -46.6333, "Base");
        Drone drone = new Drone("DRONE-001", 5.0, 50.0, 60.0, baseLocation);
        
        assertEquals("DRONE-001", drone.getId());
        assertEquals(5.0, drone.getMaxWeight());
        assertEquals(50.0, drone.getMaxRange());
        assertEquals(60.0, drone.getSpeed());
        assertEquals(Drone.Status.AVAILABLE, drone.getStatus());
        assertEquals(100.0, drone.getCurrentBatteryLevel());
        assertEquals(baseLocation, drone.getCurrentLocation());
    }

    @Test
    void testCanHandleDelivery() {
        Location baseLocation = new Location(-23.5505, -46.6333, "Base");
        Location pickupLocation = new Location(-23.5489, -46.6388, "Pickup");
        Location deliveryLocation = new Location(-23.5558, -46.6396, "Delivery");
        
        Drone drone = new Drone("DRONE-001", 5.0, 50.0, 60.0, baseLocation);
        Delivery delivery = new Delivery("DEL-001", pickupLocation, deliveryLocation, 
                3.0, Delivery.Priority.NORMAL, java.time.LocalDateTime.now().plusHours(2));
        
        assertTrue(drone.canHandleDelivery(delivery));
        
        // Test with overweight delivery
        Delivery heavyDelivery = new Delivery("DEL-002", pickupLocation, deliveryLocation, 
                10.0, Delivery.Priority.NORMAL, java.time.LocalDateTime.now().plusHours(2));
        
        assertFalse(drone.canHandleDelivery(heavyDelivery));
    }

    @Test
    void testBatteryLevelConstraints() {
        Location baseLocation = new Location(-23.5505, -46.6333, "Base");
        Drone drone = new Drone("DRONE-001", 5.0, 50.0, 60.0, baseLocation);
        
        drone.setCurrentBatteryLevel(150); // Should cap at 100
        assertEquals(100.0, drone.getCurrentBatteryLevel());
        
        drone.setCurrentBatteryLevel(-10); // Should not go below 0
        assertEquals(0.0, drone.getCurrentBatteryLevel());
    }
}