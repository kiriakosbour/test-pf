package gr.deddie.pfr.managers;

import gr.deddie.pfr.dao.DATASECURITYMapper;
import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.dao.PFRMapper;
import gr.deddie.pfr.model.GrafeioWithPostalCodes;
import gr.deddie.pfr.model.Grafeio;
import gr.deddie.pfr.model.UserDataRoleWithItems;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by M.Masikos on 5/1/2017.
 */
public class GRAFEIODataManager {
    private static Logger logger = LogManager.getLogger(GRAFEIODataManager.class);

    public List<GrafeioWithPostalCodes> getGrafeiaWithPostalCodes(String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getGrafeiaWithPostalCodes();
        } catch (Exception e) {
            logger.error("exception in getGrafeiaWithPostalCodes", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    // grafeia from table PFR_ACTIVE_GRAF
    public List<Grafeio> getGrafeiaActive(String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
        	DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getGrafeiaActive();
        } catch (Exception e) {
            logger.error("exception in getGrafeiaActive", e);
            throw e;
        } finally {
            sqlSession.close();            
        }
    }

    public List<Grafeio> getLoggedOnUserActiveGrafeia(String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getGrafeia();
        } catch (Exception e) {
            logger.error("exception in getLoggedOnUserActiveGrafeia", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public boolean isGrafeioActive(String graf) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.isGrafeioActive(graf);
        } catch (Exception e) {
            logger.error("exception in isGrafeioActive", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
