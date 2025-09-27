package com.dti.drone.model;

import java.time.LocalDateTime;

/**
 * Represents a delivery request with pickup and delivery locations
 */
public class Delivery {
    public enum Priority {
        LOW(1), NORMAL(2), HIGH(3), URGENT(4);
        
        private final int value;
        
        Priority(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    public enum Status {
        PENDING, ASSIGNED, PICKED_UP, IN_TRANSIT, DELIVERED, CANCELLED
    }

    private final String id;
    private final Location pickupLocation;
    private final Location deliveryLocation;
    private final double weight;
    private final Priority priority;
    private final LocalDateTime requestTime;
    private final LocalDateTime deadline;
    private Status status;
    private String assignedDroneId;
    private LocalDateTime assignmentTime;
    private LocalDateTime deliveryTime;

    public Delivery(String id, Location pickupLocation, Location deliveryLocation, 
                   double weight, Priority priority, LocalDateTime deadline) {
        this.id = id;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.weight = weight;
        this.priority = priority;
        this.requestTime = LocalDateTime.now();
        this.deadline = deadline;
        this.status = Status.PENDING;
    }

    public String getId() {
        return id;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDeliveryLocation() {
        return deliveryLocation;
    }

    public double getWeight() {
        return weight;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssignedDroneId() {
        return assignedDroneId;
    }

    public void setAssignedDroneId(String droneId) {
        this.assignedDroneId = droneId;
        if (droneId != null) {
            this.assignmentTime = LocalDateTime.now();
            this.status = Status.ASSIGNED;
        }
    }

    public LocalDateTime getAssignmentTime() {
        return assignmentTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
        this.status = Status.DELIVERED;
    }

    /**
     * Calculate total delivery distance
     */
    public double getTotalDistance() {
        return pickupLocation.distanceTo(deliveryLocation);
    }

    /**
     * Check if delivery is overdue
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(deadline) && status != Status.DELIVERED;
    }

    /**
     * Calculate urgency score based on priority and time remaining
     */
    public double getUrgencyScore() {
        LocalDateTime now = LocalDateTime.now();
        long hoursRemaining = java.time.Duration.between(now, deadline).toHours();
        
        // Higher score = more urgent
        double timeUrgency = Math.max(0, 24.0 / Math.max(1, hoursRemaining));
        double priorityUrgency = priority.getValue();
        
        return priorityUrgency * timeUrgency;
    }

    @Override
    public String toString() {
        return String.format("Delivery{id='%s', status=%s, priority=%s, weight=%.2fkg, distance=%.2fkm}", 
            id, status, priority, weight, getTotalDistance());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Delivery delivery = (Delivery) obj;
        return id.equals(delivery.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}