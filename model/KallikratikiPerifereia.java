package gr.deddie.pfr.model;

import java.util.List;

public class KallikratikiPerifereia {
    private String klpe;
    private String peri;
    private List<KallikratikiNomarxia> kallikratikiNomarxiaList;

    public String getKlpe() {
        return klpe;
    }

    public void setKlpe(String klpe) {
        this.klpe = klpe;
    }

    public String getPeri() {
        return peri;
    }

    public void setPeri(String peri) {
        this.peri = peri;
    }

    public List<KallikratikiNomarxia> getKallikratikiNomarxiaList() {
        return kallikratikiNomarxiaList;
    }

    public void setKallikratikiNomarxiaList(List<KallikratikiNomarxia> kallikratikiNomarxiaList) {
        this.kallikratikiNomarxiaList = kallikratikiNomarxiaList;
    }
}
