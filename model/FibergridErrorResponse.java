package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Response DTO for error responses (400, 401, 404, 500).
 * Used for all API error responses following the OpenAPI specification.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridErrorResponse {

    private boolean success;
    private String error;

    @JsonProperty("error_code")
    private String errorCode;

    private List<String> details;

    public FibergridErrorResponse() {
        this.success = false;
    }

    public FibergridErrorResponse(String error, String errorCode) {
        this.success = false;
        this.error = error;
        this.errorCode = errorCode;
    }

    public FibergridErrorResponse(String error, String errorCode, List<String> details) {
        this.success = false;
        this.error = error;
        this.errorCode = errorCode;
        this.details = details;
    }

    // Factory methods for common error types

    /**
     * Validation error (400) - missing required fields or invalid values.
     */
    public static FibergridErrorResponse validationError(List<String> details) {
        return new FibergridErrorResponse("Validation failed", "VALIDATION_ERROR", details);
    }

    /**
     * Validation error (400) with single detail.
     */
    public static FibergridErrorResponse validationError(String detail) {
        return new FibergridErrorResponse("Validation failed", "VALIDATION_ERROR", 
            Collections.singletonList(detail));
    }

    /**
     * Invalid enum value error (400).
     */
    public static FibergridErrorResponse invalidEnum(List<String> details) {
        return new FibergridErrorResponse("Invalid enum value", "INVALID_ENUM", details);
    }

    /**
     * Invalid status value error (400) - specific for status field.
     */
    public static FibergridErrorResponse invalidStatus(String detail) {
        return new FibergridErrorResponse("Invalid status value", "INVALID_STATUS", 
            Collections.singletonList(detail));
    }

    /**
     * Duplicate record error (400).
     */
    public static FibergridErrorResponse duplicate(String fibergridId) {
        return new FibergridErrorResponse("Duplicate record", "DUPLICATE",
            Collections.singletonList("Fault with fibergrid_id '" + fibergridId + "' already exists"));
    }

    /**
     * Unauthorized error (401) - invalid or missing token.
     */
    public static FibergridErrorResponse unauthorized() {
        return new FibergridErrorResponse("Unauthorized", "INVALID_TOKEN");
    }

    /**
     * Not found error (404).
     */
    public static FibergridErrorResponse notFound(String fibergridId) {
        return new FibergridErrorResponse("Fault not found", "NOT_FOUND",
            Collections.singletonList("Fault with ID " + fibergridId + " not found in PFR system"));
    }

    /**
     * Internal server error (500).
     */
    public static FibergridErrorResponse internalError() {
        return new FibergridErrorResponse("Internal server error occurred", "INTERNAL_ERROR");
    }

    /**
     * Internal server error (500) with details.
     */
    public static FibergridErrorResponse internalError(String detail) {
        return new FibergridErrorResponse("Internal server error occurred", "INTERNAL_ERROR",
            Collections.singletonList(detail));
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
