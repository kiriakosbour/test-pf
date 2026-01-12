package gr.deddie.pfr.model;

// diairesi tis aftodioikisis prin ton kallikrati kai ton kapodistria
public class OldNomarxia {
    private Long oldno;
    private String peri;

    public Long getOldno() {
        return oldno;
    }

    public void setOldno(Long oldno) {
        this.oldno = oldno;
    }

    public String getPeri() {
        return peri;
    }

    public void setPeri(String peri) {
        this.peri = peri;
    }

    @Override
    public String toString() {
        return "{" +
                "oldno:\"" + oldno + '\"' +
                ",peri:\"" + peri + '\"' +
                '}';
    }
}
