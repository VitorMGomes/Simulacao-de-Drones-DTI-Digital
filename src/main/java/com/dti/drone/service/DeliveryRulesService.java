package com.dti.drone.service;

import com.dti.drone.model.Delivery;
import com.dti.drone.model.Drone;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * Service to validate delivery rules and constraints
 */
public class DeliveryRulesService {
    
    // Delivery operating hours
    private static final LocalTime OPERATING_START = LocalTime.of(6, 0); // 6:00 AM
    private static final LocalTime OPERATING_END = LocalTime.of(22, 0);   // 10:00 PM
    
    // Weather conditions that prevent delivery
    private static final List<String> UNSAFE_WEATHER = Arrays.asList(
        "THUNDERSTORM", "HEAVY_RAIN", "SEVERE_WIND", "FOG"
    );
    
    // Maximum flight time per drone (in hours)
    private static final double MAX_FLIGHT_TIME_HOURS = 4.0;
    
    // Minimum battery level for safe operation
    private static final double MIN_BATTERY_LEVEL = 20.0;
    
    /**
     * Validate if a delivery can be assigned to a drone
     */
    public boolean canAssignDelivery(Drone drone, Delivery delivery) {
        return isWithinOperatingHours() &&
               drone.canHandleDelivery(delivery) &&
               isWeatherSafe("CLEAR") && // Simplified - assume clear weather
               hasAdequateBattery(drone, delivery) &&
               isWithinFlightTimeLimit(drone, delivery);
    }
    
    /**
     * Check if current time is within operating hours
     */
    public boolean isWithinOperatingHours() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(OPERATING_START) && !now.isAfter(OPERATING_END);
    }
    
    /**
     * Check if weather conditions are safe for delivery
     */
    public boolean isWeatherSafe(String weatherCondition) {
        return !UNSAFE_WEATHER.contains(weatherCondition);
    }
    
    /**
     * Check if drone has adequate battery for delivery and return trip
     */
    public boolean hasAdequateBattery(Drone drone, Delivery delivery) {
        double requiredBattery = calculateRequiredBattery(drone, delivery);
        return drone.getCurrentBatteryLevel() >= requiredBattery + MIN_BATTERY_LEVEL;
    }
    
    /**
     * Check if delivery is within flight time limits
     */
    public boolean isWithinFlightTimeLimit(Drone drone, Delivery delivery) {
        double deliveryTime = drone.calculateDeliveryTime(delivery) / 60.0; // Convert to hours
        return deliveryTime <= MAX_FLIGHT_TIME_HOURS;
    }
    
    /**
     * Calculate required battery percentage for a delivery
     */
    public double calculateRequiredBattery(Drone drone, Delivery delivery) {
        double totalDistance = drone.getCurrentLocation().distanceTo(delivery.getPickupLocation()) +
                              delivery.getPickupLocation().distanceTo(delivery.getDeliveryLocation());
        
        // Simplified calculation: assume 1% battery per km
        return totalDistance;
    }
    
    /**
     * Validate delivery priority rules
     */
    public boolean isPriorityValid(Delivery delivery) {
        LocalDateTime now = LocalDateTime.now();
        
        // Urgent deliveries must be within 2 hours of deadline
        if (delivery.getPriority() == Delivery.Priority.URGENT) {
            return java.time.Duration.between(now, delivery.getDeadline()).toHours() <= 2;
        }
        
        // High priority deliveries must be within 6 hours of deadline
        if (delivery.getPriority() == Delivery.Priority.HIGH) {
            return java.time.Duration.between(now, delivery.getDeadline()).toHours() <= 6;
        }
        
        return true; // Normal and low priority have no time restrictions
    }
    
    /**
     * Check if drone needs maintenance before next delivery
     */
    public boolean needsMaintenance(Drone drone) {
        LocalDateTime lastMaintenance = drone.getLastMaintenanceDate();
        LocalDateTime now = LocalDateTime.now();
        
        // Require maintenance every 30 days
        return java.time.Duration.between(lastMaintenance, now).toDays() >= 30;
    }
}