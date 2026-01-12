package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;

public class CallcenterPerformanceMetricsDTO {
    private String timePeriod;
    @JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date requestedDate;
    private int agents;
    private int announcements;

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public int getAgents() {
        return agents;
    }

    public void setAgents(int agents) {
        this.agents = agents;
    }

    public int getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(int announcements) {
        this.announcements = announcements;
    }
}
