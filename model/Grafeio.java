package gr.deddie.pfr.model;

import java.util.Objects;

/**
 * Created by M.Masikos on 5/1/2017.
 */
public class Grafeio {
    private String graf;
    private String peri;
    private String perx;
    private String perioxi;
    private String perf;
    private String perd_peri;

    public String getGraf() {
        return graf;
    }

    public void setGraf(String graf) {
        this.graf = graf;
    }

    public String getPeri() {
        return peri;
    }

    public void setPeri(String peri) {
        this.peri = peri;
    }

    public String getPerx() {
        return perx;
    }

    public void setPerx(String perx) {
        this.perx = perx;
    }

    public String getPerf() {
        return perf;
    }

    public void setPerf(String perf) {
        this.perf = perf;
    }

    public String getPerioxi() {
        return perioxi;
    }

    public void setPerioxi(String perioxi) {
        this.perioxi = perioxi;
    }

    public String getPerd_peri() {
        return perd_peri;
    }

    public void setPerd_peri(String perd_peri) {
        this.perd_peri = perd_peri;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Grafeio)) {
            return false;
        } else {
            return ((Grafeio) obj).getGraf().equals(this.getGraf());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(graf);
    }
}
