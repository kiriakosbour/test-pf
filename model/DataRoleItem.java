package gr.deddie.pfr.model;

/**
 * Created by M.Masikos on 9/12/2016.
 */
public class DataRoleItem {
    private long id;
    private String item;
    private String descr;
    private long data_role_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public long getData_role_id() {
        return data_role_id;
    }

    public void setData_role_id(long data_role_id) {
        this.data_role_id = data_role_id;
    }
}
