package gr.deddie.pfr.model;

import gr.deddie.pfr.utilities.UnixTimestampSerializer;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.Date;

public class FailureHistory extends Message{
	public enum ContactMethod {email,sms,no_contact}

    private Integer id;
    private Long failure_id;
	@JsonbTypeSerializer(UnixTimestampSerializer.class)
    private Date created;
    private String contact_method;
    private String status;
    private String comments;
    private String creator;

    public FailureHistory() {
        super();
        this.id = id;
    }

	public FailureHistory(Long failure_id, Integer message_id, String contact_method, String status, String comments, String creator) {
		super(message_id);
		this.failure_id = failure_id;
		this.contact_method = contact_method;
		this.status = status;
		this.comments = comments;
		this.creator = creator;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getFailure_id() {
		return failure_id;
	}
	public void setFailure_id(Long failure_id) {
		this.failure_id = failure_id;
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getContact_method() {
		return contact_method;
	}
	public void setContact_method(String contact_method) {
		this.contact_method = contact_method;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

}
