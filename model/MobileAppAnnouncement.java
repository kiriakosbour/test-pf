package gr.deddie.pfr.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gr.deddie.pfr.utilities.DEVICEOSSerializer;

import java.util.Date;

public class MobileAppAnnouncement {

    @JsonSerialize(using = DEVICEOSSerializer.class)
    public enum DeviceOS {
        IOS ("iOS"), ANDROID("ANDROID"), BOTH("iOS/ANDROID");
        private String value;

        DeviceOS(String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }
    }

    private long id;
    private Boolean is_enabled;
    private String message;
    private Date valid_from;
    private Date valid_to;
    private Date created;
    private String creator;
    private String version;
    private DeviceOS os;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getValid_from() {
        return valid_from;
    }

    public void setValid_from(Date valid_from) {
        this.valid_from = valid_from;
    }

    public Date getValid_to() {
        return valid_to;
    }

    public void setValid_to(Date valid_to) {
        this.valid_to = valid_to;
    }

    public Boolean getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(Boolean is_enabled) {
        this.is_enabled = is_enabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DeviceOS getOs() {
        return os;
    }

    public void setOs(DeviceOS os) {
        this.os = os;
    }
}
