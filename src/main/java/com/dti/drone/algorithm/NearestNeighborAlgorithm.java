package com.dti.drone.algorithm;

import com.dti.drone.model.Delivery;
import com.dti.drone.model.Drone;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Nearest neighbor allocation algorithm - assigns deliveries to the closest available drone
 */
public class NearestNeighborAlgorithm implements AllocationAlgorithm {

    @Override
    public Map<Drone, List<Delivery>> allocate(List<Delivery> deliveries, List<Drone> drones) {
        Map<Drone, List<Delivery>> allocation = new HashMap<>();
        
        // Initialize empty lists for each drone
        for (Drone drone : drones) {
            allocation.put(drone, new ArrayList<>());
        }
        
        // Sort deliveries by urgency (highest first)
        List<Delivery> sortedDeliveries = deliveries.stream()
                .sorted((d1, d2) -> Double.compare(d2.getUrgencyScore(), d1.getUrgencyScore()))
                .collect(Collectors.toList());
        
        // Assign each delivery to the nearest capable drone
        for (Delivery delivery : sortedDeliveries) {
            Drone bestDrone = findNearestCapableDrone(delivery, drones);
            if (bestDrone != null) {
                allocation.get(bestDrone).add(delivery);
                delivery.setAssignedDroneId(bestDrone.getId());
            }
        }
        
        return allocation;
    }
    
    private Drone findNearestCapableDrone(Delivery delivery, List<Drone> drones) {
        return drones.stream()
                .filter(drone -> drone.canHandleDelivery(delivery))
                .min((d1, d2) -> {
                    double dist1 = d1.getCurrentLocation().distanceTo(delivery.getPickupLocation());
                    double dist2 = d2.getCurrentLocation().distanceTo(delivery.getPickupLocation());
                    return Double.compare(dist1, dist2);
                })
                .orElse(null);
    }
}