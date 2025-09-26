package com.dti.drone.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a delivery drone with its capabilities and current state
 */
public class Drone {
    public enum Status {
        AVAILABLE, IN_TRANSIT, DELIVERING, MAINTENANCE, CHARGING
    }

    private final String id;
    private final double maxWeight;
    private final double maxRange;
    private final double speed; // km/h
    private Status status;
    private Location currentLocation;
    private double currentBatteryLevel; // percentage 0-100
    private final List<Delivery> assignedDeliveries;
    private LocalDateTime lastMaintenanceDate;

    public Drone(String id, double maxWeight, double maxRange, double speed, Location baseLocation) {
        this.id = id;
        this.maxWeight = maxWeight;
        this.maxRange = maxRange;
        this.speed = speed;
        this.status = Status.AVAILABLE;
        this.currentLocation = baseLocation;
        this.currentBatteryLevel = 100.0;
        this.assignedDeliveries = new ArrayList<>();
        this.lastMaintenanceDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public double getSpeed() {
        return speed;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public double getCurrentBatteryLevel() {
        return currentBatteryLevel;
    }

    public void setCurrentBatteryLevel(double batteryLevel) {
        this.currentBatteryLevel = Math.max(0, Math.min(100, batteryLevel));
    }

    public List<Delivery> getAssignedDeliveries() {
        return new ArrayList<>(assignedDeliveries);
    }

    public void assignDelivery(Delivery delivery) {
        assignedDeliveries.add(delivery);
    }

    public void removeDelivery(Delivery delivery) {
        assignedDeliveries.remove(delivery);
    }

    public void clearDeliveries() {
        assignedDeliveries.clear();
    }

    public LocalDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDateTime date) {
        this.lastMaintenanceDate = date;
    }

    /**
     * Check if drone can handle a delivery based on weight and range constraints
     */
    public boolean canHandleDelivery(Delivery delivery) {
        if (status != Status.AVAILABLE) {
            return false;
        }
        
        if (delivery.getWeight() > maxWeight) {
            return false;
        }
        
        double totalDistance = currentLocation.distanceTo(delivery.getPickupLocation()) +
                              delivery.getPickupLocation().distanceTo(delivery.getDeliveryLocation());
        
        return totalDistance <= maxRange && currentBatteryLevel > 20; // Keep 20% battery reserve
    }

    /**
     * Calculate estimated delivery time in minutes
     */
    public double calculateDeliveryTime(Delivery delivery) {
        double totalDistance = currentLocation.distanceTo(delivery.getPickupLocation()) +
                              delivery.getPickupLocation().distanceTo(delivery.getDeliveryLocation());
        return (totalDistance / speed) * 60; // Convert hours to minutes
    }

    @Override
    public String toString() {
        return String.format("Drone{id='%s', status=%s, battery=%.1f%%, location=%s}", 
            id, status, currentBatteryLevel, currentLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Drone drone = (Drone) obj;
        return id.equals(drone.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}