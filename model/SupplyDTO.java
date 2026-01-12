package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.awt.geom.Point2D;

/**
 * Created by M.Masikos on 24/4/2017.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplyDTO {
    private String no;
    private String prefecture;
    private Integer lastTenant;
    private String lastTenantName;
    private Boolean isDisconnectedForDebt;
    private LatestFailureDTO latestOpenFailure;

    public SupplyDTO() {
    }

    public SupplyDTO(String no, String prefecture, Integer lastTenant, String lastTenantName, Boolean isDisconnectedForDebt, LatestFailureDTO latestOpenFailure) {
        this.no = no;
        this.prefecture = prefecture;
        this.lastTenant = lastTenant;
        this.lastTenantName = lastTenantName;
        this.isDisconnectedForDebt = isDisconnectedForDebt;
        this.latestOpenFailure = latestOpenFailure;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public Integer getLastTenant() {
        return lastTenant;
    }

    public void setLastTenant(Integer lastTenant) {
        this.lastTenant = lastTenant;
    }

    public String getLastTenantName() {
        return lastTenantName;
    }

    public void setLastTenantName(String lastTenantName) {
        this.lastTenantName = lastTenantName;
    }

    public Boolean getDisconnectedForDebt() {
        return isDisconnectedForDebt;
    }

    public void setDisconnectedForDebt(Boolean disconnectedForDebt) {
        isDisconnectedForDebt = disconnectedForDebt;
    }

    public LatestFailureDTO getLatestOpenFailure() {
        return latestOpenFailure;
    }

    public void setLatestOpenFailure(LatestFailureDTO latestOpenFailure) {
        this.latestOpenFailure = latestOpenFailure;
    }
}
