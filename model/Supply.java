package gr.deddie.pfr.model;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Created by M.Masikos on 24/4/2017.
 */
public class Supply {

    public static final Integer SUPPLY_NO_LENGTH_A = 8;
    public static final Integer SUPPLY_NO_LENGTH_B = 11;

    private String no;
    private String prefecture;
    private Integer lastTenant;
    private String lastTenantName;
    private String area;
    private String address;
    private String postal_code;
    private Boolean isDisconnectedForDebt;
    private Grafeio grafeio;
    private Point2D.Double point;
    private String acct_muncp_code;
    private KallikratikosOTA klot;

    public Supply() {
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

    public Boolean getDisconnectedForDebt() {
        return isDisconnectedForDebt;
    }

    public void setDisconnectedForDebt(Boolean disconnectedForDebt) {
        isDisconnectedForDebt = disconnectedForDebt;
    }

    public Grafeio getGrafeio() {
        return grafeio;
    }

    public void setGrafeio(Grafeio grafeio) {
        this.grafeio = grafeio;
    }

    public Point2D.Double getPoint() {
        return point;
    }

    public void setPoint(Point2D.Double point) {
        this.point = point;
    }

    public String getAcct_muncp_code() {
        return acct_muncp_code;
    }

    public void setAcct_muncp_code(String acct_muncp_code) {
        this.acct_muncp_code = acct_muncp_code;
    }

    public KallikratikosOTA getKlot() {
        return klot;
    }

    public void setKlot(KallikratikosOTA klot) {
        this.klot = klot;
    }
}
