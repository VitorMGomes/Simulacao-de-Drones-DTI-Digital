package com.dti.drone.simulation;

/**
 * Tracks simulation statistics and metrics
 */
public class SimulationStatistics {
    
    private int totalRequests;
    private int assignedDeliveries;
    private int completedDeliveries;
    private int pendingDeliveries;
    private int activeDrones;
    private double averageDeliveryTime;
    private double successRate;
    
    public SimulationStatistics() {
        this.totalRequests = 0;
        this.assignedDeliveries = 0;
        this.completedDeliveries = 0;
        this.pendingDeliveries = 0;
        this.activeDrones = 0;
        this.averageDeliveryTime = 0.0;
        this.successRate = 0.0;
    }
    
    public int getTotalRequests() {
        return totalRequests;
    }
    
    public void incrementTotalRequests() {
        this.totalRequests++;
    }
    
    public int getAssignedDeliveries() {
        return assignedDeliveries;
    }
    
    public void incrementAssignedDeliveries() {
        this.assignedDeliveries++;
    }
    
    public int getCompletedDeliveries() {
        return completedDeliveries;
    }
    
    public void incrementCompletedDeliveries() {
        this.completedDeliveries++;
        updateSuccessRate();
    }
    
    public int getPendingDeliveries() {
        return pendingDeliveries;
    }
    
    public void setPendingDeliveries(int pendingDeliveries) {
        this.pendingDeliveries = pendingDeliveries;
    }
    
    public int getActiveDrones() {
        return activeDrones;
    }
    
    public void setActiveDrones(int activeDrones) {
        this.activeDrones = activeDrones;
    }
    
    public double getAverageDeliveryTime() {
        return averageDeliveryTime;
    }
    
    public void setAverageDeliveryTime(double averageDeliveryTime) {
        this.averageDeliveryTime = averageDeliveryTime;
    }
    
    public double getSuccessRate() {
        return successRate;
    }
    
    private void updateSuccessRate() {
        if (totalRequests > 0) {
            this.successRate = (double) completedDeliveries / totalRequests * 100;
        }
    }
    
    @Override
    public String toString() {
        return String.format(
            "Statistics{requests=%d, assigned=%d, completed=%d, pending=%d, " +
            "active_drones=%d, avg_time=%.2f min, success_rate=%.1f%%}",
            totalRequests, assignedDeliveries, completedDeliveries, pendingDeliveries,
            activeDrones, averageDeliveryTime, successRate
        );
    }
}