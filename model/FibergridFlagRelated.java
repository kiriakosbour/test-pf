package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Flag indicating which network is affected by the fault.
 * As defined in the FiberGrid API specification:
 * - 1: HEDNO network fault
 * - 2: Fiber network fault  
 * - 3: Both networks affected
 */
public enum FibergridFlagRelated {
    HEDNO(1, "HEDNO network fault"),
    FIBER(2, "Fiber network fault"),
    BOTH(3, "Both networks affected");

    private final int value;
    private final String description;

    FibergridFlagRelated(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static FibergridFlagRelated fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (FibergridFlagRelated flag : FibergridFlagRelated.values()) {
            if (flag.value == value) {
                return flag;
            }
        }
        return null;
    }

    /**
     * Check if an integer value is a valid flag.
     */
    public static boolean isValid(Integer value) {
        return fromValue(value) != null;
    }

    /**
     * Get valid values description for error messages.
     */
    public static String getValidValuesDescription() {
        return "1 (HEDNO), 2 (Fiber), or 3 (Both)";
    }
}
