package gr.deddie.pfr.model;

import java.util.Objects;

public class KallikratikiDhmotikiEnothta {
    private String klde;
    private String peri;

    public KallikratikiDhmotikiEnothta() {
    }

    public KallikratikiDhmotikiEnothta(KallikratikiDhmotikiEnothta kallikratikiDhmotikiEnothta) {
        this.klde = kallikratikiDhmotikiEnothta.getKlde();
        this.peri = kallikratikiDhmotikiEnothta.getPeri();
    }

    public String getKlde() {
        return klde;
    }

    public void setKlde(String klde) {
        this.klde = klde;
    }

    public String getPeri() {
        return peri;
    }

    public void setPeri(String peri) {
        this.peri = peri;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof KallikratikiDhmotikiEnothta)) {
            return false;
        } else {
            return ((KallikratikiDhmotikiEnothta) o).getKlde().equals(this.getKlde());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(klde);
    }
}
