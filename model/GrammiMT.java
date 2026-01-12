package gr.deddie.pfr.model;

import java.util.List;

public class GrammiMT {
    private String name;
    private String ypostathmos_ym_id;
    private String metasxhmatisths_ym_id;
    private String pylh_id;
    private List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList;
    private List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYpostathmos_ym_id() {
        return ypostathmos_ym_id;
    }

    public void setYpostathmos_ym_id(String ypostathmos_ym_id) {
        this.ypostathmos_ym_id = ypostathmos_ym_id;
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

    public List<ExyphretoumeniPerioxi> getExyphretoumeniPerioxiList() {
        return exyphretoumeniPerioxiList;
    }

    public void setExyphretoumeniPerioxiList(List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList) {
        this.exyphretoumeniPerioxiList = exyphretoumeniPerioxiList;
    }

    public List<ExyphretoumeniDhmEnothta> getExyphretoumeniDhmEnothtaList() {
        return exyphretoumeniDhmEnothtaList;
    }

    public void setExyphretoumeniDhmEnothtaList(List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList) {
        this.exyphretoumeniDhmEnothtaList = exyphretoumeniDhmEnothtaList;
    }
}
