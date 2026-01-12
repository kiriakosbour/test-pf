package gr.deddie.pfr.model;

import java.util.Objects;

public class LektikoGenikonDiakopon {
    private Long id;
    private String text;
    private boolean to_be_announced;

    public LektikoGenikonDiakopon() {
    }

    public LektikoGenikonDiakopon(LektikoGenikonDiakopon lektikoGenikonDiakopon) {
        this.id = lektikoGenikonDiakopon.id;
        this.text = lektikoGenikonDiakopon.text;
        this.to_be_announced = lektikoGenikonDiakopon.to_be_announced;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTo_be_announced() {
        return to_be_announced;
    }

    public void setTo_be_announced(boolean to_be_announced) {
        this.to_be_announced = to_be_announced;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof LektikoGenikonDiakopon)) {
            return false;
        } else {
            return ((LektikoGenikonDiakopon) o).getId().equals(this.getId());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
