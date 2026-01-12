package gr.deddie.pfr.model;

public class FailureAudit extends Audit {
    private Long failure_id;

    public FailureAudit(String property, String state, String new_value, String creator, Long failure_id) {
        super(property, state, new_value, creator);
        this.failure_id = failure_id;
    }

    public Long getFailure_id() {
        return failure_id;
    }

    public void setFailure_id(Long failure_id) {
        this.failure_id = failure_id;
    }

    @Override
    public String toString() {
        return "FailureAudit:" +
                "{failure_id: " + (failure_id == null ? "null" : failure_id.toString()) + "," +
                " property: " + (getProperty() == null ? "null" : getProperty()) + "," +
                " state: " + (getState() == null ? "null" : getState()) + "," +
                " new_value: " + (getNew_value() == null ? "null" : getNew_value()) + "," +
                " creator: " + (getCreator() == null ? "null" : getCreator()) +
                "}";
    }
}
