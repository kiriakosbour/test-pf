package gr.deddie.pfr.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.danielbechler.diff.introspection.ObjectDiffProperty;
import gr.deddie.pfr.utilities.*;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTypeSerializer;

//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Failure {
	final public static String[] auditedFields= {"/assigned_texniths/mhtroo", "/estimated_restore_time","/general_failure_group/id", "/final_restoration_time", "/texniths_assignment_timestamp", "/assigned_texniths_contractor/mhtroo"};

	@JsonbTypeSerializer(FailureStatusJsonBSerializer.class)
	@JsonSerialize(using = FailureStatusSerializer.class)
	public enum FailureStatus {
		ANNOUNCED ("ΑΝΑΓΓΕΛΙΑ"), IN_PROGRESS("ΣΕ ΕΞΕΛΙΞΗ"), RESOLVED_WITH_PENDING_ISSUES("ΑΠΟΚΑΤΑΣΤΑΘΗΚΕ ΜΕ ΕΚΚΡΕΜΟΤΗΤΑ"), RESOLVED("ΑΠΟΚΑΤΑΣΤΑΘΗΚΕ"), REJECTED("ΑΠΟΡΡΙΦΘΗΚΕ"), RECALLED_BY_ANNOUNCER("ΑΝΑΚΛΗΣΗ ΑΠΟ ΚΑΤΑΝΑΛΩΤΗ"), UNNECESSARY_VISIT("ΑΣΚΟΠΗ ΜΕΤΑΒΑΣΗ");
		private String value;

		FailureStatus(String s) {
			this.value = s;
		}

		public String getValue() {
			return value;
		}
	}

	@JsonSerialize(using = GeneralFailureGroupSerializer.class)
	public enum GeneralFailureGroup {
		GROUP_1("ΟΜΑΔΑ_1"), GROUP_2("ΟΜΑΔΑ_2"), GROUP_3("ΟΜΑΔΑ_3"), GROUP_4("ΟΜΑΔΑ_4"), GROUP_5("ΟΜΑΔΑ_5"),
		GROUP_6("ΟΜΑΔΑ_6"), GROUP_7("ΟΜΑΔΑ_7"), GROUP_8("ΟΜΑΔΑ_8"), GROUP_9("ΟΜΑΔΑ_9"), GROUP_10("ΟΜΑΔΑ_10"),
		GROUP_11("ΟΜΑΔΑ_11"), GROUP_12("ΟΜΑΔΑ_12"), GROUP_13("ΟΜΑΔΑ_13"), GROUP_14("ΟΜΑΔΑ_14"), GROUP_15("ΟΜΑΔΑ_15"),
		GROUP_16("ΟΜΑΔΑ_16"), GROUP_17("ΟΜΑΔΑ_17"), GROUP_18("ΟΜΑΔΑ_18"), GROUP_19("ΟΜΑΔΑ_19"), GROUP_20("ΟΜΑΔΑ_20"),
		GROUP_21("ΟΜΑΔΑ_21"), GROUP_22("ΟΜΑΔΑ_22"), GROUP_23("ΟΜΑΔΑ_23"), GROUP_24("ΟΜΑΔΑ_24"), GROUP_25("ΟΜΑΔΑ_25"),
		GROUP_26("ΟΜΑΔΑ_26"), GROUP_27("ΟΜΑΔΑ_27"), GROUP_28("ΟΜΑΔΑ_28"), GROUP_29("ΟΜΑΔΑ_29"), GROUP_30("ΟΜΑΔΑ_30"),
		GROUP_31("ΟΜΑΔΑ_31"), GROUP_32("ΟΜΑΔΑ_32"), GROUP_33("ΟΜΑΔΑ_33"), GROUP_34("ΟΜΑΔΑ_34"), GROUP_35("ΟΜΑΔΑ_35"),
		GROUP_36("ΟΜΑΔΑ_36"), GROUP_37("ΟΜΑΔΑ_37"), GROUP_38("ΟΜΑΔΑ_38"), GROUP_39("ΟΜΑΔΑ_39"), GROUP_40("ΟΜΑΔΑ_40"),
		GROUP_41("ΟΜΑΔΑ_41"), GROUP_42("ΟΜΑΔΑ_42"), GROUP_43("ΟΜΑΔΑ_43"), GROUP_44("ΟΜΑΔΑ_44"), GROUP_45("ΟΜΑΔΑ_45"),
		GROUP_46("ΟΜΑΔΑ_46"), GROUP_47("ΟΜΑΔΑ_47"), GROUP_48("ΟΜΑΔΑ_48"), GROUP_49("ΟΜΑΔΑ_49"), GROUP_50("ΟΜΑΔΑ_50");
//		GROUP_51("ΟΜΑΔΑ_51"), GROUP_52("ΟΜΑΔΑ_52"), GROUP_53("ΟΜΑΔΑ_53"), GROUP_54("ΟΜΑΔΑ_54"), GROUP_55("ΟΜΑΔΑ_55"),
//		GROUP_56("ΟΜΑΔΑ_56"), GROUP_57("ΟΜΑΔΑ_57"), GROUP_58("ΟΜΑΔΑ_58"), GROUP_59("ΟΜΑΔΑ_59"), GROUP_60("ΟΜΑΔΑ_60"),
//		GROUP_61("ΟΜΑΔΑ_61"), GROUP_62("ΟΜΑΔΑ_62"), GROUP_63("ΟΜΑΔΑ_63"), GROUP_64("ΟΜΑΔΑ_64"), GROUP_65("ΟΜΑΔΑ_65"),
//		GROUP_66("ΟΜΑΔΑ_66"), GROUP_67("ΟΜΑΔΑ_67"), GROUP_68("ΟΜΑΔΑ_68"), GROUP_69("ΟΜΑΔΑ_69"), GROUP_70("ΟΜΑΔΑ_70"),
//		GROUP_71("ΟΜΑΔΑ_71"), GROUP_72("ΟΜΑΔΑ_72"), GROUP_73("ΟΜΑΔΑ_73"), GROUP_74("ΟΜΑΔΑ_74"), GROUP_75("ΟΜΑΔΑ_75"),
//		GROUP_76("ΟΜΑΔΑ_76"), GROUP_77("ΟΜΑΔΑ_77"), GROUP_78("ΟΜΑΔΑ_78"), GROUP_79("ΟΜΑΔΑ_79"), GROUP_80("ΟΜΑΔΑ_80"),
//		GROUP_81("ΟΜΑΔΑ_81"), GROUP_82("ΟΜΑΔΑ_82"), GROUP_83("ΟΜΑΔΑ_83"), GROUP_84("ΟΜΑΔΑ_84"), GROUP_85("ΟΜΑΔΑ_85"),
//		GROUP_86("ΟΜΑΔΑ_86"), GROUP_87("ΟΜΑΔΑ_87"), GROUP_88("ΟΜΑΔΑ_88"), GROUP_89("ΟΜΑΔΑ_89"), GROUP_90("ΟΜΑΔΑ_90"),
//		GROUP_91("ΟΜΑΔΑ_91"), GROUP_92("ΟΜΑΔΑ_92"), GROUP_93("ΟΜΑΔΑ_93"), GROUP_94("ΟΜΑΔΑ_94"), GROUP_95("ΟΜΑΔΑ_95"),
//		GROUP_96("ΟΜΑΔΑ_96"), GROUP_97("ΟΜΑΔΑ_97"), GROUP_98("ΟΜΑΔΑ_98"), GROUP_99("ΟΜΑΔΑ_99"), GROUP_100("ΟΜΑΔΑ_100");
		private String value;

		GeneralFailureGroup(String s) {
			this.value = s;
		}

		public String getValue() {
			return value;
		}

		public GeneralFailureGroup fromString(final String s) {
			return GeneralFailureGroup.valueOf(s);
		}
	}

	@JsonSerialize(using = InputChannelSerializer.class)
	public enum InputChannel {
		WEBSITE ("www.deddie.gr"), CALLCENTER_AGENT ("Τηλεφωνικό κέντρο"), DEDDIE_USER("Χρήστης ΔΕΔΔΗΕ"),
		MOBILE_APP_IOS("Mobile App iOS version"), MOBILE_APP_ANDROID("Mobile App android version");
		private String value;

		InputChannel(String s) {
			this.value = s;
		}

		public String getValue() {
			return value;
		}
	}

	private Long id;
    private String area;
    private Long area_id;
    private String address;
    private String postal_code;
	private String supply_no_partial;
	private String name;
    private String phone;
    private String cell;
    private String email;
	private FailureType failureType;
	private String comments;
    private List<Landmark> landmarks;
    private List<FailureHistory> events;
    private List<Question> questions;
	private List<FailureAssignment> assignments;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddhhmm")
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date estimated_restore_time;
	private Misthotos assigned_texniths;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	@JsonbDateFormat(JsonbDateFormat.TIME_IN_MILLIS)
	private Date texniths_assignment_timestamp;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date created;
	private String creator;
	private String real_estate_usage;
	private Boolean dangerous;
	private Boolean is_general_failure;
	private gr.deddie.pfr.model.GeneralFailureGroup general_failure_group;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date final_restoration_time;
	private Otp otp;
	private Boolean is_user_of_supply;
	private InputChannel input_channel;
	private FailureStatus status;
	private String supply_no;
	private Grafeio grafeio;
	private String latitude;
	private String longitute;
	private String questions_serialized;
	private KallikratikosOTA klot;
	private SabMT sab_MT;
	private SabXT sab_XT;
	private SabParoxis sab_Paroxis;
	private SabDiafora sab_Diafora;
	private ContractorMisthotos assigned_texniths_contractor;

