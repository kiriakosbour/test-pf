package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;

/**
 * Entity class representing a FiberGrid fault record in the database.
 * Maps to the PFR_FBG_FAULTS table.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridFault {

    private Long id;
    private String fibergridId;
    private Long pfrFailureId;  // FK to PFR_FAILURES (Phase 2)
    private String address;
    private Double latitude;
    private Double longitude;
    private FibergridFlagRelated flagRelated;
    private Integer statusId;   // FK to H_PFR_FBG_STATUS
    private FibergridFaultStatus status;
    private String notes;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date dateCreated;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date dateResolved;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date created;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date lastUpdated;
    private String createdBy;
    private String updatedBy;
    private Boolean deleted;

    public FibergridFault() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFibergridId() {
        return fibergridId;
    }

    public void setFibergridId(String fibergridId) {
        this.fibergridId = fibergridId;
    }

    public Long getPfrFailureId() {
        return pfrFailureId;
    }

    public void setPfrFailureId(Long pfrFailureId) {
        this.pfrFailureId = pfrFailureId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public FibergridFlagRelated getFlagRelated() {
        return flagRelated;
    }

    public void setFlagRelated(FibergridFlagRelated flagRelated) {
        this.flagRelated = flagRelated;
    }

    public FibergridFaultStatus getStatus() {
        return status;
    }

    public void setStatus(FibergridFaultStatus status) {
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

    public Date getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Date dateResolved) {
        this.dateResolved = dateResolved;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Create coordinates object from lat/long.
     */
    public FibergridCoordinates getCoordinates() {
        if (latitude != null && longitude != null) {
            return new FibergridCoordinates(latitude, longitude);
        }
        return null;
    }

    /**
     * Set coordinates from coordinates object.
     */
    public void setCoordinates(FibergridCoordinates coordinates) {
        if (coordinates != null) {
            this.latitude = coordinates.getLatitude();
            this.longitude = coordinates.getLongitude();
        }
    }

    /**
     * Create contact information object.
     */
    public FibergridContactInformation getContactInformation() {
        if (contactName != null || contactPhone != null || contactEmail != null) {
            return new FibergridContactInformation(contactName, contactPhone, contactEmail);
        }
        return null;
    }

    /**
     * Set contact information from object.
     */
    public void setContactInformation(FibergridContactInformation contactInfo) {
        if (contactInfo != null) {
            this.contactName = contactInfo.getName();
            this.contactPhone = contactInfo.getPhone();
            this.contactEmail = contactInfo.getEmail();
        }
    }

    @Override
    public String toString() {
        return "FibergridFault{" +
                "id=" + id +
                ", fibergridId='" + fibergridId + '\'' +
                ", pfrFailureId=" + pfrFailureId +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", flagRelated=" + flagRelated +
                '}';
    }
}
