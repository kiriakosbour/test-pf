package gr.deddie.pfr.managers;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.dao.PFRMapper;
import gr.deddie.pfr.dao.POWERCUTREPORTMapper;
import gr.deddie.pfr.model.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class POWERCUTREPORTDataManager {
    private static Logger logger = LogManager.getLogger(POWERCUTREPORTDataManager.class);

    public List<KallikratikiNomarxia> getPerifereiakesEnotites() {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            POWERCUTREPORTMapper powercutreportMapper = sqlSession.getMapper(POWERCUTREPORTMapper.class);
            return powercutreportMapper.getPerifereiakesEnotites();
        } catch (Exception e) {
            logger.error("exception in getPerifereiakesEnotites query: ", e);
            return null;
        } finally {
            sqlSession.close();
        }

    }

    public List<KallikratikiNomarxia> getNomarxies(String language) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            POWERCUTREPORTMapper powercutreportMapper = sqlSession.getMapper(POWERCUTREPORTMapper.class);
            List<KallikratikiNomarxia> kallikratikiNomarxiaList = powercutreportMapper.getPerifereiakesEnotitesWithOldNomarxiesRelations(language)
                    .stream().filter(item->item.getOldNomarxia() == null).collect(Collectors.toList());
            List<OldNomarxia> oldNomarxiaList = powercutreportMapper.getOldNOmarxies(language);
            oldNomarxiaList.stream().map(item -> {
                KallikratikiNomarxia kallikratikiNomarxia = new KallikratikiNomarxia();
                kallikratikiNomarxia.setKlno(item.getOldno().toString());
                kallikratikiNomarxia.setPeri(item.getPeri());
                return kallikratikiNomarxia;
            }).collect(Collectors.toCollection(() -> kallikratikiNomarxiaList));

            return kallikratikiNomarxiaList;
        } catch (Exception e) {
            logger.error("Exception in getNomarxies: ", e);
            throw e;
        } finally {
            sqlSession.close();
        }

    }

    public List<MobileAppAnnouncementDTO> getAnnouncements(String version, MobileAppAnnouncement.DeviceOS os, String language) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getAnnouncements(version, os.toString(), language);
        } catch (Exception e) {
            logger.error("exception in getAnnouncements", e);
        } finally {
            sqlSession.close();
        }

        return null;
    }
}
