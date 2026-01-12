package gr.deddie.pfr.model;

import java.util.Date;

/**
 * Created by M.Masikos on 26/4/2017.
 */
public class FailureSearchParameters {
    private Date announcedFrom;
    private Date announcedTo;
    private Integer failure_type_id;
    private String status;
    private Boolean failure_extent;
    private Grafeio grafeio;

    public Date getAnnouncedFrom() {
        return announcedFrom;
    }

    public void setAnnouncedFrom(Date announcedFrom) {
        this.announcedFrom = announcedFrom;
    }

    public Date getAnnouncedTo() {
        return announcedTo;
    }

    public void setAnnouncedTo(Date announcedTo) {
        this.announcedTo = announcedTo;
    }

    public Integer getFailure_type_id() {
        return failure_type_id;
    }

    public void setFailure_type_id(Integer failure_type_id) {
        this.failure_type_id = failure_type_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getFailure_extent() {
        return failure_extent;
    }

    public void setFailure_extent(Boolean failure_extent) {
        this.failure_extent = failure_extent;
    }

    public Grafeio getGrafeio() {
        return grafeio;
    }

    public void setGrafeio(Grafeio grafeio) {
        this.grafeio = grafeio;
    }
}
