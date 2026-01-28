package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Request DTO for POST /pfr/faults/update
 * FiberGrid updates notes and optionally status for an existing fault in HEDNO PFR.
 * 
 * Required fields: fibergrid_id
 * Optional fields: status, notes, date_resolved, contact_information, photos
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridFaultUpdateRequest {

    @JsonProperty("fibergrid_id")
    private String fibergridId;

    private String status;

    private String notes;

    @JsonProperty("date_resolved")
    private Date dateResolved;

    @JsonProperty("estimated_arrival_time_deddie")
    private String estimatedArrivalTimeDeddie;

    @JsonProperty("estimated_arrival_time_fibergrid")
    private String estimatedArrivalTimeFibergrid;

    @JsonProperty("root_cause")
    private String rootCause;

    @JsonProperty("contact_information")
    private FibergridContactInformation contactInformation;

    private List<String> photos;

    public FibergridFaultUpdateRequest() {
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

    public String getEstimatedArrivalTimeDeddie() {
        return estimatedArrivalTimeDeddie;
    }

    public void setEstimatedArrivalTimeDeddie(String estimatedArrivalTimeDeddie) {
        this.estimatedArrivalTimeDeddie = estimatedArrivalTimeDeddie;
    }

    public String getEstimatedArrivalTimeFibergrid() {
        return estimatedArrivalTimeFibergrid;
    }

    public void setEstimatedArrivalTimeFibergrid(String estimatedArrivalTimeFibergrid) {
        this.estimatedArrivalTimeFibergrid = estimatedArrivalTimeFibergrid;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
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

    /**
     * Check if this update has any fields to update.
     */
    public boolean hasUpdates() {
        return status != null || notes != null || dateResolved != null
            || contactInformation != null || (photos != null && !photos.isEmpty())
            || estimatedArrivalTimeDeddie != null || estimatedArrivalTimeFibergrid != null
            || rootCause != null;
    }

    @Override
    public String toString() {
        return "FibergridFaultUpdateRequest{" +
                "fibergridId='" + fibergridId + '\'' +
                ", status='" + status + '\'' +
                ", notes='" + (notes != null ? notes.substring(0, Math.min(50, notes.length())) + "..." : null) + '\'' +
                '}';
    }
}
