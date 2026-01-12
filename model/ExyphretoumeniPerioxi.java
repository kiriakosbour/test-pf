package gr.deddie.pfr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gr.deddie.pfr.utilities.ExyphretoymeniPerioxiStatusSerializer;

import java.util.Date;
import java.util.Objects;

public class ExyphretoumeniPerioxi {

    private Long id;
    private YpostathmosYM ypostathmosYM;
    private String metasxhmatisths_ym_id;
    private String pylh_id;
    private String exyphretoymenes_perioxes;
    private Grafeio grafeio;
    private PerifereiakiEnotita perifereiakiEnotita;
    private String tmhma_grammhs;
    private String krisima_fortia;
    private String simantika_fortia;
    @JsonSerialize(using = ExyphretoymeniPerioxiStatusSerializer.class)
    public enum ExyphretoymeniPerioxiStatus {
        INSERT ("Υπό επεξεργασία"), READY_TO_RECORD ("Αποστολή προς ηχογράφηση"), RECORDED("Ηχογραφημένο"), DELETED("Διεγραμμένο");
        private String value;

        ExyphretoymeniPerioxiStatus(String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }
    }
    private ExyphretoymeniPerioxiStatus status;
    private Date last_upd_timestamp;

    public ExyphretoumeniPerioxi() {
    }

    public ExyphretoumeniPerioxi(ExyphretoumeniPerioxi exyphretoumeniPerioxi) {
        this.id = exyphretoumeniPerioxi.id;
        this.ypostathmosYM = exyphretoumeniPerioxi.ypostathmosYM;
        this.metasxhmatisths_ym_id = exyphretoumeniPerioxi.metasxhmatisths_ym_id;
        this.pylh_id = exyphretoumeniPerioxi.pylh_id;
        this.exyphretoymenes_perioxes = exyphretoumeniPerioxi.exyphretoymenes_perioxes;
        this.grafeio = exyphretoumeniPerioxi.grafeio;
        this.perifereiakiEnotita = exyphretoumeniPerioxi.perifereiakiEnotita;
        this.tmhma_grammhs = exyphretoumeniPerioxi.tmhma_grammhs;
        this.status = exyphretoumeniPerioxi.status;
        this.last_upd_timestamp = exyphretoumeniPerioxi.last_upd_timestamp;
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

    public String getTmhma_grammhs() {
        return tmhma_grammhs;
    }

    public void setTmhma_grammhs(String tmhma_grammhs) {
        this.tmhma_grammhs = tmhma_grammhs;
    }

    public String getKrisima_fortia() {
        return krisima_fortia;
    }

    public void setKrisima_fortia(String krisima_fortia) {
        this.krisima_fortia = krisima_fortia;
    }

    public String getSimantika_fortia() {
        return simantika_fortia;
    }

    public void setSimantika_fortia(String simantika_fortia) {
        this.simantika_fortia = simantika_fortia;
    }

    public ExyphretoymeniPerioxiStatus getStatus() {
        return status;
    }

    public void setStatus(ExyphretoymeniPerioxiStatus status) {
        this.status = status;
    }

    public Date getLast_upd_timestamp() {
        return last_upd_timestamp;
    }

    public void setLast_upd_timestamp(Date last_upd_timestamp) {
        this.last_upd_timestamp = last_upd_timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof ExyphretoumeniPerioxi)) {
            return false;
        } else {
            return ((ExyphretoumeniPerioxi) o).getId().equals(this.getId());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
