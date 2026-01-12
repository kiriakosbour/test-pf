package gr.deddie.pfr.dao;

import gr.deddie.pfr.model.AssignmentDTO;
import gr.deddie.pfr.model.UserInfoDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by M.Masikos on 24/10/2016.
 */
public interface USERSMapper {

    //--------------insert assignment-------------------------------
    @Insert("INSERT INTO H_MONA_SEC_USERS(MONA,SEC_USER_ID,APP_ID) VALUES(#{monada},#{user_id},#{app_id})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    public int insertAssignment(AssignmentDTO assignmentDTO);

    //--------------update assignment-------------------------------
    @Update("UPDATE H_MONA_SEC_USERS SET MONA=#{monada} WHERE ID=#{id}")
    public int updateAssignment(AssignmentDTO assignmentDTO);

    //--------------get assignments----------------------------------
    @Select("SELECT ID, MONA, SEC_USER_ID FROM H_MONA_SEC_USERS WHERE APP_ID=#{app_id}")
    @Results({
            @Result(property="id", column="ID"),
            @Result(property="monada", column="MONA"),
            @Result(property="user_id", column="SEC_USER_ID")
    })
    public List<AssignmentDTO> getAssignments(Integer app_id);

    //--------------get user info by username----------------------------------
    public UserInfoDTO getUserInfoByUsername(String username);

    //--------------get user info by AM----------------------------------
    public UserInfoDTO getUserInfoByAM(String am);

    //--------------delink user data role-------------------------------
    @Update("UPDATE SEC_USERS_DATA_ROLES SET DELINK_DATE=SYSDATE WHERE USR_ID=#{user_id} AND DATA_ROLE_ID=#{data_role_id} AND DELINK_DATE IS NULL")
    public int delinkUserDataRole(@Param("user_id") long user_id, @Param("data_role_id") long data_role_id);

    //--------------link user data role-------------------------------
    @Update("INSERT INTO SEC_USERS_DATA_ROLES(USR_ID,DATA_ROLE_ID) VALUES(#{user_id},#{data_role_id})")
    public int linkUserDataRole(@Param("user_id") long user_id, @Param("data_role_id") long data_role_id);

}
