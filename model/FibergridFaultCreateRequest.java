package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Request DTO for POST /pfr/faults/create
 * FiberGrid sends fault data to HEDNO PFR.
 * 
 * Required fields: fibergrid_id, address, coordinates, flag_related, status, date_created
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridFaultCreateRequest {

    @JsonProperty("fibergrid_id")
    private String fibergridId;

    private String address;

    private FibergridCoordinates coordinates;

    @JsonProperty("flag_related")
    private Integer flagRelated;

    private String status;

    private String notes;

    @JsonProperty("date_created")
    private Date dateCreated;

    @JsonProperty("contact_information")
    private FibergridContactInformation contactInformation;

    private List<String> photos;

    public FibergridFaultCreateRequest() {
    }

    // Getters and Setters

    public String getFibergridId() {
        return fibergridId;
    }

    public void setFibergridId(String fibergridId) {
        this.fibergridId = fibergridId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public FibergridCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(FibergridCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getFlagRelated() {
        return flagRelated;
    }

    public void setFlagRelated(Integer flagRelated) {
        this.flagRelated = flagRelated;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
     * Convert request to entity for persistence.
     */
    public FibergridFault toEntity() {
        FibergridFault fault = new FibergridFault();
        fault.setFibergridId(this.fibergridId);
        fault.setAddress(this.address);
        fault.setCoordinates(this.coordinates);
        fault.setFlagRelated(FibergridFlagRelated.fromValue(this.flagRelated));
        fault.setStatus(FibergridFaultStatus.fromValue(this.status));
        fault.setNotes(this.notes);
        fault.setDateCreated(this.dateCreated);
        fault.setContactInformation(this.contactInformation);
        return fault;
    }

    @Override
    public String toString() {
        return "FibergridFaultCreateRequest{" +
                "fibergridId='" + fibergridId + '\'' +
                ", address='" + address + '\'' +
                ", flagRelated=" + flagRelated +
                ", status='" + status + '\'' +
                '}';
    }
}
