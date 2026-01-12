package gr.deddie.pfr.model;

import java.util.Date;

public class ExyphretoumeniDhmEnothta {
    private Long id;
    private YpostathmosYM ypostathmosYM;
    private String metasxhmatisths_ym_id;
    private String pylh_id;
    private KallikratikiDhmotikiEnothta kallikratikiDhmotikiEnothta;
    private String perigraf_Ephreazomen_Perioxhs;
    private Grafeio grafeio;
    private Date last_upd_timestamp;

    public ExyphretoumeniDhmEnothta() {
    }

    public ExyphretoumeniDhmEnothta(ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta) {
        this.id = exyphretoumeniDhmEnothta.id;
        this.ypostathmosYM = exyphretoumeniDhmEnothta.ypostathmosYM;
        this.metasxhmatisths_ym_id = exyphretoumeniDhmEnothta.metasxhmatisths_ym_id;
        this.pylh_id = exyphretoumeniDhmEnothta.pylh_id;
        this.kallikratikiDhmotikiEnothta = exyphretoumeniDhmEnothta.kallikratikiDhmotikiEnothta;
        this.perigraf_Ephreazomen_Perioxhs = exyphretoumeniDhmEnothta.perigraf_Ephreazomen_Perioxhs;
        this.grafeio = exyphretoumeniDhmEnothta.grafeio;
        this.last_upd_timestamp = exyphretoumeniDhmEnothta.last_upd_timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YpostathmosYM getYpostathmosYM() {
        return ypostathmosYM;
    }

    public void setYpostathmosYM(YpostathmosYM ypostathmosYM) {
        this.ypostathmosYM = ypostathmosYM;
    }

    public String getMetasxhmatisths_ym_id() {
        return metasxhmatisths_ym_id;
    }

    public void setMetasxhmatisths_ym_id(String metasxhmatisths_ym_id) {
        this.metasxhmatisths_ym_id = metasxhmatisths_ym_id;
    }

    public String getPylh_id() {
        return pylh_id;
    }

    public void setPylh_id(String pylh_id) {
        this.pylh_id = pylh_id;
    }

    public KallikratikiDhmotikiEnothta getKallikratikiDhmotikiEnothta() {
        return kallikratikiDhmotikiEnothta;
    }

    public void setKallikratikiDhmotikiEnothta(KallikratikiDhmotikiEnothta kallikratikiDhmotikiEnothta) {
        this.kallikratikiDhmotikiEnothta = kallikratikiDhmotikiEnothta;
    }

    public String getPerigraf_Ephreazomen_Perioxhs() {
        return perigraf_Ephreazomen_Perioxhs;
    }

    public void setPerigraf_Ephreazomen_Perioxhs(String perigraf_Ephreazomen_Perioxhs) {
        this.perigraf_Ephreazomen_Perioxhs = perigraf_Ephreazomen_Perioxhs;
    }

    public Grafeio getGrafeio() {
        return grafeio;
    }

    public void setGrafeio(Grafeio grafeio) {
        this.grafeio = grafeio;
    }

    public Date getLast_upd_timestamp() {
        return last_upd_timestamp;
    }

    public void setLast_upd_timestamp(Date last_upd_timestamp) {
        this.last_upd_timestamp = last_upd_timestamp;
    }
}
