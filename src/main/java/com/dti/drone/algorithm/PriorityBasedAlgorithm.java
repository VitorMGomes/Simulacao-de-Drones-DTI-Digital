package com.dti.drone.algorithm;

import com.dti.drone.model.Delivery;
import com.dti.drone.model.Drone;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Priority-based allocation algorithm - prioritizes deliveries based on urgency and drone efficiency
 */
public class PriorityBasedAlgorithm implements AllocationAlgorithm {

    @Override
    public Map<Drone, List<Delivery>> allocate(List<Delivery> deliveries, List<Drone> drones) {
        Map<Drone, List<Delivery>> allocation = new HashMap<>();
        
        // Initialize empty lists for each drone
        for (Drone drone : drones) {
            allocation.put(drone, new ArrayList<>());
        }
        
        // Sort deliveries by urgency score (highest first)
        List<Delivery> sortedDeliveries = deliveries.stream()
                .sorted((d1, d2) -> Double.compare(d2.getUrgencyScore(), d1.getUrgencyScore()))
                .collect(Collectors.toList());
        
        // For each delivery, find the most efficient drone
        for (Delivery delivery : sortedDeliveries) {
            Drone bestDrone = findMostEfficientDrone(delivery, drones);
            if (bestDrone != null) {
                allocation.get(bestDrone).add(delivery);
                delivery.setAssignedDroneId(bestDrone.getId());
            }
        }
        
        return allocation;
    }
    
    private Drone findMostEfficientDrone(Delivery delivery, List<Drone> drones) {
        return drones.stream()
                .filter(drone -> drone.canHandleDelivery(delivery))
                .min((d1, d2) -> {
                    double efficiency1 = calculateEfficiency(d1, delivery);
                    double efficiency2 = calculateEfficiency(d2, delivery);
                    return Double.compare(efficiency1, efficiency2);
                })
                .orElse(null);
    }
    
    private double calculateEfficiency(Drone drone, Delivery delivery) {
        double deliveryTime = drone.calculateDeliveryTime(delivery);
        double batteryUsage = calculateBatteryUsage(drone, delivery);
        double distance = drone.getCurrentLocation().distanceTo(delivery.getPickupLocation());
        
        // Lower score = more efficient (less time, battery usage, and distance)
        return deliveryTime * 0.4 + batteryUsage * 0.3 + distance * 0.3;
    }
    
    private double calculateBatteryUsage(Drone drone, Delivery delivery) {
        double totalDistance = drone.getCurrentLocation().distanceTo(delivery.getPickupLocation()) +
                              delivery.getPickupLocation().distanceTo(delivery.getDeliveryLocation());
        // Assume 1% battery per km (simplified calculation)
        return totalDistance;
    }
}