package com.dti.drone.simulation;

import com.dti.drone.algorithm.AllocationAlgorithm;
import com.dti.drone.model.Delivery;
import com.dti.drone.model.Drone;
import com.dti.drone.service.DeliveryRulesService;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main simulation engine that orchestrates drone operations
 */
public class DroneSimulationEngine {
    
    private final List<Drone> drones;
    private final List<Delivery> pendingDeliveries;
    private final List<Delivery> completedDeliveries;
    private final AllocationAlgorithm allocationAlgorithm;
    private final DeliveryRulesService deliveryRulesService;
    private final SimulationStatistics statistics;
    
    public DroneSimulationEngine(AllocationAlgorithm algorithm) {
        this.drones = new ArrayList<>();
        this.pendingDeliveries = new ArrayList<>();
        this.completedDeliveries = new ArrayList<>();
        this.allocationAlgorithm = algorithm;
        this.deliveryRulesService = new DeliveryRulesService();
        this.statistics = new SimulationStatistics();
    }
    
    /**
     * Add a drone to the fleet
     */
    public void addDrone(Drone drone) {
        drones.add(drone);
    }
    
    /**
     * Add a delivery request
     */
    public void addDelivery(Delivery delivery) {
        if (deliveryRulesService.isPriorityValid(delivery)) {
            pendingDeliveries.add(delivery);
            statistics.incrementTotalRequests();
        }
    }
    
    /**
     * Run one simulation step - allocate deliveries and update drone states
     */
    public void simulateStep() {
        // Filter available drones (not in maintenance, adequate battery, etc.)
        List<Drone> availableDrones = getAvailableDrones();
        
        // Filter valid deliveries that can be processed
        List<Delivery> validDeliveries = getValidDeliveries();
        
        if (!availableDrones.isEmpty() && !validDeliveries.isEmpty()) {
            // Allocate deliveries to drones
            Map<Drone, List<Delivery>> allocation = allocationAlgorithm.allocate(validDeliveries, availableDrones);
            
            // Execute allocations
            executeAllocations(allocation);
        }
        
        // Update drone states and complete deliveries
        updateDroneStates();
        
        // Update statistics
        updateStatistics();
    }
    
    private List<Drone> getAvailableDrones() {
        return drones.stream()
                .filter(drone -> drone.getStatus() == Drone.Status.AVAILABLE)
                .filter(drone -> !deliveryRulesService.needsMaintenance(drone))
                .filter(drone -> drone.getCurrentBatteryLevel() > 20)
                .collect(Collectors.toList());
    }
    
    private List<Delivery> getValidDeliveries() {
        return pendingDeliveries.stream()
                .filter(delivery -> delivery.getStatus() == Delivery.Status.PENDING)
                .filter(delivery -> deliveryRulesService.isWithinOperatingHours())
                .collect(Collectors.toList());
    }
    
    private void executeAllocations(Map<Drone, List<Delivery>> allocation) {
        for (Map.Entry<Drone, List<Delivery>> entry : allocation.entrySet()) {
            Drone drone = entry.getKey();
            List<Delivery> deliveries = entry.getValue();
            
            for (Delivery delivery : deliveries) {
                if (deliveryRulesService.canAssignDelivery(drone, delivery)) {
                    assignDeliveryToDrone(drone, delivery);
                }
            }
        }
    }
    
    private void assignDeliveryToDrone(Drone drone, Delivery delivery) {
        drone.assignDelivery(delivery);
        drone.setStatus(Drone.Status.IN_TRANSIT);
        delivery.setStatus(Delivery.Status.ASSIGNED);
        delivery.setAssignedDroneId(drone.getId());
        
        pendingDeliveries.remove(delivery);
        statistics.incrementAssignedDeliveries();
    }
    
    private void updateDroneStates() {
        for (Drone drone : drones) {
            if (drone.getStatus() == Drone.Status.IN_TRANSIT) {
                // Simulate delivery completion (simplified)
                List<Delivery> droneDeliveries = drone.getAssignedDeliveries();
                if (!droneDeliveries.isEmpty()) {
                    // Complete first delivery
                    Delivery delivery = droneDeliveries.get(0);
                    completeDelivery(drone, delivery);
                }
            }
        }
    }
    
    private void completeDelivery(Drone drone, Delivery delivery) {
        delivery.setStatus(Delivery.Status.DELIVERED);
        delivery.setDeliveryTime(java.time.LocalDateTime.now());
        
        drone.removeDelivery(delivery);
        completedDeliveries.add(delivery);
        
        // Update drone battery (simplified)
        double batteryUsed = deliveryRulesService.calculateRequiredBattery(drone, delivery);
        drone.setCurrentBatteryLevel(drone.getCurrentBatteryLevel() - batteryUsed);
        
        if (drone.getAssignedDeliveries().isEmpty()) {
            drone.setStatus(Drone.Status.AVAILABLE);
        }
        
        statistics.incrementCompletedDeliveries();
    }
    
    private void updateStatistics() {
        statistics.setActiveDrones(drones.stream()
                .mapToInt(drone -> drone.getStatus() != Drone.Status.AVAILABLE ? 1 : 0)
                .sum());
        statistics.setPendingDeliveries(pendingDeliveries.size());
    }
    
    /**
     * Get current simulation statistics
     */
    public SimulationStatistics getStatistics() {
        return statistics;
    }
    
    /**
     * Get list of all drones
     */
    public List<Drone> getDrones() {
        return new ArrayList<>(drones);
    }
    
    /**
     * Get list of pending deliveries
     */
    public List<Delivery> getPendingDeliveries() {
        return new ArrayList<>(pendingDeliveries);
    }
    
    /**
     * Get list of completed deliveries
     */
    public List<Delivery> getCompletedDeliveries() {
        return new ArrayList<>(completedDeliveries);
    }
}