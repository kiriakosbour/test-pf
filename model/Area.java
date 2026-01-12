package gr.deddie.pfr.model;

/**
 * Created by m.masikos on 23/3/2017.
 */
public class Area {
    private Long id;
    private String description;

    public Area(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
