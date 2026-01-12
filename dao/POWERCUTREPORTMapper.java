package gr.deddie.pfr.dao;

import gr.deddie.pfr.model.Grafeio;
import gr.deddie.pfr.model.KallikratikiNomarxia;
import gr.deddie.pfr.model.OldNomarxia;
import gr.deddie.pfr.model.Perifereia;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface POWERCUTREPORTMapper {
    // --------------get all perifereiakes enotites--------------------------------------
    @Select("SELECT * FROM H_KLNO")
    public List<KallikratikiNomarxia> getPerifereiakesEnotites();

    // --------------get perifereiakes enotites and oldno info--------------------------------------
    @Select("SELECT KLNO, " +
            "CASE WHEN '" + LocalizationUTIL.ENGLISH + "' = #{0} THEN a.PERI_ENG ELSE a.PERI END AS PERI" +
            ", OLDNO FROM H_KLNO a LEFT JOIN H_OLDNO b USING (OLDNO) ")
    @Results({
            @Result(property="klno", column="KLNO"),
            @Result(property="peri", column="PERI"),
            @Result(property="oldNomarxia", column="OLDNO", javaType= OldNomarxia.class, one=@One(select="getOldNOmarxia"))
    })
    public List<KallikratikiNomarxia> getPerifereiakesEnotitesWithOldNomarxiesRelations(String language);

    //--------------get oldnomarxia by id--------------------------------------
    @Select("SELECT * FROM H_OLDNO WHERE OLDNO = #{0}")
    public OldNomarxia getOldNOmarxia(Long id);

    //--------------get oldnomarxies--------------------------------------
    @Select("SELECT OLDNO, CASE WHEN '" + LocalizationUTIL.ENGLISH + "' = #{0} THEN PERI_ENG ELSE PERI END AS PERI FROM H_OLDNO ")
    public List<OldNomarxia> getOldNOmarxies(String language);
}
