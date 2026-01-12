package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;
import java.util.List;

/**
 * Created by M.Masikos on 9/5/2017.
 */
public class FailureStatsDTO {
    private GeneralFailureGroup generalFailureGroup;
    private Integer counter;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date lastAnnouncementTimestamp;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date lastActionTimestamp;
    private List<FailureDTO> failureDTOs;
    private Boolean dangerous;
    private String status;

    public FailureStatsDTO() {
    }

    public FailureStatsDTO(GeneralFailureGroup generalFailureGroup, Integer counter, Date lastAnnouncementTimestamp, Date lastActionTimestamp, List<FailureDTO> failureDTOs, Boolean dangerous, String status) {
        this.generalFailureGroup = generalFailureGroup;
        this.counter = counter;
        this.lastAnnouncementTimestamp = lastAnnouncementTimestamp;
        this.lastActionTimestamp = lastActionTimestamp;
        this.failureDTOs = failureDTOs;
        this.dangerous = dangerous;
        this.status = status;
    }

    public GeneralFailureGroup getGeneralFailureGroup() {
        return generalFailureGroup;
    }

    public void setGeneralFailureGroup(GeneralFailureGroup generalFailureGroup) {
        this.generalFailureGroup = generalFailureGroup;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Date getLastAnnouncementTimestamp() {
        return lastAnnouncementTimestamp;
    }

    public void setLastAnnouncementTimestamp(Date lastAnnouncementTimestamp) {
        this.lastAnnouncementTimestamp = lastAnnouncementTimestamp;
    }

    public Date getLastActionTimestamp() {
        return lastActionTimestamp;
    }

    public void setLastActionTimestamp(Date lastActionTimestamp) {
        this.lastActionTimestamp = lastActionTimestamp;
    }

    public List<FailureDTO> getFailureDTOs() {
        return failureDTOs;
    }

    public void setFailureDTOs(List<FailureDTO> failureDTOs) {
        this.failureDTOs = failureDTOs;
    }

    public Boolean getDangerous() {
        return dangerous;
    }

    public void setDangerous(Boolean dangerous) {
        this.dangerous = dangerous;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
