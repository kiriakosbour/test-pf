package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Contact information for fault reports.
 * Used in both inbound and outbound API calls.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FibergridContactInformation {

    private String name;
    private String phone;
    private String email;

    public FibergridContactInformation() {
    }

    public FibergridContactInformation(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Basic email format validation.
     */
    public boolean hasValidEmail() {
        if (email == null || email.isEmpty()) {
            return true; // Email is optional
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    @Override
    public String toString() {
        return "FibergridContactInformation{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
