package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;

/**
 * Created by M.Masikos on 11/7/2017.
 */
public class FailureAssignment {
    private Long id;
    private Long failure_id;
    private Grafeio assignee;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date created;
    private String assignor;

    public FailureAssignment() {
    }

    public FailureAssignment(Grafeio assignee, String assignor) {
        this.assignee = assignee;
        this.assignor = assignor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFailure_id() {
        return failure_id;
    }

    public void setFailure_id(Long failure_id) {
        this.failure_id = failure_id;
    }

    public Grafeio getAssignee() {
        return assignee;
    }

    public void setAssignee(Grafeio assignee) {
        this.assignee = assignee;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAssignor() {
        return assignor;
    }

    public void setAssignor(String assignor) {
        this.assignor = assignor;
    }
}
