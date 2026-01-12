package gr.deddie.pfr.model;

import java.util.Date;
import java.util.List;

public class PowerOutageTALDDTO {

	private Long id;
	private Date start_date;
	private Date end_date;
	private Date end_date_announced;
	private Boolean is_scheduled;
	private PowerOutage.PowerOutageCause cause;
	private Perifereia perifereia;
	private List<Long> lektikoGenikonDiakoponIdList;
	private List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList;
	private List<KallikratikosOTA> kallikratikosOTAList;
	private List<KallikratikiNomarxia> kallikratikiNomarxiaList;

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

	public Perifereia getPerifereia() {
		return perifereia;
	}

	public void setPerifereia(Perifereia perifereia) {
		this.perifereia = perifereia;
	}

	public List<Long> getLektikoGenikonDiakoponIdList() {
		return lektikoGenikonDiakoponIdList;
	}

	public void setLektikoGenikonDiakoponIdList(List<Long> lektikoGenikonDiakoponIdList) {
		this.lektikoGenikonDiakoponIdList = lektikoGenikonDiakoponIdList;
	}

	public List<KallikratikiDhmotikiEnothta> getKallikratikiDhmotikiEnothtaList() {
		return kallikratikiDhmotikiEnothtaList;
	}

	public void setKallikratikiDhmotikiEnothtaList(List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList) {
		this.kallikratikiDhmotikiEnothtaList = kallikratikiDhmotikiEnothtaList;
	}

	public List<KallikratikosOTA> getKallikratikosOTAList() {
		return kallikratikosOTAList;
	}

	public void setKallikratikosOTAList(List<KallikratikosOTA> kallikratikosOTAList) {
		this.kallikratikosOTAList = kallikratikosOTAList;
	}

	public List<KallikratikiNomarxia> getKallikratikiNomarxiaList() {
		return kallikratikiNomarxiaList;
	}

	public void setKallikratikiNomarxiaList(List<KallikratikiNomarxia> kallikratikiNomarxiaList) {
		this.kallikratikiNomarxiaList = kallikratikiNomarxiaList;
	}
}
