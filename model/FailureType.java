package gr.deddie.pfr.model;

public class FailureType {
	public static final Integer DANGEROUS_STATE_ID = 29;
	public static final Integer GENERAL_FAILURE_ID = 25;
	public static final Integer ISOLATED_FAILURE_ID = 26;
	public static final Integer TRANSIENT_MV_FAULT_ID = 30;

    private Integer id;
    private String description;

	public FailureType() {
	}

	public FailureType(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
