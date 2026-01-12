package gr.deddie.pfr.dao;

import gr.deddie.pfr.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

import javax.ws.rs.DELETE;
import java.util.Date;
import java.util.List;

/**
 * Created by M.Masikos on 4/11/2016.
 */
public interface DATASECURITYMapper {

    //--------------set loggedin user----------------------------------
    @Select("call SEC_POLICY.set_ctx_user(#{0})")
    public void setAppUserToken(String token);

    //--------------send sms----------------------------------
    @Select("select appstd.SEND_SMS(#{cell},#{text}) from dual")
    public String sendSMS(@Param("cell") String cell, @Param("text") String text);

    //--------------persist otp----------------------------------
    @Insert("INSERT INTO PFR_OTP(CELL, PIN, TYPE) VALUES(#{cell},#{pin},#{type.id})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    public int addOtp(Otp otp);

    //--------------get otp----------------------------------
    @Select("SELECT * FROM PFR_OTP WHERE ID=#{id}")
    @Results(id = "Otp", value = {
            @Result(property="id", column="ID"),
            @Result(property="cell", column="CELl"),
            @Result(property="pin", column="PIN"),
            @Result(property="type", column="type", typeHandler = EnumOrdinalTypeHandler.class),
            @Result(property="created", column="CREATED"),
            @Result(property="used", column="USED")
    })
    public Otp getOtp(Otp otp);

    //--------------get any active otp----------------------------------
    @Select("SELECT * FROM PFR_OTP WHERE CELL=#{otp.cell} AND TYPE = #{otp.type.id} AND USED = 0 AND CURRENT_TIMESTAMP-created <= NUMTODSINTERVAL(#{validityPeriod}, 'SECOND')")
    @ResultMap("Otp")
    public Otp getAnyActiveOtp(@Param("otp") Otp otp, @Param("validityPeriod") Integer validityPeriod);

    //--------------check if specific otp is still active----------------------------------
    @Select("SELECT * FROM PFR_OTP WHERE CELL=#{otp.cell} AND TYPE = #{otp.type.id} AND PIN=#{otp.pin} AND USED = 0 AND CURRENT_TIMESTAMP-created <= NUMTODSINTERVAL(#{validityPeriod}, 'SECOND')")
    @ResultMap("Otp")
    public Otp getOtpOnlyIfActive(@Param("otp") Otp otp, @Param("validityPeriod") Integer validityPeriod);

    //--------------invalidate otp-------------------------------
    @Update("UPDATE PFR_OTP SET USED=1 WHERE ID=#{id} AND USED=0")
    public int invalidateOtp(Otp otp);

    //---------------------get User Data Roles-------------------------
    @Select("select id, name, descr from sec_data_roles")
    @Results({
            @Result(property = "id", column="ID"),
            @Result(property="name", column="NAME"),
            @Result(property = "descr", column = "DESCR")
    })
    public List<UserDataRole> getDataRoles ();

    //--------------get user data role with items----------------------------------
    public List<UserDataRoleWithItems> getUserDataRolesWithItems();

    //--------------get data role items----------------------------------
    public List<DataRoleItem> getDataRoleItems(long data_role_id);

    //--------------add data role item-------------------------------
    @Insert("INSERT INTO SEC_DATA_ROLES_LN(ITEM,DESCR,DATA_ROLE_ID) VALUES(#{item},#{descr},#{data_role_id})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    public int addDataRoleItem(DataRoleItem dataRoleItem);

    //--------------delete data role item-------------------------------
    @Delete("DELETE FROM SEC_DATA_ROLES_LN WHERE ID=#{id}")
    public int deleteDataRoleItem(DataRoleItem dataRoleItem);

    //--------------update data role item-------------------------------
    @Update("UPDATE SEC_DATA_ROLES_LN SET ITEM=#{item}, DESCR=#{descr} WHERE ID=#{id}")
    public int updateDataRoleItem(DataRoleItem dataRoleItem);

    //--------------add data role item-------------------------------
    @Insert("INSERT INTO SEC_DATA_ROLES(NAME,DESCR) VALUES(#{name},#{descr})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    public int addDataRole(UserDataRole userDataRole);

    //--------------update data role-------------------------------
    @Update("UPDATE SEC_DATA_ROLES SET NAME=#{name}, DESCR=#{descr} WHERE ID=#{id}")
    public int updateDataRole(UserDataRole userDataRole);

    //--------------Delete data role-------------------------------
    @Delete("DELETE FROM SEC_DATA_ROLES WHERE ID=#{id}")
    public int deleteDataRole(UserDataRole userDataRole);

    //------Delete items related to specific data role-------------
    @Delete("DELETE FROM SEC_DATA_ROLES_LN WHERE DATA_ROLE_ID=#{id}")
    public int deleteDataRoleItems(UserDataRole userDataRole);


}
