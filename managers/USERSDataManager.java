package gr.deddie.pfr.managers;

import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.dao.PFRMapper;
import gr.deddie.pfr.dao.USERSMapper;
import gr.deddie.pfr.model.AssignmentDTO;
import gr.deddie.pfr.model.UserInfoDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by M.Masikos on 24/10/2016.
 */
public class USERSDataManager {
    private static Logger logger = LogManager.getLogger(USERSDataManager.class);

    public List<AssignmentDTO> getAssignments(Integer app_id) {
        logger.info("USERSDatamanager");
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            USERSMapper usersMapper = sqlSession.getMapper(USERSMapper.class);
            return usersMapper.getAssignments(app_id);
        } catch (Exception e) {
            logger.error("exception in getAssignments", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public Integer insertAssignment(AssignmentDTO assignmentDTO) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            USERSMapper usersMapper = sqlSession.getMapper(USERSMapper.class);
            logger.info("Inserting new record into table H_MONA_SEC_USERS");
            result = usersMapper.insertAssignment(assignmentDTO);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in insertAssignment", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public UserInfoDTO searchUser(String searchText) {
        logger.info("USERSDatamanager");
        UserInfoDTO result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            USERSMapper usersMapper = sqlSession.getMapper(USERSMapper.class);
            if (StringUtils.isNumeric(searchText)) {
                result = usersMapper.getUserInfoByAM(searchText);
            }
            else {
                result = usersMapper.getUserInfoByUsername(searchText);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in searchUser", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public int delinkUserDataRole(long user_id, long data_role_id) {
        logger.info("USERSDatamanager");
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            USERSMapper usersMapper = sqlSession.getMapper(USERSMapper.class);
            result = usersMapper.delinkUserDataRole(user_id,data_role_id);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in delinkUserDataRole", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public int linkUserDataRole(long user_id, long data_role_id) {
        logger.info("USERSDatamanager");
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            USERSMapper usersMapper = sqlSession.getMapper(USERSMapper.class);
            result = usersMapper.linkUserDataRole(user_id,data_role_id);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in linkUserDataRole", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }
}
