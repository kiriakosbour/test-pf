package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;
import java.util.List;

public class PowerOutageIVRDTO {

	private Long id;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date start_date;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date end_date;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date end_date_announced;
	private Boolean is_scheduled;
	private PowerOutage.PowerOutageCause cause;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getEnd_date_announced() {
		return end_date_announced;
	}

	public void setEnd_date_announced(Date end_date_announced) {
		this.end_date_announced = end_date_announced;
	}

	public Boolean getIs_scheduled() {
		return is_scheduled;
	}

	public void setIs_scheduled(Boolean is_scheduled) {
		this.is_scheduled = is_scheduled;
	}

	public PowerOutage.PowerOutageCause getCause() {
		return cause;
	}

	public void setCause(PowerOutage.PowerOutageCause cause) {
		this.cause = cause;
	}

}
