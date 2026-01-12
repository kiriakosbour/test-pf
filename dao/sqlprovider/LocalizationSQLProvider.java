package gr.deddie.pfr.dao.sqlprovider;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class LocalizationSQLProvider {
    public String getGrafeio(@Param("language") String language){
        return new SQL(){{
            SELECT("GRAF, PERX, PERF, HMEN, HMLH, BOK, PLHR, THLE, MO");
            if (LocalizationUTIL.ENGLISH.equals(language)){
                SELECT("PERI_ENG AS PERI");
            } else {
                SELECT("PERI");
            }
            FROM("H_GRAF");
            WHERE("GRAF = #{graf}");
        }}.toString();
    }

    public String getLektikabyPowerOutage(@Param("language") String language) {
        return new SQL() {{
            SELECT("LGDH.ID,LGDH.TO_BE_ANNOUNCED,LGDH.KLDE");
            if (LocalizationUTIL.ENGLISH.equals(language)) {
                SELECT("LGDH.TEXT_ENG AS TEXT");
            } else {
                SELECT("LGDH.TEXT");
            }
            FROM("H_LEKTIKA_GENIKON_DIAKOPON LGDH");
            JOIN("PFR_POWER_OUT_LEKTIKA PPOL ON PPOL.LEKTIKO_ID=LGDH.ID");
            WHERE("PPOL.POWER_OUTAGE_ID = #{id}");
        }}.toString();
    }

    public String getkallikratikiNomarxiaListbyPowerOutage(@Param("language") String language){
        return new SQL(){{
            SELECT("HK.KLNO, HK.KLPE, HK.OLDNO");
            if (LocalizationUTIL.ENGLISH.equals(language)){
                SELECT("HK.PERI_ENG AS PERI");
            } else {
                SELECT("HK.PERI");
            }
            FROM("H_KLNO HK");
            JOIN("PFR_POWER_OUT_KLNO PPOK ON PPOK.KLNO=HK.KLNO");
            WHERE("PPOK.POWER_OUTAGE_ID = #{id}");
        }}.toString();
    }

    public String getkallikratikiDhmotikiEnothtaListbyPowerOutage(@Param("language") String language){
        return new SQL(){{
            SELECT("HK.KLDE, HK.KLOT");
            if (LocalizationUTIL.ENGLISH.equals(language)){
                SELECT("HK.PERI_ENG AS PERI");
            } else {
                SELECT("HK.PERI");
            }
            FROM("H_KLDE HK");
            JOIN("PFR_POWER_OUT_KLDE PPOK ON PPOK.KLDE=HK.KLDE");
            WHERE("PPOK.POWER_OUTAGE_ID = #{id}");
        }}.toString();
    }
    public String getkallikratikosOTAListbyPowerOutage(@Param("language") String language){
        return new SQL(){{
            SELECT("HK.KLOT, HK.KLNO");
            if (LocalizationUTIL.ENGLISH.equals(language)){
                SELECT("HK.PERI_ENG AS PERI");
            } else {
                SELECT("HK.PERI");
            }
            FROM("H_KLOT HK");
            JOIN("PFR_POWER_OUT_KLOT PPOK ON PPOK.KLOT=HK.KLOT");
            WHERE("PPOK.POWER_OUTAGE_ID = #{id}");
        }}.toString();
    }

    public String getKallikratikiDhmotikiEnotita(@Param("language") String language){
        return new SQL(){{
            SELECT("HK.KLDE, HK.KLOT");
            if (LocalizationUTIL.ENGLISH.equals(language)){
                SELECT("HK.PERI_ENG AS PERI");
            } else {
                SELECT("HK.PERI");
            }
            FROM("H_KLDE HK");
            WHERE("HK.KLDE = #{klde}");
        }}.toString();
    }
}
