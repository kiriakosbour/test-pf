package gr.deddie.pfr.model;

public class PowerOutageAudit extends Audit {
    private Long power_outage_id;

    public PowerOutageAudit(String property, String state, String new_value, String creator, Long power_outage_id) {
        super(property, state, new_value, creator);
        this.power_outage_id = power_outage_id;
    }

    public Long getPower_outage_id() {
        return power_outage_id;
    }

    public void setPower_outage_id(Long power_outage_id) {
        this.power_outage_id = power_outage_id;
    }
}
