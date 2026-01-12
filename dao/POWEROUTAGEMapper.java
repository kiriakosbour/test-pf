package gr.deddie.pfr.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import gr.deddie.pfr.dao.sqlprovider.LocalizationSQLProvider;
import gr.deddie.pfr.model.*;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.FetchType;

/**
 * Created by M.Paschou on 15/12/2017.
 */

public interface POWEROUTAGEMapper {

	// --------------insert power outage---------------------------------------------
	@Insert("INSERT INTO PFR_POWER_OUTAGES(GRAF, START_DATE, END_DATE, IS_ACTIVE, IS_SCHEDULED, CAUSE, CREATOR, END_DATE_ANNOUNCED) "
			+ "VALUES(#{grafeio.graf}, #{start_date}, #{end_date}, #{is_active}, #{is_scheduled}, #{cause}, #{creator}, #{end_date_announced})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
	public int insertPowerOutage(PowerOutage po);

	// --------------insert power outage TALD info---------------------------------------------
	@Insert("INSERT INTO PFR_POWER_OUTAGES_TALD(ID, PERIFEREIA_ID, TALD, POWER_OUTAGE_DFR_ID, IVR_MESSAGE) "
			+ "VALUES(#{id}, #{perifereia.id}, #{tald}, #{power_outage_dfr_id}, #{text})")
	public int insertTALDPowerOutage(PowerOutage po);

	// --------------update power outage---------------------------------------------
	public int updatePowerOutage(PowerOutage po);

	// --------------update TALD power outage---------------------------------------------
	public int updateTALDPowerOutage(PowerOutage po);

	// --------------insert exyphretoymenes perioxes related to power outage--------------------------------------------
	public int insertPowerOutageExypPerioxes(PowerOutage po);

	// --------------insert exyphretoymenes dhmotikes enothtes related to power outage--------------------------------------------
	public int insertPowerOutageExypDE(PowerOutage po);

	// --------------insert lektika genikon diakopon related to power outage--------------------------------------------
	public int insertPowerOutageLektika(PowerOutage po);

	// --------------insert Kallikratiki Dhmotiki Enothta List related to power outage--------------------------------------------
	public int insertPowerOutageKallikratikiDhmotikiEnothtaList(PowerOutage po);

	// --------------insert Kallikratikos OTA List related to power outage--------------------------------------------
	public int insertPowerOutageKallikratikosOTAList(PowerOutage po);

	// --------------insert Kallikratiki Nomarxia List related to power outage--------------------------------------------
	public int insertPowerOutageKallikratikiNomarxiaList(PowerOutage po);

	// --------------add exyphretoymenes perioxes related to power outage--------------------------------------------
	public int addPowerOutageExypPerioxes(@Param("poweroutage") PowerOutage po, @Param("exyphretoumeniPerioxiList") List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList);

	// --------------add exyphretoymenes dhmotikes enothtes related to power outage--------------------------------------------
	public int addPowerOutageExypDE(@Param("poweroutage") PowerOutage po, @Param("exyphretoumeniDhmEnothtaList") List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList);

	// --------------add Lektika related to power outage--------------------------------------------
	public int addPowerOutageLektika(@Param("poweroutage") PowerOutage po, @Param("lektikoGenikonDiakoponList") List<LektikoGenikonDiakopon> lektikoGenikonDiakoponList);

	// --------------add kallikratikiDhmotikiEnothtaList related to power outage--------------------------------------------
	public int addPowerOutageKallikratikesDhmotikesEnothtes(@Param("poweroutage") PowerOutage po, @Param("kallikratikiDhmotikiEnothtaList") List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList);

	// --------------add kallikratikosOTAList related to power outage--------------------------------------------
	public int addPowerOutageKallikratikoiOTA(@Param("poweroutage") PowerOutage po, @Param("kallikratikosOTAList") List<KallikratikosOTA> kallikratikosOTAList);

	// --------------add kallikratikiNomarxiaList related to power outage--------------------------------------------
	public int addPowerOutageKallikratikesNomarxies(@Param("poweroutage") PowerOutage po, @Param("kallikratikiNomarxiaList") List<KallikratikiNomarxia> kallikratikiNomarxiaList);

	// --------------delete exyphretoymenes perioxes related to power outage--------------------------------------------
	public int deletePowerOutageExypPerioxes(@Param("poweroutage") PowerOutage po, @Param("exyphretoumeniPerioxiList") List<ExyphretoumeniPerioxi> exyphretoumeniPerioxiList);

	// --------------delete exyphretoymenes dhmotikes enothtes related to power outage--------------------------------------------
	public int deletePowerOutageExypDE(@Param("poweroutage") PowerOutage po, @Param("exyphretoumeniDhmEnothtaList") List<ExyphretoumeniDhmEnothta> exyphretoumeniDhmEnothtaList);

	// --------------delete LEKTIKA related to power outage--------------------------------------------
	public int deletePowerOutageLektika(@Param("poweroutage") PowerOutage po, @Param("lektikoGenikonDiakoponList") List<LektikoGenikonDiakopon> lektikoGenikonDiakoponList);

	// --------------delete kallikratikes dhmotikes enothtes related to power outage--------------------------------------------
	public int deletePowerOutageKallikratikesDhmotikesEnothtes(@Param("poweroutage") PowerOutage po, @Param("kallikratikiDhmotikiEnothtaList") List<KallikratikiDhmotikiEnothta> kallikratikiDhmotikiEnothtaList);

	// --------------delete kallikratikous ota related to power outage--------------------------------------------
	public int deletePowerOutageKallikratikoiOta(@Param("poweroutage") PowerOutage po, @Param("kallikratikosOTAList") List<KallikratikosOTA> kallikratikosOTAList);

	// --------------delete kallikratikes nomarxies related to power outage--------------------------------------------
	public int deletePowerOutageKallikratikesNomarxies(@Param("poweroutage") PowerOutage po, @Param("kallikratikiNomarxiaList") List<KallikratikiNomarxia> kallikratikiNomarxiaList);

	// --------------insert power outage audits--------------------------------------------
	public int insertPowerOutageAudits(List<PowerOutageAudit> powerOutageAuditList);

	// --------------get all power outage DTOs--------------------------------------
	@Select("SELECT * FROM PFR_POWER_OUTAGES")
	@Results(id = "powerOutage", value = {
			@Result(property="id", column="ID"),
			@Result(property="grafeio", column="GRAF", javaType=Grafeio.class, one=@One(select="getGrafeio")),
			@Result(property="perifereia", column="PERIFEREIA_ID", javaType=Perifereia.class, one=@One(select="getPerifereia")),
			@Result(property="tald", column="TALD"),
			@Result(property="power_outage_dfr_id", column="POWER_OUTAGE_DFR_ID"),
			@Result(property="start_date", column="START_DATE"),
			@Result(property="end_date", column="END_DATE"),
			@Result(property="is_active", column="IS_ACTIVE"),
			@Result(property="is_scheduled", column="IS_SCHEDULED"),
			@Result(property="cause", column="CAUSE"),
			@Result(property="created", column="CREATED"),
			@Result(property="creator", column="CREATOR"),
			@Result(property="exyphretoumeniPerioxiList", column="ID", many=@Many(select="getExyphretoumenesPerioxesbyPowerOutage")),
			@Result(property="lektikoGenikonDiakoponList", column="{id=ID, language=LANGUAGE}", many=@Many(select="getLektikabyPowerOutage")),
			@Result(property="exyphretoumeniDhmEnothtaList", column="ID", many=@Many(select="getExyphretoumenesDEbyPowerOutage")),
			@Result(property="kallikratikiDhmotikiEnothtaList", column="{id=ID, language=LANGUAGE}", many=@Many(select="getkallikratikiDhmotikiEnothtaListbyPowerOutage")),
			@Result(property="kallikratikosOTAList", column="{id=ID, language=LANGUAGE}", many=@Many(select="getkallikratikosOTAListbyPowerOutage")),
			@Result(property="kallikratikiNomarxiaList", column="{id=ID, language=LANGUAGE}", many=@Many(select="getkallikratikiNomarxiaListbyPowerOutage"))
	})
	public List<PowerOutage> getAllPowerOutages(@Param("language") String language);

	// ---------------get power outage by ID----------------------------------------
	@Select("SELECT ID, START_DATE, END_DATE, IS_ACTIVE, IS_SCHEDULED, CAUSE, CREATED, CREATOR, GRAF, END_DATE_ANNOUNCED, #{language} as language FROM PFR_POWER_OUTAGES WHERE ID = #{id}")
	@ResultMap("powerOutage")
	public PowerOutage getPowerOutage(@Param("id") Long id, @Param("language") String language);

	// --------------get TALD power outage------------------------------------------
	@Select("SELECT PPO.ID, PPO.START_DATE, PPO.END_DATE, PPO.IS_ACTIVE, PPO.IS_SCHEDULED, PPO.CAUSE, PPO.CREATED, PPO.CREATOR, PPO.GRAF, PPO.END_DATE_ANNOUNCED, #{language} as LANGUAGE FROM PFR_POWER_OUTAGES PPO JOIN PFR_POWER_OUTAGES_TALD PPOT ON PPO.ID=PPOT.ID AND POWER_OUTAGE_DFR_ID = #{power_outage_dfr_id} ")
	@ResultMap("powerOutage")
	public PowerOutage getTALDPowerOutage(@Param("power_outage_dfr_id") Long power_outage_dfr_id, @Param("language") String language);

	// --------------get TALD power outage by ID------------------------------------------
	@Select("SELECT * FROM PFR_POWER_OUTAGES PPO JOIN PFR_POWER_OUTAGES_TALD PPOT ON PPO.ID=PPOT.ID WHERE PPO.ID = #{0} ")
	@ResultMap("powerOutage")
	public PowerOutage getTALDPowerOutageByID(Long power_outage_id);

	// --------------get all perifereiakes enotites--------------------------------------
	@Select("SELECT * FROM H_KALLIKRATIS WHERE TYPE_ID = 293")
	public List<PerifereiakiEnotita> getPerifereiakesEnotites();

	// --------------get all kallikratikos ota--------------------------------------
	@Select("SELECT * FROM H_KLOT")
	@Results({
			@Result(property="klot", column="KLOT"),
			@Result(property="peri", column="PERI"),
			@Result(property="kallikratikiDhmotikiEnothtaList", column="KLOT", javaType=List.class, many=@Many(select="getKallikratikiDhmotikiEnotitaListbyKLOT"))
	})public List<KallikratikosOTA> getKallikratikosOTAList();

	// --------------get all perifereies-------------------------------------------------
	@Select("SELECT * FROM H_KALLIKRATIS WHERE TYPE_ID = 292")
	public List<Perifereia> getPerifereies();

	// --------------get exyphretoymenes perioxes by power outage----------------------------------
	@Select("SELECT HEPAG.* FROM H_EXYPHRET_PERIOXES_ANA_GRAMMH HEPAG JOIN PFR_POWER_OUT_EXYP_PERIOXES PPOEP ON PPOEP.EXYP_PERIOXI_ID=HEPAG.ID WHERE PPOEP.POWER_OUTAGE_ID = #{0}")
	@ResultMap("exyphretoumeni_perioxi")
	public List<ExyphretoumeniPerioxi> getExyphretoumenesPerioxesbyPowerOutage(Long power_outage_id);

	// --------------get exyphretoymenes dhmotikes enothtes by power outage----------------------------------
	@Select("SELECT HEDAG.* FROM H_EXYP_DE_ANA_GRAMMH_MT HEDAG JOIN PFR_POWER_OUT_EXYP_DE PPOEDE ON PPOEDE.EXYP_DE_ID=HEDAG.ID WHERE PPOEDE.POWER_OUTAGE_ID = #{0}")
	@ResultMap("exyphretoumeni_dhmotiki_enothta")
	public List<ExyphretoumeniDhmEnothta> getExyphretoumenesDEbyPowerOutage(Long power_outage_id);

	// --------------get lektika by power outage----------------------------------
//	@Select("SELECT LGDH.* END AS TEXT, FROM H_LEKTIKA_GENIKON_DIAKOPON LGDH JOIN PFR_POWER_OUT_LEKTIKA PPOL ON PPOL.LEKTIKO_ID=LGDH.ID WHERE PPOL.POWER_OUTAGE_ID = #{id}")
	@SelectProvider(type = LocalizationSQLProvider.class, method = "getLektikabyPowerOutage")
	public List<LektikoGenikonDiakopon> getLektikabyPowerOutage(@Param("language") String language, @Param("id") Long id);

	// --------------get kallikratiki dhmotiki enothta list by power outage----------------------------------
//	@Select("SELECT HK.* FROM H_KLDE HK JOIN PFR_POWER_OUT_KLDE PPOK ON PPOK.KLDE=HK.KLDE WHERE PPOK.POWER_OUTAGE_ID = #{0}")
	@SelectProvider(type = LocalizationSQLProvider.class, method = "getkallikratikiDhmotikiEnothtaListbyPowerOutage")
	public List<KallikratikiDhmotikiEnothta> getkallikratikiDhmotikiEnothtaListbyPowerOutage(@Param("id") Long power_outage_id, @Param("language") String language);

	// --------------get kallikratikos OTA list by power outage----------------------------------
//	@Select("SELECT HK.* FROM H_KLOT HK JOIN PFR_POWER_OUT_KLOT PPOK ON PPOK.KLOT=HK.KLOT WHERE PPOK.POWER_OUTAGE_ID = #{0}")
	@SelectProvider(type = LocalizationSQLProvider.class, method = "getkallikratikosOTAListbyPowerOutage")
	public List<KallikratikosOTA> getkallikratikosOTAListbyPowerOutage(@Param("id") Long power_outage_id, @Param("language") String language);

	// --------------get kallikratiki nomarxia list by power outage----------------------------------
//	@Select("SELECT HK.* FROM H_KLNO HK JOIN PFR_POWER_OUT_KLNO PPOK ON PPOK.KLNO=HK.KLNO WHERE PPOK.POWER_OUTAGE_ID = #{0}")
	@SelectProvider(type = LocalizationSQLProvider.class, method = "getkallikratikiNomarxiaListbyPowerOutage")
	public List<KallikratikiNomarxia> getkallikratikiNomarxiaListbyPowerOutage(@Param("id") Long power_outage_id, @Param("language") String language);

	// --------------get power outage DTOs, using filters (if any), using VPD-------------------
	public List<PowerOutage> getPowerOutageDTOsByCriteriaByVPD(@Param("start_date") Date start_date,
																  @Param("end_date") Date end_date,
																  @Param("grammiMT") GrammiMT grammiMT,
																  @Param("is_active") Boolean is_active,
																  @Param("is_scheduled") Boolean is_scheduled,
																  @Param("cause") String cause,
																  @Param("list_max_size") Long list_max_size);

	//--------------get grafeio by id--------------------------------------
//	@Select("SELECT * FROM H_GRAF WHERE GRAF = #{graf}")
	@SelectProvider(type = LocalizationSQLProvider.class, method = "getGrafeio")
	public Grafeio getGrafeio(@Param("graf") String graf, @Param("language") String language);

	// --------------get all ypostathmous Y/M - grammes MT - exyphretoymenes perioxes by VPD----------------------------
	public List<YpostathmosYM> getYpostathmoiYMGrammesMTExypPerioxesbyVPD();

	// --------------get all ypostathmous Y/M - grammes MT - exyphretoymenes dhmotikes enothtes by VPD----------------------------
	public List<YpostathmosYM> getYpostathmoiYMGrammesMTExypDEbyVPD();

	// --------------get all ypostathmous Y/M - grammes MT - exyphretoymenes perioxes ----------------------------------
	@Select("SELECT * FROM H_YPOSTATHMOI_YM YYM")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="name", column="NAME"),
			@Result(property="grammiMTList", column="ID", javaType=List.class, many=@Many(select="getGrammesMTbyYMM"))
	})
	public List<YpostathmosYM> getYpostathmoiYMGrammesMTExypPerioxes();

	// --------------get all grammes MT - exyphretoymenes perioxes by YYM ----------------------------------
	@Select("SELECT * FROM H_GRAMMES_MT WHERE YPOSTATHMOS_YM_ID = #{0}")
	@Results({
			@Result(property="name", column="NAME"),
			@Result(property="ypostathmos_ym_id", column="YPOSTATHMOS_YM_ID"),
			@Result(property="metasxhmatisths_ym_id", column="METASXHMATISTHS_YM_ID"),
			@Result(property="pylh_id", column="PYLH_ID"),
			@Result(property="exyphretoumeniPerioxiList", column="{ypostathmos_ym_id=YPOSTATHMOS_YM_ID, metasxhmatisths_ym_id=METASXHMATISTHS_YM_ID, pylh_id=PYLH_ID}", javaType=List.class, many=@Many(select="getExyphretoumenesPerioxesbyGrammiMT"))
	})
	public List<GrammiMT> getGrammesMTbyYMM(String ypostathmos_ym_id);

	// --------------get all exyphretoymenes perioxes by grammi MT by VPD----------------------------------
	public List<ExyphretoumeniPerioxi> getExyphretoumenesPerioxesbyGrammiMTbyVPD(@Param("ypostathmos_ym_id") String ypostathmos_ym_id, @Param("metasxhmatisths_ym_id") String metasxhmatisths_ym_id, @Param("pylh_id") String pylh_id);

	// --------------get all exyphretoymenes dhmotikes enotites by grammi MT by VPD----------------------------------
	public List<ExyphretoumeniDhmEnothta> getExyphretoumenesDhmEnotitesbyGrammiMTbyVPD(@Param("ypostathmos_ym_id") String ypostathmos_ym_id, @Param("metasxhmatisths_ym_id") String metasxhmatisths_ym_id, @Param("pylh_id") String pylh_id);

	//--------------Exyphretoymenes perioxes by VPD--------------------------------
	public List<ExyphretoumeniPerioxiDTO> getExyphretoumenesPerioxesbyStatusesbyVPD(@Param("list") List<ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus> statuses);

	//--------------YpostathmosYM  for a given id----------------------------------
	@Select("SELECT * FROM H_YPOSTATHMOI_YM WHERE ID = #{id}")
	public YpostathmosYM getYpostathmosYM(@Param("id") String id);

	//--------------Lektiko Genikon Diakopon for a given id----------------------------------
	@Select("SELECT * FROM H_LEKTIKA_GENIKON_DIAKOPON WHERE ID = #{0}")
	public LektikoGenikonDiakopon getLektikoGenikonDiakopon(long id);

	//--------------Kallikratikos OTA for a given id----------------------------------
	@Select("SELECT * FROM H_KLOT WHERE KLOT = #{0}")
	public KallikratikosOTA getKallikratikosOTA(String klot);

	//--------------Kallikratiki Nomarxia for a given id----------------------------------
	@Select("SELECT * FROM H_KLNO WHERE KLNO = #{0}")
	public KallikratikiNomarxia getKallikratikiNomarxia(String klno);

	//--------------get Kallikratikes Nomarxies of old nomarxia by old nomarxia id----------------------------------
	@Select("SELECT * FROM H_KLNO WHERE OLDNO = #{0}")
	public List<KallikratikiNomarxia> getKallikratikesNomarxiesOfOldNomarxia(Long oldno);

	//--------------return the members of the old nomarxia in which the nomarxiaki enothta belongs----------------------------------
	@Select("select a.* from h_klno a join h_klno b on a.oldno=b.oldno and b.klno= #{kallikratikiNomarxia.klno} union select * from h_klno where klno= #{kallikratikiNomarxia.klno}")
	public List<KallikratikiNomarxia> getKallikratikesNomarxiakesEnothtesOfOldNomarxia(@Param("kallikratikiNomarxia") KallikratikiNomarxia kallikratikiNomarxia);

	//--------------kallikratis  for a given id----------------------------------
	@Select("SELECT * FROM H_KALLIKRATIS WHERE ID = #{0}")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="description", column="DESCRIPTION")
	})
	public PerifereiakiEnotita getPerifereiakiEnotita(Long id);

	//--------------kallikratiki dhmotiki enotita for a given id----------------------------------
