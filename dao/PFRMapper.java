/**
 * Created by M.Masikos on 21/6/2016.
   Contributors: Kleopatra Konstanteli
 */

package gr.deddie.pfr.dao;

import java.util.Date;
import java.util.List;

import gr.deddie.pfr.model.*;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface PFRMapper {

	//------------------------get Project Settings------------------------------
	@Select("select * from pfr_settings")
	public Settings getSettings ();

	//--------------delete GeneralFailureGroup-------------------------------
	@Update("UPDATE PFR_FAILURES SET GENERAL_FAILURE_GROUP=null WHERE ID=#{id}")
	public int deleteGeneralFailureGroup(Long failure_id);

	//--------------delete failure type-------------------------------
	@Update("UPDATE PFR_FAILURE_TYPE SET DELETED=1 WHERE ID=#{id}")
	public int deleteFailureType(FailureType ft);
			
	//--------------delete message-----------------------------------
	@Update("UPDATE PFR_MESSAGE SET DELETED=1 WHERE ID=#{message_id}")
	public int deleteMessage(Message m);
	
	//--------------update failure type-------------------------------
	@Update("UPDATE PFR_FAILURE_TYPE SET DESCRIPTION=#{description} WHERE ID=#{id}")
	public int updateFailureType(FailureType ft);

	//--------------update failure-------------------------------
	@Update("UPDATE PFR_FAILURES SET ASSIGNED_TEXNITHS=#{assigned_texniths.mhtroo},ASSIGNED_TEXNITHS_CONTRACTOR=#{assigned_texniths_contractor.mhtroo}, ESTIMATED_RESTORE_TIME=#{estimated_restore_time}, " +
			"FINAL_RESTORATION_TIME=#{final_restoration_time}, TEXNITHS_ASSIGNMENT_TIMESTAMP=#{texniths_assignment_timestamp} WHERE ID=#{id}")
	public int updateFailure(Failure f);

	//--------------update failure status------------------------
	@Update("UPDATE PFR_FAILURES SET STATUS=#{status} WHERE ID=#{id}")
	public int updateFailureStatus(Failure f);

	//--------------update failure status------------------------
	@Update("UPDATE PFR_FAILURES SET STATUS=#{status} WHERE ID=#{failure_id}")
	public int updateFailureStatus2(FailureHistory fh);

	//--------------update failure assignment------------------------
	@Update("UPDATE PFR_FAILURES SET GRAF=#{assignee.graf} WHERE ID=#{failure_id}")
	public int updateFailureAssignment(FailureAssignment assignment);

	//--------------update message-----------------------------------
	@Update("UPDATE PFR_MESSAGE SET HEADER=#{message_header}, DESCRIPTION=#{message_description} WHERE ID=#{message_id}")
	public int updateMessage(Message m);

	//--------------insert sab MT-------------------------------
	@Insert("INSERT INTO SAVINFO(PFR_ID,VLTYPE,A,B,C,O,VLKIND,DTCREATED,VLVAL,NOTES,LAST_UPD_USR,SYNE,SYNE2,DEPM,STAT,ENDDT,INITSTAT,YS,DIAKL,REASON,KVA,MSPL,NETYPE,TVL) " +
			"VALUES(#{pfr_id}, #{vltype}, #{phase_a}, #{phase_b}, #{phase_c}, #{phase_o}, #{vlkind}, sysdate, #{vlval}, #{notes}, #{last_upd_usr}, #{syne}, #{syne2}, 'ΤΕ', #{stat}, " +
			"sysdate, #{initstat}, #{ys}, #{diakl}, #{reason}, #{kva}, #{mspl}, #{netype}, #{tvl})")
	public int insertSABMT(SabMT sabMT);

	//--------------insert sab XT-------------------------------
	@Insert("INSERT INTO SAVINFO(PFR_ID,VLTYPE,A,B,C,O,VLKIND,DTCREATED,VLVAL,NOTES,LAST_UPD_USR,SYNE,SYNE2,DEPM,STAT,ENDDT,INITSTAT,ANAX,CITY,YS,REASON,MSPL,VRAX,TVL,SYNESTR,NETYPE) " +
			"VALUES(#{pfr_id}, #{vltype}, #{phase_a}, #{phase_b}, #{phase_c}, #{phase_o}, #{vlkind}, sysdate, #{vlval}, #{notes}, #{last_upd_usr}, #{syne}, #{syne2}, 'ΤΕ', #{stat}, " +
			"sysdate, #{initstat}, #{anax}, #{city}, #{ys}, #{reason}, #{mspl}, #{vrax}, #{tvl}, #{synestr}, #{netype})")
	public int insertSABXT(SabXT sabXT);

	//--------------insert sab Paroxis-------------------------------
	@Insert("INSERT INTO SAVINFO(PFR_ID,VLTYPE,SUPPLY,VLKIND,DTCREATED,VLVAL,NOTES,LAST_UPD_USR,SYNE,SYNE2,DEPM,METER,ENDE,ENDN,NEWMETER,NEWENDE,NEWENDN,SFRAKB,SFRAKROD," +
			"SFRAD,STAT,ISCHARGE,ISRK,RKSHME,ENDDT,INITSTAT,CITY,SFRAKBOLD,SFRAKRODOLD,REASON,VRAX,TVL,SYNESTR,NETYPE,SFRAM) " +
			"VALUES(#{pfr_id}, #{vltype}, #{supply}, #{vlkind}, sysdate, #{vlval}, #{notes}, #{last_upd_usr}, #{syne}, #{syne2}, 'ΤΕ', #{meter}, " +
			"#{ende}, #{endn}, #{newmeter}, #{newende}, #{newendn}, #{sfrakb}, #{sfrakrod}, #{sfrad}, #{stat}, #{ischarge}, #{isrk}, #{rkshme}, sysdate, " +
			"#{initstat}, #{city}, #{sfrakbold}, #{sfrakrodold}, #{reason}, #{vrax}, #{tvl}, #{synestr}, #{netype}, #{sfram})")
	public int insertSABParoxis(SabParoxis sabParoxis);

	//--------------insert sab Diafora-------------------------------
	@Insert("INSERT INTO SAVINFO(PFR_ID,VLTYPE,SUPPLY,VLKIND,DTCREATED,VLVAL,NOTES,LAST_UPD_USR,SYNE,SYNE2,DEPM,STAT,ENDDT,INITSTAT,CITY,REASON,TVL) " +
			"VALUES(#{pfr_id}, #{vltype}, #{supply}, #{vlkind}, sysdate, #{vlval}, #{notes}, #{last_upd_usr}, #{syne}, #{syne2}, 'ΤΕ', #{stat}, " +
			"sysdate, #{initstat}, #{city}, #{reason}, #{tvl})")
	public int insertSABDiafora(SabDiafora sabDiafora);

	//--------------insert failure type-------------------------------
	@Insert("INSERT INTO PFR_FAILURE_TYPE(DESCRIPTION) VALUES(#{description})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertFailureType(FailureType ft);
	
	//--------------insert message-----------------------------------
	@Insert("INSERT INTO PFR_MESSAGE(HEADER, DESCRIPTION) VALUES(#{message_header}, #{message_description})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertMessage(Message m);

	//--------------insert general failure group-----------------------------------
	@Insert("INSERT INTO PFR_GENERAL_FAILURE_GROUPS(LABEL, CREATOR) VALUES(#{label}, #{creator})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int addGeneralFailureGroup(GeneralFailureGroup generalFailureGroup);

	//--------------insert failure------------------------------------
	@Insert("INSERT INTO PFR_FAILURES(ADDRESS, NAME, POSTAL_CODE, FAILURE_TYPE_ID, COMMENTS, PHONE, CELL, EMAIL, SUPPLY_NO, ESTIMATED_RESTORE_TIME, AREA, CREATED, CREATOR, REAL_ESTATE_USAGE, DANGEROUS, IS_GENERAL_FAILURE, IS_USER_OF_SUPPLY, INPUT_CHANNEL, STATUS, GRAF, KLOT) " +
			"VALUES(#{address}, #{name}, #{postal_code}, #{failureType.id}, #{comments}, #{phone}, #{cell}, #{email}, #{supply_no}, #{estimated_restore_time}, #{area}, #{created}, #{creator}, #{real_estate_usage}, #{dangerous}, #{is_general_failure}, #{is_user_of_supply}, " +
			"#{input_channel}, #{status}, #{grafeio.graf}, #{klot.klot})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertFailure(Failure f);
	
	//--------------insert failure history----------------------------
	@Insert("INSERT INTO PFR_FAILURE_HISTORY(FAILURE_ID, MESSAGE_ID, CONTACT_METHOD, STATUS, COMMENTS, CREATOR) VALUES(#{failure_id}, #{message_id}, #{contact_method}, #{status}, #{comments}, #{creator})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	//@Insert("INSERT INTO PFR_FAILURE_HISTORY(FAILURE_ID, MESSAGE_ID, CONTACT_METHOD, STATUS) VALUES(#{failure_id}, #{message_id}, #{contact_method}, #{status})")
	//@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertFailureHistory(FailureHistory fh);

	//--------------insert failure assignment----------------------------
	@Insert("INSERT INTO PFR_FAILURE_ASSIGNMENTS(FAILURE_ID, GRAF, CREATOR) VALUES(#{failure_id}, #{assignee.graf}, #{assignor})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertFailureAssignment(FailureAssignment failureAssignment);

	//--------------insert landmark point------------------------------
	@Insert("INSERT INTO PFR_LANDMARK_POINTS(LONGITUDE, LATITUDE, LANDMARK_ID) VALUES(#{lon}, #{lat}, #{landmark_id})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertLandmarkPoint(Point point);

	//--------------insert failure landmarks------------------------------
	@Insert("INSERT INTO PFR_FAILURE_LANDMARKS (TYPE, FAILURE_ID) VALUES(#{type}, #{failure_id})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertFailureLandmark(Landmark landmark);

	//--------------insert failure questions------------------------------
	@Insert("INSERT INTO PFR_FAILURE_QUESTIONS (FAILURE_ID, QUESTION_ID, ANSWER) VALUES(#{failure_id}, #{id}, #{answer})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertFailureQuestion(Question question);

	//--------------all messages--------------------------------------
    @Select("SELECT * FROM PFR_MESSAGE WHERE DELETED=0")
    @Results({
            @Result(property="message_id", column="ID"),
            @Result(property="message_description", column="DESCRIPTION"),
            @Result(property="message_header", column="HEADER")
    })
    public List<Message> getMessages();

	//--------------get message by id--------------------------------------
	@Select("SELECT * FROM PFR_MESSAGE WHERE ID = #{0}")
	@Results({
			@Result(property="message_id", column="ID"),
			@Result(property="message_description", column="DESCRIPTION"),
			@Result(property="message_header", column="HEADER")
	})
	public Message getMessage(Integer id);

	@Select("SELECT * FROM SAVINFOKINDS WHERE CODE = #{0}")
	public SabInfoType getSabInfoType(String code);

	//--------------get general failure group by id--------------------------------------
	@Select("SELECT * FROM PFR_GENERAL_FAILURE_GROUPS WHERE ID = #{0}")
	public GeneralFailureGroup getGeneralFailureGroupbyId(Long id);

	//--------------get general failure group by label--------------------------------------
	@Select("SELECT * FROM PFR_GENERAL_FAILURE_GROUPS WHERE label = #{0}")
	public GeneralFailureGroup getGeneralFailureGroupbyLabel(String label);

	//--------------get grafeio by id--------------------------------------
	@Select("SELECT * FROM H_GRAF WHERE GRAF = #{0}")
	public Grafeio getGrafeio(String graf);

	//--------------get generalFailureGroup by id--------------------------------------
	@Select("SELECT * FROM PFR_GENERAL_FAILURE_GROUPS WHERE ID = #{0}")
	public GeneralFailureGroup getGeneralFailureGroup(Long id);

	//--------------get misthotos by mhtroo--------------------------------------
	@Select("SELECT * FROM H_TEXNITES WHERE MATR = #{0}")
	@Results({
			@Result(property="mhtroo", column="MATR"),
			@Result(property="lastname", column="EPON"),
			@Result(property="firstname", column="ONKY"),
			@Result(property="fathersname", column="ONPA"),
			@Result(property="kathgoria", column="KATH"),
			@Result(property="eidikothta", column="EIDI"),
			@Result(property="misthodotiki_omada", column="MIOM")
	})
	public Misthotos getTexnitis(Long mhtroo);

	//--------------get contractor misthotos by mhtroo--------------------------------------
	@Select("select distinct(us.usr) as mhtroo, us.full_name as full_name, c.name as contractor_name from h_contractors c " +
			"join h_contractor_user_links a on a.contractor_id = c.id and a.delink_date is null and " +
					"(c.ergodiktyou ='1' or c.code in ('11')) " +
					"join h_contractor_mona_user_links u on u.con_user_id = a.id and u.delink_date is null " +
					"join h_contractor_mona_links m on m.contractor_id = c.id and m.delink_date is null and m.mona = u.mona " +
					"join sec_users us on us.id = a.sec_user_id WHERE rownum=1 and us.usr = #{0}")
	@ResultMap("contractormisthotos")
	public ContractorMisthotos getContractorTexnitis(String mhtroo);

	//-------------------get grafeia--------------------------------------
	@Select("SELECT * FROM H_GRAF")
	public List<Grafeio> getGrafeia();

	//-------------------get grafeia--------------------------------------
	@Select("SELECT * FROM H_GRAF where MO = #{0}")
	public List<Grafeio> getGrafeiaperMIOM(String misthodotiki_omada);

	//--------------all failure DTOs using filters (if any)-------------------
    public List<FailureDTO> getFailureDTOsByCriteriaByVPD(@Param("from") Date createdFrom, @Param("to") Date createdTo,
														  @Param("failure_type_id") Integer failure_type_id,
														  @Param("failure_extent") Boolean failure_extent,
														  @Param("status") String status,
														  @Param("statusLastUpdatedFrom") Date statusLastUpdatedFrom,
														  @Param("list_max_size") Long list_max_size,
														  @Param("postal_code") String postal_code,
														  @Param("grafeio") String grafeio);

	//--------------all active failure DTOs using filters (if any)-------------------
	public List<FailureDTO> getActiveFailureDTOsByCriteriaByVPD(@Param("from") Date from, @Param("to") Date to,
														  		@Param("failure_type_id") Integer failure_type_id,
																@Param("is_general_failure") Boolean isGeneralFailure,
																@Param("general_failure_group") Failure.GeneralFailureGroup generalFailureGroup,
														  		@Param("list_max_size") Long list_max_size, @Param("postal_code") String postal_code,
																@Param("failure_status_announced") String failure_status_announced,
																@Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------all active failure DTOs using filters (if any)-------------------
	public List<FailureDTO> getActiveFailureDTOsByCriteria(@Param("from") Date from, @Param("to") Date to,
														   @Param("responsible_grafeio") String responsible_grafeio,
														   @Param("failure_type_id") Integer failure_type_id,
														   @Param("is_general_failure") Boolean isGeneralFailure,
														   @Param("general_failure_group_id") Long generalFailureGroupId,
														   @Param("list_max_size") Long list_max_size, @Param("postal_code") String postal_code,
														   @Param("failure_status_announced") String failure_status_announced,
														   @Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------groups of active failures per vpd-------------------
	@Select("SELECT * FROM PFR_GENERAL_FAILURE_GROUPS WHERE ID IN " +
			"(SELECT F.GENERAL_FAILURE_GROUP_ID FROM PFR_FAILURES F " +
			"WHERE ((F.STATUS = #{failure_status_announced}) OR (F.STATUS = #{failure_status_in_progress})) AND (F.GRAF IN (SELECT GRAF FROM H_GRAF G)) AND F.GENERAL_FAILURE_GROUP_ID IS NOT NULL) " +
			"order by ID asc")
	public List<GeneralFailureGroup> getActiveGeneralFailuresGroupsByGraf(@Param("failure_status_announced") String failure_status_announced,
																		  @Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------all group failure DTOs by GroupID-------------------
	@Select(
			"SELECT F.*, D.ID AS FAILURE_ASSIGNMENT_ID,  D.FAILURE_ID AS FAILURE_ASSIGNMENT_FAILURE_ID, D.CREATOR AS FAILURE_ASSIGNMENT_ASSIGNOR, " +
				"D.CREATED AS FAILURE_ASSIGNMENT_CREATED, D.GRAF AS FAILURE_ASSIGNMENT_GRAF, B.CREATED AS LAST_UPDATED, B.STATUS AS STAT, B.COMMENTS AS LAST_UPDATE_COMMENTS, " +
				"C.CREATED AS ANNOUNCED, T.DESCRIPTION FROM PFR_FAILURES F JOIN " +
				"( " +
				"SELECT t1.* " +
				"FROM PFR_FAILURE_HISTORY t1 " +
				"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
				"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED < t2.CREATED) " +
				"WHERE t2.ID IS NULL " +
				") B " +
				"ON F.ID = B.FAILURE_ID " +
				"JOIN " +
				"( " +
				"SELECT t1.* " +
				"FROM PFR_FAILURE_HISTORY t1 " +
				"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
				"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED > t2.CREATED) " +
				"WHERE t2.ID IS NULL " +
				") C " +
				"ON F.ID = C.FAILURE_ID " +
				"JOIN " +
				"( " +
				"SELECT fa1.* " +
				"FROM PFR_FAILURE_ASSIGNMENTS fa1 " +
				"LEFT OUTER JOIN PFR_FAILURE_ASSIGNMENTS fa2 " +
				"ON (fa1.FAILURE_ID = fa2.FAILURE_ID AND fa1.CREATED < fa2.CREATED) " +
				"WHERE fa2.ID IS NULL " +
				") D " +
				"ON F.ID = D.FAILURE_ID " +
				"JOIN PFR_FAILURE_TYPE T ON F.FAILURE_TYPE_ID=T.ID " +
				"WHERE F.GENERAL_FAILURE_GROUP_ID = #{groupId} AND ((F.STATUS = 'ANNOUNCED') OR (F.STATUS = 'IN_PROGRESS'))"
		)
	@ResultMap("failureDTO")
	public List<FailureDTO> getActiveGeneralFailuresByGroupID(@Param("groupId") Long groupId);

	//--------------all failure DTOs by IDs-------------------
    @Select({"<script>",
    		"SELECT F.*, B.CREATED AS LAST_UPDATED, B.STATUS AS STAT, C.CREATED AS CREAT, T.DESCRIPTION FROM PFR_FAILURES F JOIN", 
    		"(",
    			"SELECT t1.*",
    			"FROM PFR_FAILURE_HISTORY t1", 
    			"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2",
    			"ON ((t1.FAILURE_ID = t2.FAILURE_ID) AND (t2.CREATED > t1.CREATED))",
    			"WHERE t2.ID IS NULL", 
    		") B",
    		"ON F.ID = B.FAILURE_ID",
    		"JOIN",
    		"(",
    			"SELECT t1.*",
    			"FROM PFR_FAILURE_HISTORY t1 ",
    			"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2",
    			"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED > t2.CREATED)",
    		    "WHERE t2.ID IS NULL",
    		") C",
    		"ON F.ID = C.FAILURE_ID",
    		"JOIN PFR_FAILURE_TYPE T ON F.FAILURE_TYPE_ID=T.ID",
    		"WHERE F.ID IN",
    			"<foreach item='item' index='index' collection='list'",
    				"open='(' separator=',' close=')'>",
    				"#{item}",
    			"</foreach>",
    		"</script>"})
    @ResultMap("failureDTO")
    public List<FailureDTO> getFailureDTOsByIDs(@Param("list") Long[] ids);
    
  //--------------all failure DTOs using upper limit (2000 records)------------
    @Select(
    		"SELECT F.*, B.CREATED AS LAST_UPDATED, B.STATUS AS STAT, C.CREATED AS CREAT, T.DESCRIPTION FROM PFR_FAILURES F JOIN " + 
    		"( " +
    			"SELECT t1.* " +
    			"FROM PFR_FAILURE_HISTORY t1 " + 
    			"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
    			"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED < t2.CREATED) " +
    			"WHERE t2.ID IS NULL " +
    		") B " +
    		"ON F.ID = B.FAILURE_ID " +
    		"JOIN " +
    		"( " +
    			"SELECT t1.* " +
    			"FROM PFR_FAILURE_HISTORY t1 " +
    			"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
    		    "ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED > t2.CREATED) " +
    		    "WHERE t2.ID IS NULL " + 
    		") C " +
    		"ON F.ID = C.FAILURE_ID " +
    		"JOIN PFR_FAILURE_TYPE T ON F.FAILURE_TYPE_ID=T.ID " +
    		"WHERE ROWNUM <= 2001 " + 
    		"ORDER BY C.CREATED DESC"
    )
    @Results({
    	 @Result(property="id", column="ID"),
         @Result(property="address", column="ADDRESS"),
         @Result(property="name", column="NAME"),
         @Result(property="postal_code", column="POSTAL_CODE"),
         @Result(property="failure_type_id", column="FAILURE_TYPE_ID"),
         @Result(property="failure_type_description", column="DESCRIPTION"),
         @Result(property="comments", column="COMMENTS"),          
         @Result(property="phone", column="PHONE"),
         @Result(property="cell", column="CELL"),
         @Result(property="email", column="EMAIL"),            
         @Result(property="created", column="CREAT"),
         @Result(property="status", column="STAT"),
         @Result(property="last_updated", column="LAST_UPDATED")
    })
    public List<FailureDTO> getFailureDTOsLimited();

	//--------------all failure DTOs-------------------
    @Select(
    		"SELECT F.*, B.CREATED AS LAST_UPDATED, B.STATUS AS STAT, C.CREATED AS CREAT, T.DESCRIPTION FROM PFR_FAILURES F JOIN " + 
    		"( " +
    			"SELECT t1.* " +
    			"FROM PFR_FAILURE_HISTORY t1 " + 
    			"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
    			"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED < t2.CREATED) " +
    			"WHERE t2.ID IS NULL " +
    		") B " +
    		"ON F.ID = B.FAILURE_ID " +
    		"JOIN " +
    		"( " +
    			"SELECT t1.* " +
    			"FROM PFR_FAILURE_HISTORY t1 " +
    			"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
    		    "ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED > t2.CREATED) " +
    		    "WHERE t2.ID IS NULL " + 
    		") C " +
    		"ON F.ID = C.FAILURE_ID " +
    		"JOIN PFR_FAILURE_TYPE T ON F.FAILURE_TYPE_ID=T.ID "
    )
    @Results({
    	 @Result(property="id", column="ID"),
         @Result(property="address", column="ADDRESS"),
         @Result(property="name", column="NAME"),
         @Result(property="postal_code", column="POSTAL_CODE"),
         @Result(property="failure_type_id", column="FAILURE_TYPE_ID"),
         @Result(property="failure_type_description", column="DESCRIPTION"),
         @Result(property="comments", column="COMMENTS"),
         @Result(property="phone", column="PHONE"),
         @Result(property="cell", column="CELL"),
         @Result(property="email", column="EMAIL"),
         @Result(property="created", column="CREAT"),
         @Result(property="status", column="STAT"),
         @Result(property="last_updated", column="LAST_UPDATED")
    })
    public List<FailureDTO> getFailureDTOs();

	//--------------all active failure DTOs-------------------
	@Select(
			"select f.*, a.ID AS FAILURE_ASSIGNMENT_ID,  a.FAILURE_ID AS FAILURE_ASSIGNMENT_FAILURE_ID, a.CREATOR AS FAILURE_ASSIGNMENT_ASSIGNOR, " +
				"a.CREATED AS FAILURE_ASSIGNMENT_CREATED, a.GRAF AS FAILURE_ASSIGNMENT_GRAF, d.CREATED AS LAST_UPDATED, d.STATUS AS STAT, d.COMMENTS AS LAST_UPDATE_COMMENTS, " +
				"e.CREATED AS ANNOUNCED, T.DESCRIPTION " +
				" from PFR_FAILURE_ASSIGNMENTS a, h_graf o,PFR_FAILURE_HISTORY d, PFR_FAILURE_HISTORY e, PFR_FAILURES f, PFR_FAILURE_TYPE T " +
				"where exists (select max(id) from PFR_FAILURE_ASSIGNMENTS b where a.failure_id=b.failure_id having max(id)=a.id) " +
				"and exists (select max(id) from PFR_FAILURE_HISTORY b where a.failure_id=b.failure_id having max(id)=d.id) " +
				"and exists (select min(id) from PFR_FAILURE_HISTORY b where a.failure_id=b.failure_id having min(id)=e.id) " +
				"and a.graf=o.graf " +
				"and d.failure_id=a.failure_id " +
				"and (f.STATUS = #{failure_status_announced} OR f.STATUS = #{failure_status_in_progress}) " +
				"and e.failure_id=a.failure_id " +
				"and f.id=a.failure_id " +
				"and f.FAILURE_TYPE_ID in (26,27,28) " +
				"AND f.GENERAL_FAILURE_GROUP_ID IS NULL " +
				"and f.FAILURE_TYPE_ID=T.ID "
	)
	@Results(id = "failureDTO", value = {
			@Result(property="id", column="ID"),
			@Result(property="area", column="AREA"),
			@Result(property="address", column="ADDRESS"),
			@Result(property="name", column="NAME"),
			@Result(property="postal_code", column="POSTAL_CODE"),
			@Result(property="failureType", column="FAILURE_TYPE_ID", javaType=FailureType.class, one=@One(select="getFailureType")),
			@Result(property="failureAssignment.id", column="FAILURE_ASSIGNMENT_ID"),
			@Result(property="failureAssignment.failure_id", column="FAILURE_ASSIGNMENT_FAILURE_ID"),
			@Result(property="failureAssignment.created", column="FAILURE_ASSIGNMENT_CREATED"),
			@Result(property="failureAssignment.assignor", column="FAILURE_ASSIGNMENT_ASSIGNOR"),
			@Result(property="failureAssignment.assignee", column="FAILURE_ASSIGNMENT_GRAF", javaType=Grafeio.class, one=@One(select="getGrafeio")),
			@Result(property="general_failure_group", column="GENERAL_FAILURE_GROUP_ID", javaType=GeneralFailureGroup.class, one=@One(select="getGeneralFailureGroup")),
			@Result(property="comments", column="COMMENTS"),
			@Result(property="phone", column="PHONE"),
			@Result(property="cell", column="CELL"),
			@Result(property="email", column="EMAIL"),
			@Result(property="supply_no", column="SUPPLY_NO"),
			@Result(property="real_estate_usage", column="REAL_ESTATE_USAGE"),
			@Result(property="creator", column="CREATOR"),
			@Result(property="created", column="CREATED"),
			@Result(property="announced", column="ANNOUNCED"),
			@Result(property="status", column="STAT"),
			@Result(property="last_update_comments", column="LAST_UPDATE_COMMENTS"),
			@Result(property="assigned_texniths", column="ASSIGNED_TEXNITHS", javaType=Misthotos.class, one=@One(select="getTexnitis")),
			@Result(property="assigned_texniths_contractor", column="ASSIGNED_TEXNITHS_CONTRACTOR", javaType=ContractorMisthotos.class, one=@One(select="getContractorTexnitis")),
			@Result(property="estimated_restore_time", column="ESTIMATED_RESTORE_TIME"),
			@Result(property="last_updated", column="LAST_UPDATED"),
			@Result(property="dangerous", column="DANGEROUS"),
			@Result(property="klot", column="KLOT", javaType=KallikratikosOTA.class, one=@One(select="getKLOTByID"))
	})
	public List<FailureDTO> getActiveRestFailuresDTOsByGraf(@Param("dangerous_state_id") Integer dangerous_state_id, @Param("failure_status_announced") String failure_status_announced, @Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------active dangerous state failure DTOs by GRAF-------------------
	@Select(
			"SELECT F.*, D.ID AS FAILURE_ASSIGNMENT_ID,  D.FAILURE_ID AS FAILURE_ASSIGNMENT_FAILURE_ID, D.CREATOR AS FAILURE_ASSIGNMENT_ASSIGNOR, " +
					"D.CREATED AS FAILURE_ASSIGNMENT_CREATED, D.GRAF AS FAILURE_ASSIGNMENT_GRAF, B.CREATED AS LAST_UPDATED, B.STATUS AS STAT, " +
					"B.COMMENTS AS LAST_UPDATE_COMMENTS, C.CREATED AS ANNOUNCED, T.DESCRIPTION FROM PFR_FAILURES F JOIN " +
					"( " +
					"SELECT t1.* " +
					"FROM PFR_FAILURE_HISTORY t1 " +
					"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
					"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED < t2.CREATED) " +
					"WHERE t2.ID IS NULL " +
					") B " +
					"ON F.ID = B.FAILURE_ID " +
					"JOIN " +
					"( " +
					"SELECT t1.* " +
					"FROM PFR_FAILURE_HISTORY t1 " +
					"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
					"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED > t2.CREATED) " +
					"WHERE t2.ID IS NULL " +
					") C " +
					"ON F.ID = C.FAILURE_ID " +
					"JOIN " +
					"( " +
					"SELECT fa1.* " +
					"FROM PFR_FAILURE_ASSIGNMENTS fa1 " +
					"LEFT OUTER JOIN PFR_FAILURE_ASSIGNMENTS fa2 " +
					"ON (fa1.FAILURE_ID = fa2.FAILURE_ID AND fa1.CREATED < fa2.CREATED) " +
					"WHERE fa2.ID IS NULL " +
					") D " +
					"ON F.ID = D.FAILURE_ID " +
					"JOIN PFR_FAILURE_TYPE T ON F.FAILURE_TYPE_ID=T.ID " +
					"WHERE F.FAILURE_TYPE_ID = #{dangerous_state_id} AND (F.STATUS = #{failure_status_announced} OR F.STATUS = #{failure_status_in_progress}) " +
					"AND F.GENERAL_FAILURE_GROUP_ID IS NULL " +
					"AND F.GRAF IN (SELECT GRAF FROM H_GRAF G)"
	)
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="area", column="AREA"),
			@Result(property="address", column="ADDRESS"),
			@Result(property="name", column="NAME"),
			@Result(property="postal_code", column="POSTAL_CODE"),
			@Result(property="failureType", column="FAILURE_TYPE_ID", javaType=FailureType.class, one=@One(select="getFailureType")),
			@Result(property="failureAssignment.id", column="FAILURE_ASSIGNMENT_ID"),
			@Result(property="failureAssignment.failure_id", column="FAILURE_ASSIGNMENT_FAILURE_ID"),
			@Result(property="failureAssignment.created", column="FAILURE_ASSIGNMENT_CREATED"),
			@Result(property="failureAssignment.assignor", column="FAILURE_ASSIGNMENT_ASSIGNOR"),
			@Result(property="failureAssignment.assignee", column="FAILURE_ASSIGNMENT_GRAF", javaType=Grafeio.class, one=@One(select="getGrafeio")),
			@Result(property="general_failure_group", column="GENERAL_FAILURE_GROUP_ID", javaType=GeneralFailureGroup.class, one=@One(select="getGeneralFailureGroup")),
			@Result(property="comments", column="COMMENTS"),
			@Result(property="phone", column="PHONE"),
			@Result(property="cell", column="CELL"),
			@Result(property="email", column="EMAIL"),
			@Result(property="supply_no", column="SUPPLY_NO"),
			@Result(property="real_estate_usage", column="REAL_ESTATE_USAGE"),
			@Result(property="creator", column="CREATOR"),
			@Result(property="created", column="CREATED"),
			@Result(property="announced", column="ANNOUNCED"),
			@Result(property="status", column="STAT"),
			@Result(property="last_update_comments", column="LAST_UPDATE_COMMENTS"),
			@Result(property="assigned_texniths", column="ASSIGNED_TEXNITHS", javaType=Misthotos.class, one=@One(select="getTexnitis")),
			@Result(property="assigned_texniths_contractor", column="ASSIGNED_TEXNITHS_CONTRACTOR", javaType=ContractorMisthotos.class, one=@One(select="getContractorTexnitis")),
			@Result(property="estimated_restore_time", column="ESTIMATED_RESTORE_TIME"),
			@Result(property="last_updated", column="LAST_UPDATED"),
			@Result(property="dangerous", column="DANGEROUS"),
			@Result(property="questions", column="ID", javaType=List.class, many=@Many(select="getFailureQuestions")),
			@Result(property="networkHazardPhotos", column="ID", javaType=List.class, many=@Many(select="getPhotosNamesperFailure")),
			@Result(property="klot", column="KLOT", javaType=KallikratikosOTA.class, one=@One(select="getKLOTByID"))
	})
	public List<DangerousStateDTO> getActiveDangerousStatesByGraf(@Param("dangerous_state_id") Integer dangerous_state_id, @Param("failure_status_announced") String failure_status_announced, @Param("failure_status_in_progress") String failure_status_in_progress);

	//-------------------------get Photo Names per failure----------------
	@Select(
			"SELECT ID, NAME FROM PFR_PHOTOS WHERE FAILURE_ID = #{0}"
	)
	public List<NetworkHazardPhoto> getPhotosNamesperFailure(long failure_id);

	//-------------------------get Photo per id----------------
	@Select(
			"SELECT * FROM PFR_PHOTOS WHERE ID = #{0}"
	)
	public NetworkHazardPhoto getPhoto(long file_id);

	//--------------active general failure DTOs by GRAF-------------------
	@Select(
			"SELECT COUNT(1) AS METRHTHS, F.POSTAL_CODE, MAX(B.CREATED) AS MAXDATE FROM PFR_FAILURES F JOIN" +
					"( " +
					"SELECT t1.* " +
					"FROM PFR_FAILURE_HISTORY t1 " +
					"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
					"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED < t2.CREATED) " +
					"WHERE t2.ID IS NULL " +
					") B " +
					"ON F.ID = B.FAILURE_ID " +
					"WHERE F.FAILURE_TYPE_ID = #{general_failure_id} AND B.STATUS <> #{failure_status_resolved}  AND F.POSTAL_CODE IN (SELECT TK FROM H_TK_GRAF TG JOIN H_GRAF G ON TG.GRAF = G.GRAF) " +
					"GROUP BY F.POSTAL_CODE"
	)
	@Results({
			@Result(property="counter", column="METRHTHS"),
			@Result(property="postal_code", column="POSTAL_CODE"),
			@Result(property="maxDate", column="MAXDATE")
	})
	public List<FailureStatsDTO> getActiveGeneralFailuresSumsByGraf(@Param("general_failure_id") Integer general_failure_id, @Param("failure_status_resolved") String failure_status_resolved);

	//--------------active general failure DTOs by GRAF-------------------
	@Select(
			"SELECT F.POSTAL_CODE FROM PFR_FAILURES F JOIN" +
					"( " +
					"SELECT t1.* " +
					"FROM PFR_FAILURE_HISTORY t1 " +
					"LEFT OUTER JOIN PFR_FAILURE_HISTORY t2 " +
					"ON (t1.FAILURE_ID = t2.FAILURE_ID AND t1.CREATED < t2.CREATED) " +
					"WHERE t2.ID IS NULL " +
					") B " +
					"ON F.ID = B.FAILURE_ID " +
					"JOIN " +
					"( " +
					"SELECT fa1.* " +
					"FROM PFR_FAILURE_ASSIGNMENTS fa1 " +
					"LEFT OUTER JOIN PFR_FAILURE_ASSIGNMENTS fa2 " +
					"ON (fa1.FAILURE_ID = fa2.FAILURE_ID AND fa1.CREATED < fa2.CREATED) " +
					"WHERE fa2.ID IS NULL " +
					") D " +
					"ON F.ID = D.FAILURE_ID " +
					"WHERE F.IS_GENERAL_FAILURE = 1 AND (B.STATUS = #{failure_status_announced} OR B.STATUS = #{failure_status_in_progress}) AND D.GRAF IN (SELECT GRAF FROM H_GRAF G) " +
					"GROUP BY F.POSTAL_CODE"
	)
	@Results({
			@Result(property="postal_code", column="POSTAL_CODE")
	})
	public List<String> getActiveGeneralFailuresPostalCodesByGraf(@Param("failure_status_announced") String failure_status_announced, @Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------failure type for a given id----------------------------------
	@Select("SELECT * FROM PFR_FAILURE_TYPE WHERE ID = #{0}")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="description", column="DESCRIPTION")
	})
	public FailureType getFailureType(Integer id);

	//--------------failure assignment for a given id----------------------------------
	@Select("SELECT * FROM PFR_FAILURE_ASSIGNMENTS WHERE ID = #{0}")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="failure_id", column="FAILURE_ID"),
			@Result(property="created", column="CREATED"),
			@Result(property="assignor", column="CREATOR")
	})
	public FailureAssignment getFailureAssignment(Long id);

	//--------------current active failures with related coordinates-------------------
	public List<Failure> getActiveFailuresWithCoords(@Param("failure_status_announced") String failure_status_announced, @Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------get position of texnites-------------------
	@Select("select * from apokd.SYNE_LAST_POSITION slp " +
			"join appstd.h_texnites ht on slp.am_numeric is not null and slp.am_numeric=ht.matr " +
			"where exists(select graf from H_GRAF hg where ht.miom=hg.mo) " +
			"and exists(select su.usr from sec_users su join sec_users_roles sur on su.id=sur.usr_id and sur.role_id=62 and sur.delink_date is null " +
				"where su.am = slp.am) " +
			"and slp.DEDDIE_USER=1 and slp.dt_updated>=sysdate - interval '2' hour")
	@Results({
			@Result(property="mhtroo", column="AM"),
			@Result(property="lastname", column="EPON"),
			@Result(property="firstname", column="ONKY"),
			@Result(property="fathersname", column="ONPA"),
			@Result(property="kathgoria", column="KATH"),
			@Result(property="eidikothta", column="EIDI"),
			@Result(property="lon", column="LON"),
			@Result(property="lat", column="LAT"),
			@Result(property="position_updated", column="DT_UPDATED"),
			@Result(property="grafeioList", column="MIOM", javaType=List.class, many=@Many(select="getGrafeiaperMIOM")),
			@Result(property="assignedFailures", column="AM", javaType=List.class, many=@Many(select="getActiveFailuresWithCoordsPerTexnithv2"))
	})
	public List<TexnithsDTO> getTexnitesPositions();

	//--------------current active failures with related coordinates per texnith v2-------------------
	@Select("SELECT F.* FROM PFR_FAILURES F " +
			"WHERE F.ASSIGNED_TEXNITHS = #{assigned_texniths_am} " +
			"AND ((F.STATUS = 'ANNOUNCED') OR (F.STATUS = 'IN_PROGRESS'))")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="failureType", column="FAILURE_TYPE_ID", javaType=FailureType.class, one=@One(select="getFailureType")),
			@Result(property="events", column="ID", javaType=List.class, many=@Many(select="getFailureHistory")),
			@Result(property="landmarks", column="ID", javaType=List.class, many=@Many(select="getFailureLandmarks")),
			@Result(property="questions", column="ID", javaType=List.class, many=@Many(select="getFailureQuestions")),
			@Result(property="general_failure_group", column="GENERAL_FAILURE_GROUP_ID", javaType=GeneralFailureGroup.class, one=@One(select="getGeneralFailureGroup")),
	})
	public List<Failure> getActiveFailuresWithCoordsPerTexnithv2(@Param("assigned_texniths_am") String assigned_texniths_am);

	//--------------current active failures with related coordinates per texnith-------------------
	@Select("SELECT F.* FROM PFR_FAILURES F " +
			"WHERE F.ASSIGNED_TEXNITHS = #{assigned_texniths_am} " +
			"AND ((F.STATUS = #{failure_status_announced}) OR (F.STATUS = #{failure_status_in_progress}))")
	@Results({
			@Result(property="failureType", column="FAILURE_TYPE_ID", javaType=FailureType.class, one=@One(select="getFailureType")),
			@Result(property="events", column="ID", javaType=List.class, many=@Many(select="getFailureHistory")),
			@Result(property="landmarks", column="ID", javaType=List.class, many=@Many(select="getFailureLandmarks")),
			@Result(property="questions", column="ID", javaType=List.class, many=@Many(select="getFailureQuestions")),
			@Result(property="general_failure_group", column="GENERAL_FAILURE_GROUP_ID", javaType=GeneralFailureGroup.class, one=@One(select="getGeneralFailureGroup")),
	})
	public List<Failure> getActiveFailuresWithCoordsPerTexnith(@Param("failure_status_announced") String failure_status_announced,
															   @Param("failure_status_in_progress") String failure_status_in_progress,
															   @Param("assigned_texniths_am") String assigned_texniths_am);

	//--------------get active failures by supply_no or postal_code------------------
	public List<FailureDTO> getActiveFailureDTOsBySupplyNoOrPostalCode(@Param("supply_no") String supply_no, @Param("postal_code") String postal_code,
																	   @Param("failure_status_announced") String failure_status_announced,
																	   @Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------get active failures of a power supply------------------
	public List<Failure> getSupplysActiveFailures(@Param("supply_no") String supply_no,
																	   @Param("failure_status_announced") String failure_status_announced,
																	   @Param("failure_status_in_progress") String failure_status_in_progress);

	//--------------responsible grafeia per postal_code-------------------
	@Select("SELECT HG.GRAF," +
			" CASE WHEN '" + LocalizationUTIL.ENGLISH + "' = #{language} THEN HG.PERI_ENG ELSE PERI END AS PERI," +
			" HG.PERX, HG.PERF, HG.HMEN, HG.HMLH, HG.BOK, HG.PLHR, HG.THLE, HG.MO FROM H_GRAF HG JOIN H_TK_GRAF HTG ON HG.GRAF=HTG.GRAF WHERE HTG.TK=#{postal_code}")
	public List<Grafeio> getResponsibleGrafsperPostalCode(@Param("postal_code") String postal_code, @Param("language") String language);

	//--------------failure record for a given failure-------------------
    @Select("SELECT F.* FROM PFR_FAILURES F WHERE F.ID=#{0}")
	@Results(id = "failure", value = {
            @Result(property="id", column="ID"),
			@Result(property="area", column="AREA"),
			@Result(property="address", column="ADDRESS"),
            @Result(property="name", column="NAME"),
            @Result(property="postal_code", column="POSTAL_CODE"),
			@Result(property="failureType", column="FAILURE_TYPE_ID", javaType=FailureType.class, one=@One(select="getFailureType")),
			@Result(property="events", column="ID", javaType=List.class, many=@Many(select="getFailureHistory")),
			@Result(property="landmarks", column="ID", javaType=List.class, many=@Many(select="getFailureLandmarks")),
			@Result(property="questions", column="ID", javaType=List.class, many=@Many(select="getFailureQuestions")),
			@Result(property="assignments", column="ID", javaType=List.class, many=@Many(select="getFailureAssignments")),
			@Result(property="general_failure_group", column="GENERAL_FAILURE_GROUP_ID", javaType=GeneralFailureGroup.class, one=@One(select="getGeneralFailureGroup")),
            @Result(property="comments", column="COMMENTS"),          
            @Result(property="phone", column="PHONE"),
            @Result(property="cell", column="CELL"),
            @Result(property="email", column="EMAIL"),
			@Result(property="supply_no", column="SUPPLY_NO"),
			@Result(property="supply_no_partial", column="SUPPLY_NO_PARTIAL"),
			@Result(property="estimated_restore_time", column="ESTIMATED_RESTORE_TIME"),
			@Result(property="assigned_texniths", column="ASSIGNED_TEXNITHS", javaType=Misthotos.class, one=@One(select="getTexnitis")),
			@Result(property="assigned_texniths_contractor", column="ASSIGNED_TEXNITHS_CONTRACTOR", javaType=ContractorMisthotos.class, one=@One(select="getContractorTexnitis")),
			@Result(property="creator", column="CREATOR"),
			@Result(property="real_estate_usage", column="REAL_ESTATE_USAGE"),
			@Result(property="created", column="CREATED"),
    })
    public Failure getFailure(Long failure_id);
	
	//--------------all history for a given failure-------------------
    @Select("SELECT H.*, M.* FROM PFR_FAILURE_HISTORY H LEFT JOIN PFR_MESSAGE M ON H.MESSAGE_ID=M.ID WHERE FAILURE_ID = #{0}")
    @Results({
            @Result(property="id", column="ID"),
            @Result(property="failure_id", column="FAILURE_ID"),
            @Result(property="message_id", column="MESSAGE_ID"),
            @Result(property="message_header", column="HEADER"),
            @Result(property="message_description", column="DESCRIPTION"),
            @Result(property="created", column="CREATED"),
            @Result(property="contact_method", column="CONTACT_METHOD"),
            @Result(property="status", column="STATUS"),
            @Result(property="comments", column="COMMENTS")
    })
    public List<FailureHistory> getFailureHistory(String failure_id);

	@Select("SELECT FA.* FROM PFR_FAILURE_ASSIGNMENTS FA WHERE FAILURE_ID = #{0}")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="failure_id", column="FAILURE_ID"),
			@Result(property="assignee", column="GRAF", javaType=Grafeio.class, one=@One(select="getGrafeio")),
			@Result(property="created", column="CREATED"),
			@Result(property="assignor", column="CREATOR")
	})
	public List<FailureAssignment> getFailureAssignments(String failure_id);

	//--------------all landmarks for a given failure-------------------
	@Select("SELECT L.* FROM PFR_FAILURE_LANDMARKS L WHERE FAILURE_ID = #{0}")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="failure_id", column="FAILURE_ID"),
			@Result(property="type", column="TYPE"),
			@Result(property="pointsList", column="ID", javaType=List.class, many=@Many(select="getLandmarkPoints"))
	})
	public List<Landmark> getFailureLandmarks(String failure_id);

	//--------------all points for a given landmark-------------------
	@Select("SELECT P.* FROM PFR_LANDMARK_POINTS P WHERE LANDMARK_ID = #{0}")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="landmark_id", column="LANDMARK_ID"),
			@Result(property="lon", column="LONGITUDE"),
			@Result(property="lat", column="LATITUDE")
	})
	public List<Point> getLandmarkPoints(String landmark_id);

	//--------------all questions for a given failure-------------------
	@Select("SELECT FQ.*, Q.TEXT FROM PFR_FAILURE_QUESTIONS FQ JOIN PFR_QUESTIONS Q ON FQ.QUESTION_ID = Q.ID WHERE FAILURE_ID = #{0}")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="failure_id", column="FAILURE_ID"),
			@Result(property="text", column="TEXT"),
			@Result(property="question_id", column="QUESTION_ID"),
			@Result(property="answer", column="ANSWER")
	})
	public List<Question> getFailureQuestions(String failure_id);

	//--------------all failure types----------------------------------
    @Select("SELECT * FROM PFR_FAILURE_TYPE WHERE DELETED=0")
    @Results({
            @Result(property="id", column="ID"),
            @Result(property="description", column="DESCRIPTION")
    })
    public List<FailureType> getFailureTypes();

    //--------------all failures announcements--------------------------
    @Select("SELECT * FROM PFR_FAILURES")
    @Results({
            @Result(property="id", column="ID"),
            @Result(property="created", column="CREATED"),
            @Result(property="address", column="ADDRESS"),
            @Result(property="name", column="NAME"),
            @Result(property="postal_code", column="POSTAL_CODE"),
            @Result(property="failure_type_id", column="FAILURE_TYPE_ID"),
            @Result(property="comments", column="COMMENTS"),
            @Result(property="phone", column="PHONE"),
            @Result(property="cell", column="CELL"),
            @Result(property="email", column="EMAIL")
    })
    public List<Failure> getFailuresAnnouncements();


    //------------------------authenticate------------------------------
    @Select("SELECT ID, USR, LDAP_USR, FULL_NAME FROM SEC_USERS WHERE USR = #{username} AND PWD = #{password}")
    @Results({
            @Result(property="id", column="ID"),
            @Result(property="username", column="USR"),
            @Result(property="ldap_username", column="LDAP_USR"),
            @Result(property="fullName", column="FULL_NAME")
    })
    public UserLoginInfo authenticate(@Param("username") String username, @Param("password") String password);


	//------------------------get Menu Items------------------------------
	@Select("select id, item, page,PARENT_ID,DISPLAY_ORDER from ui_menu where id in ( "+
			"select distinct(m.id) "+
			"from ui_menu m "+
			"join sec_roles_ui_menu rm on rm.menu_id = m.id and rm.delink_date is null "+
			"join sec_roles r on r.id = rm.role_id "+
			"join sec_users_roles ur on ur.role_id=r.id and ur.delink_date is null "+
			"join sec_sessions s on s.sec_user_id = ur.usr_id and s.end_date is null "+
			"where s.token= #{token} and r.app_id= #{appId} "+
			") "+
			"order by PARENT_ID DESC, display_order ")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="title", column="ITEM"),
			@Result(property="href", column="PAGE"),
			@Result(property="parentId", column="PARENT_ID"),
			@Result(property="displayOrder", column="DISPLAY_ORDER")
	})
	public List<MenuItem> getMenuItems (@Param("token") String token, @Param("appId") Integer appId);


	//------------------------get User Roles------------------------------
	@Select("select name from sec_roles where id in " +
			"(select role_id from sec_users_roles sur " +
			"join sec_sessions ss on sur.usr_id=ss.sec_user_id " +
			"and sur.delink_date is null and ss.end_date is null " +
			"and ss.token = #{0})")
	@Results({	
			@Result(property="role", column="NAME")
	})
	public List<String> getUserRoles (String token);

	//------------------------get User Role DTOs------------------------------
	@Select("select id, name, descr from sec_roles where id in " +
			"(select role_id from sec_users_roles sur " +
			"join sec_sessions ss on sur.usr_id=ss.sec_user_id " +
			"and sur.delink_date is null and ss.end_date is null " +
			"and ss.token = #{0})")
	public List<UserRoleDTO> getUserRoleDTOs (String token);

	//------------------------get User Roles------------------------------
	@Select("select usr from sec_users su " +
			"join sec_sessions ss on su.id=ss.sec_user_id " +
			"and ss.token = #{0} and ss.end_date is null")
	public String getUser (String token);

	//------------------------get User AM------------------------------
	@Select("select su.am from sec_sessions ss " +
			"join sec_users su on ss.sec_user_id=su.id and ss.token = #{0} and ss.end_date is null ")
	public String getAM (String token);

	//--------------get grafeia with assigned postal codes list---------------
	public List<GrafeioWithPostalCodes> getGrafeiaWithPostalCodes();

	//------------------------get grafeia assigned to specific postal_code------------------------------
	@Select("SELECT GRAF FROM H_GRAF JOIN H_TK_GRAF USING(GRAF) WHERE TK = #{0}")
	public List<String> getGrafeiaAssignedToPostalCode (String postal_code);

	//------------------------get grafeia which are active------------------------------
	@Select("SELECT * FROM H_GRAF GRF JOIN PFR_ACTIVE_GRAF ACT ON GRF.GRAF=ACT.GRAF WHERE ACT.ACTIVE_TO IS NULL")
	public List<Grafeio> getGrafeiaActive();

	//------------------------check whether the provided graf is active------------------------------
	@Select("SELECT COUNT(1) FROM PFR_ACTIVE_GRAF ACT WHERE ACT.ACTIVE_TO IS NULL AND ACT.GRAF = #{0}")
	public Boolean isGrafeioActive(String graf);

	//--------------mark/unmark failure as General---------------
	public Integer alterFailuresGeneralFlag(@Param("list") List<FailureDTO> failureDTOS, @Param("flag") Boolean generalFlag);

	//--------------update the generalFailureGroup field in a list of failures---------------
	public Integer updateFailuresGroup(@Param("list") List<FailureDTO> failureDTOS, @Param("generalFailureGroup") GeneralFailureGroup generalFailureGroup);

	//--------------get all valid announcements-------------------
	@Select("SELECT * FROM PFR_ANNOUNCEMENTS WHERE IS_ENABLED = 1 AND VALID_TO IS NULL AND sysdate >= VALID_FROM" +
			" UNION " +
			"SELECT AN.* FROM PFR_ANNOUNCEMENTS AN WHERE AN.IS_ENABLED = 1 and sysdate BETWEEN VALID_FROM AND VALID_TO")
	public List<Announcement> getValidAnnouncements();

	//--------------get active announcements for the requested version-------------------
	@Select("SELECT ID, CASE WHEN '" + LocalizationUTIL.ENGLISH + "' = #{language} THEN MESSAGE_ENG ELSE MESSAGE END AS MESSAGE " +
			"FROM PFR_MOBILE_APP_ANNOUNCEMENTS WHERE IS_ENABLED = 1 AND VALID_TO IS NULL AND sysdate >= VALID_FROM AND VERSION = #{version} AND OS IN ('BOTH', #{os})" +
			" UNION " +
			"SELECT AN.ID, AN.MESSAGE FROM PFR_MOBILE_APP_ANNOUNCEMENTS AN WHERE AN.IS_ENABLED = 1 and sysdate BETWEEN VALID_FROM AND VALID_TO AND VERSION = #{version} AND OS IN ('BOTH', #{os})")
	public List<MobileAppAnnouncementDTO> getAnnouncements(@Param("version") String version, @Param("os") String os, @Param("language") String language);

	//--------------texnites by graf-------------------
	@Select(
			"with mitroa_texniton as " +
					"(select unique(ht.matr) from H_TEXNITES ht join h_graf hg on ht.miom=hg.mo and ht.IS_DELETED = '0') " +
					"select a.* from H_TEXNITES a join mitroa_texniton b on a.matr=b.matr order by epon, onky"
	)
	@Results(id = "misthotos", value = {
			@Result(property="mhtroo", column="MATR"),
			@Result(property="lastname", column="EPON"),
			@Result(property="firstname", column="ONKY"),
			@Result(property="fathersname", column="ONPA"),
			@Result(property="kathgoria", column="KATH"),
			@Result(property="eidikothta", column="EIDI"),
			@Result(property="misthodotiki_omada", column="MIOM")
	})
	public List<Misthotos> getTexnitesByGraf();

	//--------------contractor texnites by graf-------------------
	@Select(
			"select distinct(us.usr) as mhtroo, us.full_name as full_name, c.name as contractor_name from h_contractors c " +
					"join h_contractor_user_links a on a.contractor_id = c.id and a.delink_date is null and " +
						"(c.ergodiktyou ='1' or c.code in ('11')) " +
					"join h_contractor_mona_user_links u on u.con_user_id = a.id and u.delink_date is null " +
					"join h_contractor_mona_links m on m.contractor_id = c.id and m.delink_date is null and m.mona = u.mona " +
					"join sec_users us on us.id = a.sec_user_id order by full_name"
	)
	@Results(id = "contractormisthotos", value = {
			@Result(property="mhtroo", column="MHTROO"),
			@Result(property="fullname", column="FULL_NAME"),
			@Result(property="contractorName", column="CONTRACTOR_NAME"),

	})
	public List<ContractorMisthotos> getTexnitesContractorByGraf();

	// --------------insert failure audits--------------------------------------------
	public int insertFailureAudits(List<FailureAudit> failureAuditList);

	//---------------- returns failuresbook contents -------------------
	public List<FailureDTO> getFailuresBookContentbyVPD(@Param("list") List<Grafeio> grafeioList, @Param("month") String month, @Param("year") String year);

	//--------------insert photo--------------------------------------------
	@Insert("INSERT into PFR_PHOTOS (failure_id, name, photo, photo_size, content_type) VALUES(#{failure_id}, #{name}, #{photo}, #{photo_size}, #{content_type} )")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	public int insertNetworkHazardPhoto(NetworkHazardPhoto nhp);

	//--------------select questions for network hazard----------------------
	@Select("SELECT ID, CASE WHEN '" + LocalizationUTIL.ENGLISH + "' = #{0} THEN TEXT_ENG ELSE TEXT END AS TEXT, DELETED, IS_DANGEROUS, ORDERING FROM PFR_QUESTIONS WHERE IS_DANGEROUS = 1 ORDER BY ORDERING ASC")
	public List<Question> getQuestionsForNetworkHazard(String language);

	//--------------get the type of failure----------------------
	@Select("SELECT failure_type_id FROM pfr_failures WHERE id=#{0}")
	public Integer getTypeOfFailure(Long failure_id);

	//--------------get CallcenterPerformanceMetrics----------------------
	@Select("SELECT  DATE_ANNOUNCED REQUESTEDDATE,TIME_ANNOUNCED TIMEPERIOD, COUNT(DISTINCT AGENT) AGENTS ,count(*) ANNOUNCEMENTS from " +
			"( SELECT F.ID, F.INPUT_CHANNEL, C.CREATOR AGENT, C.CREATED AS ANNOUNCED, " +
			"  		TRUNC (C.CREATED) AS DATE_ANNOUNCED, TO_CHAR (C.CREATED, 'HH24') || '.00-' || TO_CHAR (C.CREATED, 'HH24') " +
			"       || '.59' AS TIME_ANNOUNCED " +
			"  FROM PFR_FAILURES F " +
			"  JOIN (SELECT c1.* " +
			"        FROM PFR_FAILURE_HISTORY c1 " +
			"        WHERE id = (SELECT MIN(id) FROM PFR_FAILURE_HISTORY WHERE failure_id = c1.failure_id)) C " +
			"        ON F.ID = C.FAILURE_ID" +
			"  WHERE TRUNC(C.CREATED)=#{0} AND INPUT_CHANNEL='CALLCENTER_AGENT') " +
			"  GROUP BY DATE_ANNOUNCED,TIME_ANNOUNCED order by 2 desc")
	public List<CallcenterPerformanceMetricsDTO> getCallcenterPerformanceMetrics(Date requestedDate);

	//--------------get list of KLOT----------------------
	@Select("SELECT KLOT, CASE WHEN '" + LocalizationUTIL.ENGLISH + "' = #{0} THEN PERI_ENG ELSE PERI END AS PERI FROM H_KLOT")
	public List<KallikratikosOTA> getKLOT(String language);

	//--------------get list of KLOT by VPD----------------------
	@Select("SELECT hk.klot,hk.klno, hk.peri FROM h_klot hk join h_graf_klot_ermhs hgke on hgke.klot=hk.klot " +
			"join h_graf hg on hg.graf=hgke.graf " +
			"group by hk.klot,hk.klno, hk.peri")
	public List<KallikratikosOTA> getKLOTbyVPD();

	//--------------get KLOT by id----------------------
	@Select("SELECT * FROM h_klot where klot = #{0}")
	public KallikratikosOTA getKLOTByID(String klot);

	//--------------get KLOT by Ermhs KLOT----------------------
	@Select("SELECT hk.klno, hk.klot, hk.peri FROM h_klot hk join h_klot_ermhs_klot hkek on hkek.klot=hk.klot " +
			"where hkek.ermhs_klot = #{0}")
	public KallikratikosOTA getKLOTbyErmhsKLOT(String klot);

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

	@Select("SELECT IVR_TOKEN FROM PFR_SETTINGS")
	public String getIVRToken();

	@Select("with PFR_INFO as (\n" +
			"                SELECT F.Id,T1.id id_hist, T1.STATUS status_hist, \n" +
			"                MAX(T1.ID) over (partition by failure_id) id_last\n" +
			"                FROM PFR_FAILURES F \n" +
			"                    INNER JOIN PFR_FAILURE_HISTORY t1 \n" +
			"                    on F.id = t1.failure_id \n" +
			"                        WHERE F.SUPPLY_NO_PARTIAL = #{supply_no}\n" +
			"\n" +
			")\n" +
			"select F.* from PFR_FAILURES F JOIN PFR_INFO X on F.id=X.id\n" +
			"where 1=1\n" +
			"AND (X.status_hist =  #{failure_status_announced}  OR X.status_hist = #{failure_status_in_progress}) AND X.id_hist=X.id_last")
	public List<Failure> getSupplysActiveFailuresIVR(@Param("supply_no") String supply_no,
													 @Param("failure_status_announced") String failure_status_announced,
													 @Param("failure_status_in_progress") String failure_status_in_progress);
}
