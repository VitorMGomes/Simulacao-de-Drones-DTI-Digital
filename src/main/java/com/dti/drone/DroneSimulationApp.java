package com.dti.drone;

import com.dti.drone.algorithm.NearestNeighborAlgorithm;
import com.dti.drone.algorithm.PriorityBasedAlgorithm;
import com.dti.drone.model.Delivery;
import com.dti.drone.model.Drone;
import com.dti.drone.model.Location;
import com.dti.drone.simulation.DroneSimulationEngine;
import java.time.LocalDateTime;

/**
 * Main application for Urban Drone Simulation
 */
public class DroneSimulationApp {
    
    public static void main(String[] args) {
        System.out.println("=== Urban Drone Simulation System ===");
        System.out.println("Simulating drone operations with allocation algorithms and delivery rules\n");
        
        // Run simulation with different algorithms
        runSimulation("Nearest Neighbor Algorithm", new NearestNeighborAlgorithm());
        System.out.println();
        runSimulation("Priority Based Algorithm", new PriorityBasedAlgorithm());
    }
    
    private static void runSimulation(String algorithmName, com.dti.drone.algorithm.AllocationAlgorithm algorithm) {
        System.out.println("--- " + algorithmName + " ---");
        
        // Create simulation engine
        DroneSimulationEngine engine = new DroneSimulationEngine(algorithm);
        
        // Setup simulation data
        setupDrones(engine);
        setupDeliveries(engine);
        
        System.out.println("Initial state:");
        printStatus(engine);
        
        // Run simulation steps
        for (int step = 1; step <= 5; step++) {
            System.out.println("\n--- Simulation Step " + step + " ---");
            engine.simulateStep();
            printStatus(engine);
        }
        
        System.out.println("\nFinal Statistics: " + engine.getStatistics());
    }
    
    private static void setupDrones(DroneSimulationEngine engine) {
        // Create base location (example: São Paulo city center)
        Location baseLocation = new Location(-23.5505, -46.6333, "São Paulo Distribution Center");
        
        // Add different types of drones
        engine.addDrone(new Drone("DRONE-001", 5.0, 50.0, 60.0, baseLocation));
        engine.addDrone(new Drone("DRONE-002", 3.0, 30.0, 80.0, baseLocation));
        engine.addDrone(new Drone("DRONE-003", 10.0, 100.0, 40.0, baseLocation));
        engine.addDrone(new Drone("DRONE-004", 2.0, 25.0, 100.0, baseLocation));
    }
    
    private static void setupDeliveries(DroneSimulationEngine engine) {
        LocalDateTime now = LocalDateTime.now();
        
        // Create various delivery locations in São Paulo
        Location pickup1 = new Location(-23.5489, -46.6388, "Pickup Point A");
        Location delivery1 = new Location(-23.5558, -46.6396, "Delivery Point 1");
        
        Location pickup2 = new Location(-23.5505, -46.6444, "Pickup Point B");
        Location delivery2 = new Location(-23.5449, -46.6256, "Delivery Point 2");
        
        Location pickup3 = new Location(-23.5567, -46.6450, "Pickup Point C");
        Location delivery3 = new Location(-23.5398, -46.6301, "Delivery Point 3");
        
        Location pickup4 = new Location(-23.5445, -46.6234, "Pickup Point D");
        Location delivery4 = new Location(-23.5612, -46.6523, "Delivery Point 4");
        
        // Add deliveries with different priorities and characteristics
        engine.addDelivery(new Delivery("DEL-001", pickup1, delivery1, 2.5, 
                Delivery.Priority.HIGH, now.plusHours(2)));
        
        engine.addDelivery(new Delivery("DEL-002", pickup2, delivery2, 1.0, 
                Delivery.Priority.URGENT, now.plusMinutes(30)));
        
        engine.addDelivery(new Delivery("DEL-003", pickup3, delivery3, 4.2, 
                Delivery.Priority.NORMAL, now.plusHours(6)));
        
        engine.addDelivery(new Delivery("DEL-004", pickup4, delivery4, 0.5, 
                Delivery.Priority.LOW, now.plusHours(12)));
        
        engine.addDelivery(new Delivery("DEL-005", pickup1, delivery3, 8.0, 
                Delivery.Priority.HIGH, now.plusHours(4)));
        
        engine.addDelivery(new Delivery("DEL-006", pickup2, delivery4, 1.8, 
                Delivery.Priority.NORMAL, now.plusHours(8)));
    }
    
    private static void printStatus(DroneSimulationEngine engine) {
        System.out.println("Drones status:");
        for (Drone drone : engine.getDrones()) {
            System.out.println("  " + drone);
        }
        
        System.out.println("Pending deliveries: " + engine.getPendingDeliveries().size());
        System.out.println("Completed deliveries: " + engine.getCompletedDeliveries().size());
        System.out.println("Statistics: " + engine.getStatistics());
    }
}