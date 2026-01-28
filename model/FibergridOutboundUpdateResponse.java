package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO from Fibergrid's POST /fibergrid/faults/update endpoint.
 *
 * Success response (200):
 * {
 *   "success": true,
 *   "message": "Fault successfully updated in FiberGrid"
 * }
 *
 * Error response (400/401/404/500):
 * {
 *   "success": false,
 *   "error": "Error message",
 *   "error_code": "ERROR_CODE",
 *   "details": ["detail1", "detail2"]
 * }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridOutboundUpdateResponse {

    private boolean success;

    private String message;

    private String error;

    @JsonProperty("error_code")
    private String errorCode;

    private List<String> details;

    public FibergridOutboundUpdateResponse() {
    }

    public FibergridOutboundUpdateResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Factory method for successful response.
     */
    public static FibergridOutboundUpdateResponse success(String message) {
        FibergridOutboundUpdateResponse response = new FibergridOutboundUpdateResponse();
        response.success = true;
        response.message = message;
        return response;
    }

    /**
     * Factory method for error response.
     */
    public static FibergridOutboundUpdateResponse error(String error, String errorCode, List<String> details) {
        FibergridOutboundUpdateResponse response = new FibergridOutboundUpdateResponse();
        response.success = false;
        response.error = error;
        response.errorCode = errorCode;
        response.details = details;
        return response;
    }

    /**
     * Check if this is an error response with a specific error code.
     */
    public boolean hasErrorCode(String code) {
        return errorCode != null && errorCode.equals(code);
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    @Override
    public String toString() {
        return "FibergridOutboundUpdateResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
