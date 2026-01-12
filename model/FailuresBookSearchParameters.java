package gr.deddie.pfr.model;

import java.util.List;

public class FailuresBookSearchParameters {
    private String monthyear;
    private List<Grafeio> grafeioList;

    public String getMonthyear() {
        return monthyear;
    }

    public void setMonthyear(String monthyear) {
        this.monthyear = monthyear;
    }

    public List<Grafeio> getGrafeioList() {
        return grafeioList;
    }

    public void setGrafeioList(List<Grafeio> grafeioList) {
        this.grafeioList = grafeioList;
    }
}
