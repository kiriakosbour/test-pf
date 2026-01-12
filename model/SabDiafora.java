package gr.deddie.pfr.model;

import java.util.Date;

public class SabDiafora {
    private Long pfr_id;
    private String vltype;
    private String supply;
//    private String supply_type;
//    private Boolean A;
//    private Boolean B;
//    private Boolean C;
//    private Boolean O;
    private String vlkind;
    private Date dtcreated;
//    private Date dtupdated;
    private String vlval;
    private String notes;
    //private String loc;
    private String last_upd_usr;
    private String syne;
    private String syne2;
    private String depm;
//    private String meter;
//    private String ende;
//    private String endn;
//    private String newmeter;
//    private String newende;
//    private String newendn;
//    private String sfrakb;
//    private String sfrakrod;
//    private String sfrad;
    private String stat;
//    private Boolean ischarge;
//    private Boolean isrk;
//    private String rkshme;
    private Date enddt;
    private String initstat;
//    private Boolean depmproc;
//    private String depmnotes;
//    private String anax;
//    private String ergo;
    private String city;
//    private String ys;
//    private String diakl;
//    private String sfrakbold;
//    private String sfrakrodold;
    private String reason;
//    private String kva;
//    private String mspl;
//    private String vrax;
    private Boolean tvl;
//    private String synestr;
    private String zone;
//    private String netype;
//    private String onom;
//    private String sfram;


    public Long getPfr_id() {
        return pfr_id;
    }

    public void setPfr_id(Long pfr_id) {
        this.pfr_id = pfr_id;
    }

    public String getVltype() {
        return vltype;
    }

    public void setVltype(String vltype) {
        this.vltype = vltype;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getVlkind() {
        return vlkind;
    }

    public void setVlkind(String vlkind) {
        this.vlkind = vlkind;
    }

    public Date getDtcreated() {
        return dtcreated;
    }

    public void setDtcreated(Date dtcreated) {
        this.dtcreated = dtcreated;
    }

    public String getVlval() {
        return vlval;
    }

    public void setVlval(String vlval) {
        this.vlval = vlval;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLast_upd_usr() {
        return last_upd_usr;
    }

    public void setLast_upd_usr(String last_upd_usr) {
        this.last_upd_usr = last_upd_usr;
    }

    public String getSyne() {
        return syne;
    }

    public void setSyne(String syne) {
        this.syne = syne;
    }

    public String getSyne2() {
        return syne2;
    }

    public void setSyne2(String syne2) {
        this.syne2 = syne2;
    }

    public String getDepm() {
        return depm;
    }

    public void setDepm(String depm) {
        this.depm = depm;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public Date getEnddt() {
        return enddt;
    }

    public void setEnddt(Date enddt) {
        this.enddt = enddt;
    }

    public String getInitstat() {
        return initstat;
    }

    public void setInitstat(String initstat) {
        this.initstat = initstat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getTvl() {
        return tvl;
    }

    public void setTvl(Boolean tvl) {
        this.tvl = tvl;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
