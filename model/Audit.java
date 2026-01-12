package gr.deddie.pfr.model;

import java.util.Date;

public class Audit {
    private Long id;
    private String property;
    private String state;
    private String new_value;
    private Date created;
    private String creator;

    public Audit(String property, String state, String new_value, String creator) {
        this.property = property;
        this.state = state;
        this.new_value = new_value;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNew_value() {
        return new_value;
    }

    public void setNew_value(String new_value) {
        this.new_value = new_value;
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
}
