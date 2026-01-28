package gr.deddie.pfr.model;

import java.util.Date;

/**
 * Entity class representing a photo attached to a FiberGrid fault.
 * Maps to the PFR_FIBERGRID_FAULT_PHOTOS table.
 */
public class FibergridFaultPhoto {

    private Long id;
    private Long faultId;
    private byte[] photoData;
    private Long photoSize;
    private String contentType;
    private String fileName;
    private Date created;

    public FibergridFaultPhoto() {
    }

    public FibergridFaultPhoto(Long faultId, byte[] photoData) {
        this.faultId = faultId;
        this.photoData = photoData;
        if (photoData != null) {
            this.photoSize = (long) photoData.length;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFaultId() {
        return faultId;
    }

    public void setFaultId(Long faultId) {
        this.faultId = faultId;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
        if (photoData != null) {
            this.photoSize = (long) photoData.length;
        }
    }

    public Long getPhotoSize() {
        return photoSize;
    }

    public void setPhotoSize(Long photoSize) {
        this.photoSize = photoSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
