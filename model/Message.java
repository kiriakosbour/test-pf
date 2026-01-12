package gr.deddie.pfr.model;

public class Message {
	public static final Integer SUCCESSFUL_REGISTRATION_MESSAGE_ID = 1;
	public static final Integer FAILURE_RECOVERED_SMS_MESSAGE_ID = 16;
	public static final Integer FAILURE_RECOVERED_MESSAGE_ID = 3;
	public static final Integer FAILURE_RECALLED_MESSAGE_ID = 14;
	public static final Integer FAILURE_ANNOUNCED_MESSAGE_ID = 1;
	public static final Integer FAILURE_ESTIMATED_RESTORE_TIME_MESSAGE_ID = 15;

	private Integer message_id;
    private String message_header;
    private String message_description;

	public Message() {
	}

	public Message(Integer message_id) {
		this.message_id = message_id;
	}

	public Integer getMessage_id() {
		return message_id;
	}
	public void setMessage_id(Integer message_id) {
		this.message_id = message_id;
	}
	public String getMessage_header() {
		return message_header;
	}
	public void setMessage_header(String message_header) {
		this.message_header = message_header;
	}
	public String getMessage_description() {
		return message_description;
	}
	public void setMessage_description(String message_description) {
		this.message_description = message_description;
	}
}
