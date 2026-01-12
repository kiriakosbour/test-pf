package gr.deddie.pfr.managers;

import gr.deddie.pfr.dao.ERMINFOMapper;
import gr.deddie.pfr.dao.ErminfoConnectionFactory;
import gr.deddie.pfr.model.Supply;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by M.Masikos on 24/4/2017.
 */
public class ERMINFODataManager {
    private static Logger logger = LogManager.getLogger(ERMINFODataManager.class);

    public Supply getSupply(String supply_no) throws Exception {
        SqlSession sqlSession = ErminfoConnectionFactory.getSqlSessionFactory().openSession();
        try {
            ERMINFOMapper erminfoMapper = sqlSession.getMapper(ERMINFOMapper.class);
            return erminfoMapper.getSupply(supply_no);
        } catch (Exception e) {
            logger.error("exception in getSupply", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public String getSupplysLastTenantName(String supply_no_full) throws Exception {
        SqlSession sqlSession = ErminfoConnectionFactory.getSqlSessionFactory().openSession();
        try {
            ERMINFOMapper erminfoMapper = sqlSession.getMapper(ERMINFOMapper.class);
            return erminfoMapper.getSupplysLastTenantName(supply_no_full);
        } catch (Exception e) {
            logger.error("exception in getSupplysLastTenantName", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
