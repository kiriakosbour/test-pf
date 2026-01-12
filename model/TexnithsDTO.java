package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;
import java.util.List;

public class TexnithsDTO {
    private Long mhtroo;
    private String lastname;
    private String firstname;
    private String fathersname;
    private String kathgoria;
    private String eidikothta;
    private List<Grafeio> grafeioList;
    private String lon;
    private String lat;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date position_updated;
    private List<Failure> assignedFailures;

    public Long getMhtroo() {
        return mhtroo;
    }

    public void setMhtroo(Long mhtroo) {
        this.mhtroo = mhtroo;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFathersname() {
        return fathersname;
    }

    public void setFathersname(String fathersname) {
        this.fathersname = fathersname;
    }

    public String getKathgoria() {
        return kathgoria;
    }

    public void setKathgoria(String kathgoria) {
        this.kathgoria = kathgoria;
    }

    public String getEidikothta() {
        return eidikothta;
    }

    public void setEidikothta(String eidikothta) {
        this.eidikothta = eidikothta;
    }

    public List<Grafeio> getGrafeioList() {
        return grafeioList;
    }

    public void setGrafeioList(List<Grafeio> grafeioList) {
        this.grafeioList = grafeioList;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Date getPosition_updated() {
        return position_updated;
    }

    public void setPosition_updated(Date position_updated) {
        this.position_updated = position_updated;
    }

    public List<Failure> getAssignedFailures() {
        return assignedFailures;
    }

    public void setAssignedFailures(List<Failure> assignedFailures) {
        this.assignedFailures = assignedFailures;
    }
}
