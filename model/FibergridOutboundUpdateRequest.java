package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Request DTO for outbound POST /fibergrid/faults/update calls.
 * PFR sends fault updates to Fibergrid.
 *
 * Based on YAML spec:
 * - Required: fibergrid_id
 * - Optional: status, hedno_eta, fibergrid_eta, root_cause, notes, date_resolved, contact_information, photos
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridOutboundUpdateRequest {

    @JsonProperty("fibergrid_id")
    private String fibergridId;

    private String status;

    @JsonProperty("hedno_eta")
    private String hednoEta;

    @JsonProperty("fibergrid_eta")
    private String fibergridEta;

    @JsonProperty("root_cause")
    private String rootCause;

    private String notes;

    @JsonProperty("date_resolved")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Date dateResolved;

    @JsonProperty("contact_information")
    private FibergridContactInformation contactInformation;

    private List<String> photos;

    public FibergridOutboundUpdateRequest() {
    }

    /**
     * Builder pattern for fluent construction.
     */
    public static Builder builder(String fibergridId) {
        return new Builder(fibergridId);
    }

    public static class Builder {
        private final FibergridOutboundUpdateRequest request;

        public Builder(String fibergridId) {
            this.request = new FibergridOutboundUpdateRequest();
            this.request.fibergridId = fibergridId;
        }

        public Builder status(String status) {
            this.request.status = status;
            return this;
        }

        public Builder hednoEta(String hednoEta) {
            this.request.hednoEta = hednoEta;
            return this;
        }

        public Builder fibergridEta(String fibergridEta) {
            this.request.fibergridEta = fibergridEta;
            return this;
        }

        public Builder rootCause(String rootCause) {
            this.request.rootCause = rootCause;
            return this;
        }

        public Builder notes(String notes) {
            this.request.notes = notes;
            return this;
        }

        public Builder dateResolved(Date dateResolved) {
            this.request.dateResolved = dateResolved;
            return this;
        }

        public Builder contactInformation(FibergridContactInformation contactInformation) {
            this.request.contactInformation = contactInformation;
            return this;
        }

        public Builder photos(List<String> photos) {
            this.request.photos = photos;
            return this;
        }

        public FibergridOutboundUpdateRequest build() {
            return this.request;
        }
    }

    /**
     * Create request from a FibergridFault entity.
     */
    public static FibergridOutboundUpdateRequest fromFault(FibergridFault fault) {
        if (fault == null || fault.getFibergridId() == null) {
            return null;
        }

        Builder builder = builder(fault.getFibergridId());

        if (fault.getStatus() != null) {
            builder.status(fault.getStatus().getValue());
        }
        if (fault.getEstimatedArrivalTimeDeddie() != null) {
            builder.hednoEta(fault.getEstimatedArrivalTimeDeddie());
        }
        if (fault.getEstimatedArrivalTimeFibergrid() != null) {
            builder.fibergridEta(fault.getEstimatedArrivalTimeFibergrid());
        }
        if (fault.getRootCause() != null) {
            builder.rootCause(fault.getRootCause());
        }
        if (fault.getNotes() != null) {
            builder.notes(fault.getNotes());
        }
        if (fault.getDateResolved() != null) {
            builder.dateResolved(fault.getDateResolved());
        }
        if (fault.getContactInformation() != null) {
            builder.contactInformation(fault.getContactInformation());
        }

        return builder.build();
    }

    // Getters and Setters

    public String getFibergridId() {
        return fibergridId;
    }

    public void setFibergridId(String fibergridId) {
        this.fibergridId = fibergridId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHednoEta() {
        return hednoEta;
    }

    public void setHednoEta(String hednoEta) {
        this.hednoEta = hednoEta;
    }

    public String getFibergridEta() {
        return fibergridEta;
    }

    public void setFibergridEta(String fibergridEta) {
        this.fibergridEta = fibergridEta;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Date dateResolved) {
        this.dateResolved = dateResolved;
    }

    public FibergridContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(FibergridContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "FibergridOutboundUpdateRequest{" +
                "fibergridId='" + fibergridId + '\'' +
                ", status='" + status + '\'' +
                ", rootCause='" + rootCause + '\'' +
                ", notes='" + (notes != null ? notes.substring(0, Math.min(50, notes.length())) + "..." : null) + '\'' +
                '}';
    }
}
