package gr.deddie.pfr.dao;

import gr.deddie.pfr.model.KallikratikosOTA;
import gr.deddie.pfr.model.Supply;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * Created by M.Masikos on 24/4/2017.
 */
public interface ERMINFOMapper {

    //--------------get supply by no (8 digits)--------------------------------------
    @Select("SELECT ACCT_FOLIO as NO, ACCT_COMPANY || ACCT_OFFICE as GRAF, CITY_NAME, STR_NAME || ' ' || ACCT_STR_NBR as ADDRESS, ZIP_CODE, ACCT_COMPANY, CSC_LAST_TENANT_NR, ACCT_WGS84_X, ACCT_WGS84_Y, ACCT_MUNCP_CODE FROM ERMH_CSCACCT_ALL WHERE ACCT_FOLIO = #{0}")
    @Results({
            @Result(property="no", column="NO"),
            @Result(property="area", column="CITY_NAME"),
            @Result(property="address", column="ADDRESS"),
            @Result(property="postal_code", column="ZIP_CODE"),
            @Result(property="prefecture", column="ACCT_COMPANY"),
            @Result(property="lastTenant", column="CSC_LAST_TENANT_NR"),
            @Result(property="grafeio.graf", column="GRAF"),
            @Result(property="point.x", column="ACCT_WGS84_X"),
            @Result(property="point.y", column="ACCT_WGS84_Y"),
            @Result(property="acct_muncp_code", column="ACCT_MUNCP_CODE")
    })
    public Supply getSupply(String folio);

    //--------------get supply by no (8 digits)--------------------------------------
    @Select("SELECT CUST_NAME FROM ERMH_CSCUSTM_ALL WHERE CSC_CUST_CALC_KEY = #{0}")
    public String getSupplysLastTenantName(String supply_no_full);
}
