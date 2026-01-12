package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import gr.deddie.pfr.utilities.PowerOutageCauseSerializer;
import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PowerOutage {
	final public static String[] auditedFields= {"/graf", "/start_date", "/end_date", "/is_active", "/is_scheduled", "/cause", "/tald", "/perifereia/id"};

	private Long id;
	private Grafeio grafeio;
	private String tald;
	private Long power_outage_dfr_id;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date start_date;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date end_date;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date end_date_announced;
	private Boolean is_active;
	private Boolean is_scheduled;
	private Perifereia perifereia;

//	@Override
//	public String toString() {
//		return "PowerOutage{" +
//				"id:" + id +
//				",grafeio:" + grafeio +
//				",tald:\"" + tald + '\'' +
//				",power_outage_dfr_id:" + power_outage_dfr_id +
//				",start_date:" + start_date +
//				",end_date:" + end_date +
//				",end_date_announced:" + end_date_announced +
//				",is_active:" + is_active +
//				",is_scheduled:" + is_scheduled +
//				",perifereia:" + perifereia +
//				",cause:" + cause.toString() +
//				",created:" + created +
//				",creator:'" + creator + '\'' +
//				",exyphretoumeniPerioxiList:" + exyphretoumeniPerioxiList +
//				",lektikoGenikonDiakoponList:" + lektikoGenikonDiakoponList +
//				",exyphretoumeniDhmEnothtaList:" + exyphretoumeniDhmEnothtaList +
//				",kallikratikiDhmotikiEnothtaList:" + kallikratikiDhmotikiEnothtaList +
//				",kallikratikosOTAList:" + kallikratikosOTAList +
//				",kallikratikiNomarxiaList:" + kallikratikiNomarxiaList +
//				",text:\"" + text + '\"' +
//				'}';
//	}

	@JsonSerialize(using = PowerOutageCauseSerializer.class)
	public enum PowerOutageCause {
		OUTAGE ("Βλάβη"), OPERATION ("Λειτουργία"), MAINTENANCE("Συντήρηση"), CONSTRUCTION("Κατασκευές"), ADMHE("ΑΔΜΗΕ"), OTHER_BUSINESS_UNITS("Άλλες ΒΕΜ"), THIRD_PARTY_SERVICES("Εξυπηρέτηση τρίτων"), STRIKE("Λόγω απεργίας");
		private String value;

		PowerOutageCause(String s) {
			this.value = s;
		}

		public String getValue() {
			return value;
		}

	}
	private PowerOutageCause cause;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date created;
	private String creator;
	private List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList;
	private List<LektikoGenikonDiakopon> lektikoGenikonDiakoponList;
	private List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList;
	private List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList;
	private List<KallikratikosOTA> kallikratikosOTAList;
	private List<KallikratikiNomarxia> kallikratikiNomarxiaList;
	// ivr message
	private String text;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Grafeio getGrafeio() {
		return grafeio;
	}

	public void setGrafeio(Grafeio grafeio) {
		this.grafeio = grafeio;
	}

	public String getTald() {
		return tald;
	}

	public void setTald(String tald) {
		this.tald = tald;
	}

	public Long getPower_outage_dfr_id() {
		return power_outage_dfr_id;
	}

	public void setPower_outage_dfr_id(Long power_outage_dfr_id) {
		this.power_outage_dfr_id = power_outage_dfr_id;
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

	public Perifereia getPerifereia() {
		return perifereia;
	}

	public void setPerifereia(Perifereia perifereia) {
		this.perifereia = perifereia;
	}

	public PowerOutageCause getCause() {
		return cause;
	}

	public void setCause(PowerOutageCause cause) {
		this.cause = cause;
	}

	@ObjectDiffProperty(excluded = true)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@ObjectDiffProperty(excluded = true)
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@ObjectDiffProperty(excluded = true)
	public List<ExyphretoumeniPerioxi> getExyphretoumeniPerioxiList() {
		return exyphretoumeniPerioxiList;
	}

	public void setExyphretoumeniPerioxiList(List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList) {
		this.exyphretoumeniPerioxiList = exyphretoumeniPerioxiList;
	}

	public List<LektikoGenikonDiakopon> getLektikoGenikonDiakoponList() {
		return lektikoGenikonDiakoponList;
	}

	public void setLektikoGenikonDiakoponList(List<LektikoGenikonDiakopon> lektikoGenikonDiakoponList) {
		this.lektikoGenikonDiakoponList = lektikoGenikonDiakoponList;
	}

	public List<ExyphretoumeniDhmEnothta> getExyphretoumeniDhmEnothtaList() {
		return exyphretoumeniDhmEnothtaList;
	}

	public void setExyphretoumeniDhmEnothtaList(List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList) {
		this.exyphretoumeniDhmEnothtaList = exyphretoumeniDhmEnothtaList;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
