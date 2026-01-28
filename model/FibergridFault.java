package gr.deddie.pfr.model;

import java.util.Date;

import javax.json.bind.annotation.JsonbTypeSerializer;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

public class FibergridFault {
    private Long id;
    private String fibergridId;
    private Long pfrId; // Renamed from pfrFailureId to match pfr_id in YAML
    private String address;
    private String perx; // New mandatory field: Regional Unit



    private Double latitude;
    private Double longitude;
    private FibergridFlagRelated flagRelated;
    private FibergridFaultStatus status;
    private FibergridConnectionType connectionType; // New mandatory field

    private String notes;
    private Date dateCreated;
    private Date dateUpdated; // Added for sync
    private Date dateResolved;

 
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date dateVisited;
    private String estimatedArrivalTimeDeddie;
    private String estimatedArrivalTimeFibergrid;
    private String rootCause;
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

    public Long getPfrId() {
        return pfrId;
    }

    public void setPfrId(Long pfrId) {
        this.pfrId = pfrId;
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

    public FibergridConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(FibergridConnectionType connectionType) {
        this.connectionType = connectionType;
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

    public Date getDateVisited() {
        return dateVisited;
    }

    public void setDateVisited(Date dateVisited) {
        this.dateVisited = dateVisited;
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
    public String getPerx() {
        return perx;
    }

    public void setPerx(String perx) {
        this.perx = perx;
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
       public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
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
                ", pfrId=" + pfrId +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", flagRelated=" + flagRelated +
                '}';
    }
}
