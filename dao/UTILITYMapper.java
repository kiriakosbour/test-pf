package gr.deddie.pfr.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by M.Masikos on 11/5/2017.
 */
public interface UTILITYMapper {
    //-----------------------send email----------------------------------
    @Select("call P_MAIL.send_email(#{aFromEmailAddr},#{aToEmailAddr},#{aSubject},#{aBody},null,null,#{aCCEmailAddr})")
    public void sendEmail(@Param("aFromEmailAddr") String aFromEmailAddr, @Param("aToEmailAddr") String aToEmailAddr, @Param("aCCEmailAddr") String aCCEmailAddr, @Param("aSubject") String aSubject, @Param("aBody") String aBody, @Param("aBodySignature") String aBodySignature);
}
