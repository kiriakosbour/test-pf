package gr.deddie.pfr.model;

/**
 * Created by M.Masikos on 24/11/2016.
 */
public class UserDataRole {
    public static final String NO_SECURITY_TOKEN = "DEDDHE_GLOBAL";

    private int id;
    private String name;
    private String descr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
