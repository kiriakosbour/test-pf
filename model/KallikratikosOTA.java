package gr.deddie.pfr.model;

import java.util.List;
import java.util.Objects;

public class KallikratikosOTA {
    private String klot;
    private String peri;
    private List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList;

    public KallikratikosOTA() {
    }

    public KallikratikosOTA(String klot) {
        this.klot = klot;
    }

    public KallikratikosOTA(KallikratikosOTA kallikratikosOTA) {
        this.klot = kallikratikosOTA.getKlot();
        this.peri = kallikratikosOTA.getPeri();
        this.kallikratikiDhmotikiEnothtaList = kallikratikosOTA.getKallikratikiDhmotikiEnothtaList();
    }

    public String getKlot() {
        return klot;
    }

    public void setKlot(String klot) {
        this.klot = klot;
    }

    public String getPeri() {
        return peri;
    }

    public void setPeri(String peri) {
        this.peri = peri;
    }

    public List<KallikratikiDhmotikiEnothta> getKallikratikiDhmotikiEnothtaList() {
        return kallikratikiDhmotikiEnothtaList;
    }

    public void setKallikratikiDhmotikiEnothtaList(List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList) {
        this.kallikratikiDhmotikiEnothtaList = kallikratikiDhmotikiEnothtaList;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof KallikratikosOTA)) {
            return false;
        } else {
            return ((KallikratikosOTA) o).getKlot().equals(this.getKlot());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(klot);
    }

    @Override
    public String toString() {
        return "KallikratikosOTA{" +
                "klot:\"" + klot + '\"' +
                ",peri:\"" + peri + '\"' +
                '}';
    }
}
