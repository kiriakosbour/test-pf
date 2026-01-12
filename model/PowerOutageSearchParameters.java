package gr.deddie.pfr.model;

import java.util.Date;

/**
 * Created by M.Paschou on 18/12/2017.
 */

public class PowerOutageSearchParameters {
	private Date start_date;
    private Date end_date;
    private GrammiMT grammiMT;
	private Boolean is_active;
	private Boolean is_scheduled;
	private String cause;

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

	public GrammiMT getGrammiMT() {
		return grammiMT;
	}

	public void setGrammiMT(GrammiMT grammiMT) {
		this.grammiMT = grammiMT;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Boolean getIs_scheduled() {
		return is_scheduled;
	}

	public void setIs_scheduled(Boolean is_scheduled) {
		this.is_scheduled = is_scheduled;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}
