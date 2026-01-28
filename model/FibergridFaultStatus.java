package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FibergridFaultStatus {
    OPEN("OPEN"),
    IN_PROGRESS("IN_PROGRESS"),
    RESOLVED("RESOLVED"),
    CANCELLED("CANCELLED");

    private final String value;

    FibergridFaultStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static FibergridFaultStatus fromValue(String value) {
        if (value == null) return null;
        for (FibergridFaultStatus status : FibergridFaultStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }

    /**
     * Check if a string value is a valid status.
     */
    public static boolean isValid(String value) {
        return fromValue(value) != null;
    }

    /**
     * Get comma-separated list of valid values for error messages.
     */
    public static String getValidValues() {
        StringBuilder sb = new StringBuilder();
        for (FibergridFaultStatus status : values()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(status.value);
        }
        return sb.toString();
    }
}
