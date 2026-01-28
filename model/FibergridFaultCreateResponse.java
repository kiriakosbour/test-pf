package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for successful fault creation (201 Created).
 * POST /pfr/faults/create response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridFaultCreateResponse {

    private boolean success;

    @JsonProperty("pfr_id")
    private String pfrId;

    private String message;

    public FibergridFaultCreateResponse() {
    }

    public FibergridFaultCreateResponse(boolean success, String pfrId, String message) {
        this.success = success;
        this.pfrId = pfrId;
        this.message = message;
    }

    /**
     * Factory method for successful creation.
     */
    public static FibergridFaultCreateResponse success(Long pfrId) {
        return new FibergridFaultCreateResponse(
            true, 
            pfrId != null ? pfrId.toString() : null,
            "Fault successfully created in PFR"
        );
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPfrId() {
        return pfrId;
    }

    public void setPfrId(String pfrId) {
        this.pfrId = pfrId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
