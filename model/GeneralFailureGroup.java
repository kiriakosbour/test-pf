package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;
import java.util.Objects;

public class GeneralFailureGroup {
    private Long id;
    private String label;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date created;
    private String creator;

    public GeneralFailureGroup() {
    }

    public GeneralFailureGroup(Long id, String label, Date created, String creator) {
        this.id = id;
        this.label = label;
        this.created = created;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof GeneralFailureGroup)) {
            return false;
        } else {
            return ((GeneralFailureGroup) o).getId().equals(this.getId());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
