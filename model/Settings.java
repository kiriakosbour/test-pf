package gr.deddie.pfr.model;

public class Settings {
    private String handheld_notific_endpoint;
    private Long handheld_notific_audit_flg;

    public String getHandheld_notific_endpoint() {
        return handheld_notific_endpoint;
    }

    public void setHandheld_notific_endpoint(String handheld_notific_endpoint) {
        this.handheld_notific_endpoint = handheld_notific_endpoint;
    }

    public Long getHandheld_notific_audit_flg() {
        return handheld_notific_audit_flg;
    }

    public void setHandheld_notific_audit_flg(Long handheld_notific_audit_flg) {
        this.handheld_notific_audit_flg = handheld_notific_audit_flg;
    }
}
