package com.dti.drone.algorithm;

import com.dti.drone.model.Delivery;
import com.dti.drone.model.Drone;
import java.util.List;
import java.util.Map;

/**
 * Interface for drone allocation algorithms
 */
public interface AllocationAlgorithm {
    
    /**
     * Allocate deliveries to available drones
     * @param deliveries list of pending deliveries
     * @param drones list of available drones
     * @return map of drone to assigned deliveries
     */
    Map<Drone, List<Delivery>> allocate(List<Delivery> deliveries, List<Drone> drones);
}