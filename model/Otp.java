package gr.deddie.pfr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gr.deddie.pfr.utilities.FailureStatusSerializer;

import java.util.Date;

public class Otp {
    public static final Integer VALIDITY_PERIOD_IN_SECONDS = 300;

    //TODO fix serializer
    @JsonSerialize(using = FailureStatusSerializer.class)
    public enum OtpType {
        FAILURE_ANNOUNCEMENT (0), FAILURE_RECALL(1);
        private int id;

        OtpType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

    }

    private Long id;
    private String cell;
    private String pin;
    private OtpType type;
    private Date created;
    private Boolean used;

    public Otp() {
    }

    public Otp(String cell, String pin, OtpType otpType) {
        this.cell = cell;
        this.pin = pin;
        this.type = otpType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public OtpType getType() {
        return type;
    }

    public void setType(OtpType type) {
        this.type=type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
