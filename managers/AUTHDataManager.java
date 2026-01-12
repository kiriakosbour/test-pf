package gr.deddie.pfr.managers;

import gr.deddie.pfr.dao.DATASECURITYMapper;
import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.dao.PFRMapper;
import gr.deddie.pfr.model.MenuItem;
import gr.deddie.pfr.model.UserRoleDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;



/**
 * Created by M.Masikos on 9/10/2016.
 */
public class AUTHDataManager {
    private static Logger logger = LogManager.getLogger(AUTHDataManager.class);

    public List<MenuItem> getMenuItems(String token, Integer appId) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        //SqlSession sqlSession2 = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        //Connection connection = sqlSession.getConnection();

        List<MenuItem> result = null;

        try {
//            ResultSet rs = connection.getMetaData().getClientInfoProperties();
//
//            ResultSetMetaData rsmd = rs.getMetaData();
//
//            int columnsNumber = rsmd.getColumnCount();
//            while (rs.next()) {
//                for (int i = 1; i <= columnsNumber; i++) {
//                    //if (i > 1) logger.warn(",  ");
//                    String columnValue = rs.getString(i);
//                    logger.warn("kolona" + columnValue + " " + rsmd.getColumnName(i));
//                }
//                logger.warn("");
//            }

//            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
//            DATASECURITYMapper datasecurityMapper2 = sqlSession2.getMapper(DATASECURITYMapper.class);
//            datasecurityMapper.setAppUserToken("a");
//            logger.info("A: the retrieved descriptions are: " + datasecurityMapper.getDescriptions());
//            sqlSession.commit();
//            logger.info("B: the retrieved descriptions are: " + datasecurityMapper2.getDescriptions());
//            sqlSession2.commit();
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
//            logger.info("select menu items according to authorization");
            result = pfrMapper.getMenuItems(token, appId);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getMenuItems", e);
        } finally {
            sqlSession.close();
            //sqlSession2.close();
        }

        return result;
    }

    public List<String> getUserRoles(String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        List<String> result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getUserRoles(token);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getUserRoles",e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public List<UserRoleDTO> getUserRoleDTOs(String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        List<UserRoleDTO> result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getUserRoleDTOs(token);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getUserRoles",e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public String getUser(String token) throws Exception {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getUser(token);
        } catch (Exception e) {
            logger.error("exception in getUser",e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public String getAM(String token) throws Exception {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getAM(token);
        } catch (Exception e) {
            logger.error("exception in getAM",e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