//	@Select("SELECT * FROM H_KLDE WHERE KLDE = #{klde}")
	@SelectProvider(type = LocalizationSQLProvider.class, method = "getKallikratikiDhmotikiEnotita")
	public KallikratikiDhmotikiEnothta getKallikratikiDhmotikiEnotita(@Param("klde") String klde, @Param("language") String language);

	//--------------kallikratiki dhmotiki enotita for a given ΟΤΑ----------------------------------
	@Select("SELECT * FROM H_KLDE WHERE KLOT = #{0}")
	public List<KallikratikiDhmotikiEnothta> getKallikratikiDhmotikiEnotitaListbyKLOT(String klot);

	//--------------get kallikratiki perifereia for a given id------------------------
	@Select("SELECT * FROM H_KALLIKRATIS WHERE ID = #{0} AND TYPE_ID=292")
	@Results({
			@Result(property="id", column="ID"),
			@Result(property="description", column="DESCRIPTION")
	})
	public Perifereia getPerifereia(Long id);

	// --------------insert exyphretoumenes perioxes---------------------------------------------
	@Insert("INSERT INTO H_EXYPHRET_PERIOXES_ANA_GRAMMH(YPOSTATHMOS_YM_ID, METASXHMATISTHS_YM_ID, PYLH_ID, EXYPHRETOYMENES_PERIOXES, GRAF, KRISIMA_FORTIA, SHMANTIKA_FORTIA, PERIFEREIAKH_ENOTHTA_ID, TMHMA_GRAMMHS, STATUS) "
			+ "VALUES(#{ypostathmosYM.id}, #{metasxhmatisths_ym_id}, #{pylh_id}, #{exyphretoymenes_perioxes}, #{grafeio.graf}, #{krisima_fortia}, #{simantika_fortia}, #{perifereiakiEnotita.id}, #{tmhma_grammhs}, #{status})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
	public int insertExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep);

	// --------------insert exyphretoumenh dhmotikh enothta--------------------------------------
	@Insert("INSERT INTO H_EXYP_DE_ANA_GRAMMH_MT(YPOSTATHMOS_YM_ID, METASXHMATISTHS_YM_ID, PYLH_ID, DHMOTIKH_ENOTHTA_ID, GRAF) "
			+ "VALUES(#{ypostathmosYM.id}, #{metasxhmatisths_ym_id}, #{pylh_id}, #{kallikratikiDhmotikiEnothta.klde}, #{grafeio.graf})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
	public int insertExyphretDEAnaGrammh(ExyphretoumeniDhmEnothta ep);

	//--------------update status of exyphretoumenes perioxes ana grammh-----------------------------------
	@Update("UPDATE (SELECT STATUS, LAST_UPD_TIMESTAMP FROM H_EXYPHRET_PERIOXES_ANA_GRAMMH JOIN H_GRAF USING (GRAF) WHERE id = #{exyphretoumeniPerioxi.id}) hepag SET hepag.status = #{status}, hepag.LAST_UPD_TIMESTAMP=systimestamp")
	public int updateExyphretPerioxesAnaGrammhStatus(@Param("exyphretoumeniPerioxi") ExyphretoumeniPerioxi ep, @Param("status")ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus status);

	//--------------update status of exyphretoumenes perioxes ana grammh-----------------------------------
	@Update("UPDATE (SELECT IS_DELETED, LAST_UPD_TIMESTAMP FROM H_EXYP_DE_ANA_GRAMMH_MT JOIN H_GRAF USING (GRAF) WHERE id = #{exyphretoumeniDhmEnothta.id}) hedag SET hedag.IS_DELETED = 1, hedag.LAST_UPD_TIMESTAMP=systimestamp")
	public int deleteExyphretDEAnaGrammh(@Param("exyphretoumeniDhmEnothta") ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta);

	//--------------update status of list of exyphretoumenes perioxes -----------------------------------
	public int updateListExyphretPerioxesAnaGrammhStatus(@Param("list") List<ExyphretoumeniPerioxi> ep, @Param("status")ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus status);

	// --------------update exyphretoumenh perioxh se grammh---------------------------------------------
	@Update("UPDATE (SELECT a.* FROM H_EXYPHRET_PERIOXES_ANA_GRAMMH a JOIN H_GRAF b ON a.GRAF = b.GRAF WHERE a.id = #{id} AND a.STATUS != 'DELETED') hepag " +
			"SET hepag.GRAF = #{grafeio.graf}, hepag.EXYPHRETOYMENES_PERIOXES = #{exyphretoymenes_perioxes}, hepag.TMHMA_GRAMMHS = #{tmhma_grammhs}, " +
			"hepag.KRISIMA_FORTIA = #{krisima_fortia}, hepag.SHMANTIKA_FORTIA = #{simantika_fortia}, hepag.PERIFEREIAKH_ENOTHTA_ID = #{perifereiakiEnotita.id}, hepag.LAST_UPD_TIMESTAMP = systimestamp")
	public int updateExyphretPerioxesAnaGrammh(ExyphretoumeniPerioxi ep);

	// --------------update exyphretoumenh dhmotikh enothta se grammh MT---------------------------------------------
	@Update("UPDATE (SELECT a.* FROM H_EXYP_DE_ANA_GRAMMH_MT a JOIN H_GRAF b ON a.GRAF = b.GRAF WHERE a.id = #{id} AND a.IS_DELETED = 0) hedag " +
			"SET hedag.GRAF = #{grafeio.graf}, hedag.DHMOTIKH_ENOTHTA_ID = #{kallikratikiDhmotikiEnothta.klde}, hedag.LAST_UPD_TIMESTAMP = systimestamp")
	public int updateExyphretDEAnaGrammh(ExyphretoumeniDhmEnothta exyphretoumeniDhmEnothta);

	// --------------get current power outages - for call center----------------------------
	public List<PowerOutageTALDDTO> getCurrentPowerOutagesLimitedInfo();

	// --------------get current power outages - per nomarxiaki enotita ----------------------------
	public List<PowerOutageIVRDTO> getPowerOutagesperNE(@Param("list") List<KallikratikiNomarxia> kallikratikiNomarxiaList);

	// --------------get current power outages - per ota----------------------------
	public List<PowerOutageIVRDTO> getPowerOutagesperOTA(@Param("kallikratikosOTA") KallikratikosOTA kallikratikosOTA);

}
