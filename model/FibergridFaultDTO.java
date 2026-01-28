package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import java.util.Date;

import javax.json.bind.annotation.JsonbTypeSerializer;

/**
 * DTO for FiberGrid fault data exposed to frontend.
 * Includes human-readable descriptions for enum fields.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridFaultDTO {

    private Long id;

    @JsonProperty("fibergrid_id")
    private String fibergridId;

    @JsonProperty("pfr_failure_id")
    private Long pfrFailureId;

    private String address;

    private Double latitude;

    private Double longitude;

    @JsonProperty("flag_related")
    private Integer flagRelated;

    @JsonProperty("flag_related_description")
    private String flagRelatedDescription;

    private String status;

    private String notes;

    @JsonProperty("date_created")
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Long dateCreated;

    @JsonProperty("date_resolved")
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Long dateResolved;

    @JsonProperty("contact_name")
    private String contactName;

    @JsonProperty("contact_phone")
    private String contactPhone;

    @JsonProperty("contact_email")
    private String contactEmail;

    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private long created;

    @JsonProperty("last_updated")
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private long lastUpdated;
    public FibergridFaultDTO() {
    }

    /**
     * Create DTO from entity.
     */
    public static FibergridFaultDTO fromEntity(FibergridFault fault) {
        if (fault == null) {
            return null;
        }

        FibergridFaultDTO dto = new FibergridFaultDTO();
        dto.setId(fault.getId());
        dto.setFibergridId(fault.getFibergridId());
        dto.setPfrFailureId(fault.getPfrId());
        dto.setAddress(fault.getAddress());
        dto.setLatitude(fault.getLatitude());
        dto.setLongitude(fault.getLongitude());

        // Set flag_related and its description
        if (fault.getFlagRelated() != null) {
            dto.setFlagRelated(fault.getFlagRelated().getValue());
            dto.setFlagRelatedDescription(fault.getFlagRelated().getDescription());
        }

        // Set status
        if (fault.getStatus() != null) {
            dto.setStatus(fault.getStatus().getValue());
        }

        dto.setNotes(fault.getNotes());

        // Convert dates to Unix timestamps (milliseconds)
        if (fault.getDateCreated() != null) {
            dto.setDateCreated(fault.getDateCreated().getTime());
        }
        if (fault.getDateResolved() != null) {
            dto.setDateResolved(fault.getDateResolved().getTime());
        }

        // Contact information
        dto.setContactName(fault.getContactName());
        dto.setContactPhone(fault.getContactPhone());
        dto.setContactEmail(fault.getContactEmail());

        // Audit timestamps
        if (fault.getCreated() != null) {
            dto.setCreated(fault.getCreated().getTime());
        }
        if (fault.getLastUpdated() != null) {
            dto.setLastUpdated(fault.getLastUpdated().getTime());
        }

        return dto;
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

    public Integer getFlagRelated() {
        return flagRelated;
    }

    public void setFlagRelated(Integer flagRelated) {
        this.flagRelated = flagRelated;
    }

    public String getFlagRelatedDescription() {
        return flagRelatedDescription;
    }

    public void setFlagRelatedDescription(String flagRelatedDescription) {
        this.flagRelatedDescription = flagRelatedDescription;
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

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(long dateResolved) {
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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}