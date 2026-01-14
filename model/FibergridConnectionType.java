package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Connection type for outbound fault creation (PFR to FiberGrid).
 * As defined in the API specification:
 * - XT: Low voltage
 * - MT: Medium voltage
 */
public enum FibergridConnectionType {
    XT("XT"),
    MT("MT");

    private final String value;

    FibergridConnectionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static FibergridConnectionType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (FibergridConnectionType type : FibergridConnectionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Check if a string value is a valid connection type.
     */
    public static boolean isValid(String value) {
        return fromValue(value) != null;
    }

    /**
     * Get comma-separated list of valid values for error messages.
     */
    public static String getValidValues() {
        return "XT, MT";
    }
}