//	public Failure(Integer id, String area, String address, String postal_code, String supply_no, String name, String phone, String cell, String email, Integer failure_type_id, List<Landmark> landmarks, Date created, String creator, String real_estate_usage) {
//		this.id = id;
//		this.area = area;
//		this.address = address;
//		this.postal_code = postal_code;
//		this.supply_no = supply_no;
//		this.name = name;
//		this.phone = phone;
//		this.cell = cell;
//		this.email = email;
//		this.failure_type_id = failure_type_id;
//		this.landmarks = landmarks;
//		this.created = created;
//		this.creator = creator;
//		this.real_estate_usage = real_estate_usage;
//	}


	//	public Failure(Integer id, Long area_id, String failure_type_description, Date estimated_restore_time) {
//		this.id = id;
//		this.area_id = area_id;
//		this.failure_type_description = failure_type_description;
//		this.estimated_restore_time = estimated_restore_time;
//	}

//	public Failure(Integer id, Long area_id, Integer failure_type_id, Date estimated_restore_time) {
//		this.id = id;
//		this.area_id = area_id;
//		this.failure_type_id = failure_type_id;
//		this.estimated_restore_time = estimated_restore_time;
//	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ObjectDiffProperty(excluded = true)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@ObjectDiffProperty(excluded = true)
	public Long getArea_id() {
		return area_id;
	}

	public void setArea_id(Long area_id) {
		this.area_id = area_id;
	}

	@ObjectDiffProperty(excluded = true)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@ObjectDiffProperty(excluded = true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@ObjectDiffProperty(excluded = true)
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	@ObjectDiffProperty(excluded = true)
	public FailureType getFailureType() {
		return failureType;
	}

	public void setFailureType(FailureType failureType) {
		this.failureType = failureType;
	}

	@ObjectDiffProperty(excluded = true)
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	@ObjectDiffProperty(excluded = true)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ObjectDiffProperty(excluded = true)
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}

	@ObjectDiffProperty(excluded = true)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@ObjectDiffProperty(excluded = true)
	public List<Landmark> getLandmarks() {
		return landmarks;
	}

	public void setLandmarks(List<Landmark> landmarks) {
		this.landmarks = landmarks;
	}

	@ObjectDiffProperty(excluded = true)
	public List<FailureHistory> getEvents() {
		return events;
	}

	public void setEvents(List<FailureHistory> events) {
		this.events = events;
	}

	@ObjectDiffProperty(excluded = true)
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@ObjectDiffProperty(excluded = true)
	public List<FailureAssignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<FailureAssignment> assignments) {
		this.assignments = assignments;
	}

	public Date getEstimated_restore_time() {
		return estimated_restore_time;
	}

	public void setEstimated_restore_time(Date estimated_restore_time) {
		this.estimated_restore_time = estimated_restore_time;
	}

	@ObjectDiffProperty(excluded = true)
	public String getSupply_no() {
		return supply_no;
	}

	public void setSupply_no(String supply_no) {
		this.supply_no = supply_no;
	}

	@ObjectDiffProperty(excluded = true)
	public String getSupply_no_partial() {
		return supply_no_partial;
	}

	public void setSupply_no_partial(String supply_no_partial) {
		this.supply_no_partial = supply_no_partial;
	}

	public Misthotos getAssigned_texniths() {
		return assigned_texniths;
	}

	public void setAssigned_texniths(Misthotos assigned_texniths) {
		this.assigned_texniths = assigned_texniths;
	}

	public Date getTexniths_assignment_timestamp() {
		return texniths_assignment_timestamp;
	}

	public void setTexniths_assignment_timestamp(Date texniths_assignment_timestamp) {
		this.texniths_assignment_timestamp = texniths_assignment_timestamp;
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
	public String getReal_estate_usage() {
		return real_estate_usage;
	}

	public void setReal_estate_usage(String real_estate_usage) {
		this.real_estate_usage = real_estate_usage;
	}

	@ObjectDiffProperty(excluded = true)
	public Boolean getDangerous() {
		return dangerous;
	}

	public void setDangerous(Boolean dangerous) {
		this.dangerous = dangerous;
	}

	@ObjectDiffProperty(excluded = true)
	public Boolean getIs_general_failure() {
		return is_general_failure;
	}

	public void setIs_general_failure(Boolean is_general_failure) {
		this.is_general_failure = is_general_failure;
	}

	public gr.deddie.pfr.model.GeneralFailureGroup getGeneral_failure_group() {
		return general_failure_group;
	}

	public void setGeneral_failure_group(gr.deddie.pfr.model.GeneralFailureGroup general_failure_group) {
		this.general_failure_group = general_failure_group;
	}

	public Date getFinal_restoration_time() {
		return final_restoration_time;
	}

	public void setFinal_restoration_time(Date final_restoration_time) {
		this.final_restoration_time = final_restoration_time;
	}

	@ObjectDiffProperty(excluded = true)
	public Otp getOtp() {
		return otp;
	}

	public void setOtp(Otp otp) {
		this.otp = otp;
	}

	@ObjectDiffProperty(excluded = true)
	public Boolean getIs_user_of_supply() {
		return is_user_of_supply;
	}

	public void setIs_user_of_supply(Boolean is_user_of_supply) {
		this.is_user_of_supply = is_user_of_supply;
	}

	@ObjectDiffProperty(excluded = true)
	public InputChannel getInput_channel() {
		return input_channel;
	}

	public void setInput_channel(InputChannel input_channel) {
		this.input_channel = input_channel;
	}

	@ObjectDiffProperty(excluded = true)
	public FailureStatus getStatus() {
		return status;
	}

	public void setStatus(FailureStatus status) {
		this.status = status;
	}

	@ObjectDiffProperty(excluded = true)
	public Grafeio getGrafeio() {
		return grafeio;
	}

	public void setGrafeio(Grafeio grafeio) {
		this.grafeio = grafeio;
	}

	@Override
	public String toString() {
		return "Failure:" +
				"{id: " + (id == null ? "null" : id.toString()) + "," +
				" area: " + (area == null ? "null" : area)  + "," +
				" address: " + (address == null ? "null" : address) + "," +
				" postal_code: " + (postal_code == null ? "null" : postal_code) + "," +
				" supply_no: " + (supply_no == null ? "null" : supply_no) + "," +
				" name: " + (name == null ? "null" : name) + "," +
				" phone: " + (phone == null ? "null" : phone) + "," +
				" cell: " + (cell == null ? "null" : cell) + "," +
				" email: " + (email == null ? "null" : email) + "," +
				" failureType: " + (failureType == null ? "null" : failureType.getDescription()) + "," +
				" comments: " + (comments == null ? "null" : comments) + "," +
				" landmarks: " + (landmarks == null ? "null" : landmarks) + "," +
				" events: " + (events == null ? "null" : events.get(0).getStatus()) + "," +
				" assignment: " + (assignments == null ? "null" : assignments.get(0).getAssignee().getPeri()) + "," +
				" questions: " + (questions == null ? "null" : questions) + "," +
				" estimated_restore_time: " + (estimated_restore_time == null ? "null" : estimated_restore_time.toString()) + "," +
				" assigned_texniths: " + (assigned_texniths == null ? "null" : (assigned_texniths.getMhtroo() == null ? "null" : assigned_texniths.getMhtroo().toString())) + "," +
				" assigned_texniths_contractor: " + (assigned_texniths_contractor == null ? "null" : (assigned_texniths_contractor.getMhtroo() == null ? "null" : assigned_texniths_contractor.getMhtroo())) + "," +
				" created: " + (created == null ? "null" : created.toString()) + "," +
				" creator: " + (creator == null ? "null" : creator) + "," +
				" real_estate_usage: " + (real_estate_usage == null ? "null" : real_estate_usage) + "," +
				" dangerous: " + (dangerous == null ? "null" : dangerous.toString()) + "," +
				"}";
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitute() {
		return longitute;
	}
	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}
	public String getQuestions_serialized() {
		return questions_serialized;
	}
	public void setQuestions_serialized(String questions_serialized) {
		this.questions_serialized = questions_serialized;
	}

	public KallikratikosOTA getKlot() {
		return klot;
	}

	public void setKlot(KallikratikosOTA klot) {
		this.klot = klot;
	}

	public SabMT getSab_MT() {
		return sab_MT;
	}

	public void setSab_MT(SabMT sab_MT) {
		this.sab_MT = sab_MT;
	}

	public SabXT getSab_XT() {
		return sab_XT;
	}

	public void setSab_XT(SabXT sab_XT) {
		this.sab_XT = sab_XT;
	}

	public SabParoxis getSab_Paroxis() {
		return sab_Paroxis;
	}

	public void setSab_Paroxis(SabParoxis sab_Paroxis) {
		this.sab_Paroxis = sab_Paroxis;
	}

	public SabDiafora getSab_Diafora() {
		return sab_Diafora;
	}

	public void setSab_Diafora(SabDiafora sab_Diafora) {
		this.sab_Diafora = sab_Diafora;
	}

	public ContractorMisthotos getAssigned_texniths_contractor() {
		return assigned_texniths_contractor;
	}

	public void setAssigned_texniths_contractor(ContractorMisthotos assigned_texniths_contractor) {
		this.assigned_texniths_contractor = assigned_texniths_contractor;
	}
}

