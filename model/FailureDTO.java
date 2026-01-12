package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;

public class FailureDTO {
    private Long id;
    private String area;
    private String address;
	private String postal_code;
	private String supply_no;
	private String name;
    private String phone;
    private String cell;
    private String email;
	private FailureType failureType;
	private FailureAssignment failureAssignment;
	private String comments;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date created;
	private String creator;
	private String real_estate_usage;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date announced;
    private String status;
    private String last_update_comments;
    private Misthotos assigned_texniths;
    private ContractorMisthotos assigned_texniths_contractor;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date texniths_assignment_timestamp;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date estimated_restore_time;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date last_updated;
	private Boolean dangerous;
	private Boolean is_general_failure;
	private GeneralFailureGroup general_failure_group;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
	private Date final_restoration_time;
	private KallikratikosOTA klot;

	public FailureDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getSupply_no() {
		return supply_no;
	}

	public void setSupply_no(String supply_no) {
		this.supply_no = supply_no;
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

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public FailureType getFailureType() {
		return failureType;
	}

	public void setFailureType(FailureType failureType) {
		this.failureType = failureType;
	}

	public FailureAssignment getFailureAssignment() {
		return failureAssignment;
	}

	public void setFailureAssignment(FailureAssignment failureAssignment) {
		this.failureAssignment = failureAssignment;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getReal_estate_usage() {
		return real_estate_usage;
	}

	public void setReal_estate_usage(String real_estate_usage) {
		this.real_estate_usage = real_estate_usage;
	}

	public Date getAnnounced() {
		return announced;
	}

	public void setAnnounced(Date announced) {
		this.announced = announced;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLast_update_comments() {
		return last_update_comments;
	}

	public void setLast_update_comments(String last_update_comments) {
		this.last_update_comments = last_update_comments;
	}

	public Misthotos getAssigned_texniths() {
		return assigned_texniths;
	}

	public void setAssigned_texniths(Misthotos assigned_texniths) {
		this.assigned_texniths = assigned_texniths;
	}

	public ContractorMisthotos getAssigned_texniths_contractor() {
		return assigned_texniths_contractor;
	}

	public void setAssigned_texniths_contractor(ContractorMisthotos assigned_texniths_contractor) {
		this.assigned_texniths_contractor = assigned_texniths_contractor;
	}

	public Date getTexniths_assignment_timestamp() {
		return texniths_assignment_timestamp;
	}

	public void setTexniths_assignment_timestamp(Date texniths_assignment_timestamp) {
		this.texniths_assignment_timestamp = texniths_assignment_timestamp;
	}

	public Date getEstimated_restore_time() {
		return estimated_restore_time;
	}

	public void setEstimated_restore_time(Date estimated_restore_time) {
		this.estimated_restore_time = estimated_restore_time;
	}

	public Date getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(Date last_updated) {
		this.last_updated = last_updated;
	}

	public Boolean getDangerous() {
		return dangerous;
	}

	public void setDangerous(Boolean dangerous) {
		this.dangerous = dangerous;
	}

	public Boolean getIs_general_failure() {
		return is_general_failure;
	}

	public void setIs_general_failure(Boolean is_general_failure) {
		this.is_general_failure = is_general_failure;
	}

	public GeneralFailureGroup getGeneral_failure_group() {
		return general_failure_group;
	}

	public void setGeneral_failure_group(GeneralFailureGroup general_failure_group) {
		this.general_failure_group = general_failure_group;
	}

	public Date getFinal_restoration_time() {
		return final_restoration_time;
	}

	public void setFinal_restoration_time(Date final_restoration_time) {
		this.final_restoration_time = final_restoration_time;
	}

	public KallikratikosOTA getKlot() {
		return klot;
	}

	public void setKlot(KallikratikosOTA klot) {
		this.klot = klot;
	}
}
