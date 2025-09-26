package com.dti.drone.model;

/**
 * Represents a geographical location with coordinates
 */
public class Location {
    private final double latitude;
    private final double longitude;
    private final String address;

    public Location(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Calculate distance to another location using Haversine formula
     * @param other the other location
     * @return distance in kilometers
     */
    public double distanceTo(Location other) {
        final double EARTH_RADIUS_KM = 6371.0;
        
        double lat1Rad = Math.toRadians(this.latitude);
        double lon1Rad = Math.toRadians(this.longitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double lon2Rad = Math.toRadians(other.longitude);
        
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public String toString() {
        return String.format("Location{lat=%.6f, lon=%.6f, address='%s'}", 
            latitude, longitude, address);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return Double.compare(location.latitude, latitude) == 0 &&
               Double.compare(location.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(latitude, longitude);
    }
}