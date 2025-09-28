package com.dti.drone.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void testLocationCreation() {
        Location location = new Location(-23.5505, -46.6333, "São Paulo");
        
        assertEquals(-23.5505, location.getLatitude());
        assertEquals(-46.6333, location.getLongitude());
        assertEquals("São Paulo", location.getAddress());
    }

    @Test
    void testDistanceCalculation() {
        Location location1 = new Location(-23.5505, -46.6333, "Point A");
        Location location2 = new Location(-23.5489, -46.6388, "Point B");
        
        double distance = location1.distanceTo(location2);
        assertTrue(distance > 0);
        assertTrue(distance < 10); // Should be a small distance in São Paulo
    }

    @Test
    void testLocationEquality() {
        Location location1 = new Location(-23.5505, -46.6333, "São Paulo");
        Location location2 = new Location(-23.5505, -46.6333, "Different Name");
        Location location3 = new Location(-23.5506, -46.6333, "São Paulo");
        
        assertEquals(location1, location2); // Same coordinates
        assertNotEquals(location1, location3); // Different coordinates
    }
}