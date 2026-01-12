package gr.deddie.pfr.model;

import java.util.Date;

public class ExyphretoumeniPerioxiDTO {
    private Long id;
    private String exyphretoymenes_perioxes;
    private Grafeio grafeio;
    private PerifereiakiEnotita perifereiakiEnotita;
    private ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus status;
    private Date last_upd_timestamp;

    public ExyphretoumeniPerioxiDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExyphretoymenes_perioxes() {
        return exyphretoymenes_perioxes;
    }

    public void setExyphretoymenes_perioxes(String exyphretoymenes_perioxes) {
        this.exyphretoymenes_perioxes = exyphretoymenes_perioxes;
    }

    public Grafeio getGrafeio() {
        return grafeio;
    }

    public void setGrafeio(Grafeio grafeio) {
        this.grafeio = grafeio;
    }

    public PerifereiakiEnotita getPerifereiakiEnotita() {
        return perifereiakiEnotita;
    }

    public void setPerifereiakiEnotita(PerifereiakiEnotita perifereiakiEnotita) {
        this.perifereiakiEnotita = perifereiakiEnotita;
    }

    public ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus getStatus() {
        return status;
    }

    public void setStatus(ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus status) {
        this.status = status;
    }

    public Date getLast_upd_timestamp() {
        return last_upd_timestamp;
    }

    public void setLast_upd_timestamp(Date last_upd_timestamp) {
        this.last_upd_timestamp = last_upd_timestamp;
    }
}
