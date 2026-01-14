package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Response DTO for successful fault update (200 OK).
 * POST /pfr/faults/update response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridFaultUpdateResponse {

    private boolean success;
    private String message;

    public FibergridFaultUpdateResponse() {
    }

    public FibergridFaultUpdateResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Factory method for successful update.
     */
    public static FibergridFaultUpdateResponse success() {
        return new FibergridFaultUpdateResponse(true, "Fault successfully updated in HEDNO PFR");
    }

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
}
