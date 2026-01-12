package gr.deddie.pfr.model;

import java.util.ArrayList;

/**
 * Created by M.Masikos on 21/11/2016.
 */
public class UserInfoDTO {
    private int id;
    private String username;
    private String fullname;
    private String am;
    private String email;
    private String telephone;
    private String mobile;
    private ArrayList<UserDataRole> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ArrayList<UserDataRole> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<UserDataRole> roles) {
        this.roles = roles;
    }
}
