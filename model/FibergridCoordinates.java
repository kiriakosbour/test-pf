package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Geographic coordinates for fault location.
 * Used in both inbound (coordinates) and outbound (location) API calls.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridCoordinates {

    private Double latitude;
    private Double longitude;

    public FibergridCoordinates() {
    }

    public FibergridCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Validate that coordinates are within valid ranges.
     * Latitude: -90 to 90
     * Longitude: -180 to 180
     */
    public boolean isValid() {
        if (latitude == null || longitude == null) {
            return false;
        }
        return latitude >= -90 && latitude <= 90 
            && longitude >= -180 && longitude <= 180;
    }

    @Override
    public String toString() {
        return "FibergridCoordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
