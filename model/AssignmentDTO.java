package gr.deddie.pfr.model;

/**
 * Created by M.Masikos on 24/10/2016.
 */
public class AssignmentDTO {
    private long id;
    private String monada;
    private long user_id;
    private int app_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMonada() {
        return monada;
    }

    public void setMonada(String monada) {
        this.monada = monada;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }
}
