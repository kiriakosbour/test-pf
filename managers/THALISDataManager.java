package gr.deddie.pfr.managers;

import gr.deddie.pfr.dao.THALISMapper;
import gr.deddie.pfr.dao.ThalisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by M.Masikos on 16/5/2017.
 */
public class THALISDataManager {
    private static Logger logger = LogManager.getLogger(THALISDataManager.class);

    public Boolean isSupplyDisconnectedForDebts(String supply_no, String lastTenant) throws Exception {
        SqlSession sqlSession = ThalisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            THALISMapper thalisMapper = sqlSession.getMapper(THALISMapper.class);
            return thalisMapper.isSupplyDisconnectedForDebts(supply_no, lastTenant);
        } catch (Exception e) {
            logger.error("exception in isSupplyDisconnectedForDebts", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
