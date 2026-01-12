/**
 * Created by M.Masikos on 30/11/2016.
   Contributors: Kleopatra Konstanteli
 */

package gr.deddie.pfr.managers;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.deddie.pfr.dao.DATASECURITYMapper;
import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.model.DataRoleItem;
import gr.deddie.pfr.model.Otp;
import gr.deddie.pfr.model.SendSMSResponse;
import gr.deddie.pfr.model.UserDataRole;
import gr.deddie.pfr.model.UserDataRoleWithItems;
import gr.deddie.pfr.utilities.JacksonConfig;


public class SECURITYDataManager {
    private static Logger logger = LogManager.getLogger(SECURITYDataManager.class);
    private final String failure_announcement_sms_txt = "ΤΟ PIN ΓΙΑ ΤΗΝ ΚΑΤΑΧΩΡΙΣΗ ΒΛΑΒΗΣ ΕΙΝΑΙ %s. ΙΣΧΥΕΙ ΜΕΧΡΙ ΤΗΝ %s";
    private final String failure_recall_sms_txt = "ΤΟ PIN ΓΙΑ ΤΗΝ ΑΝΑΚΛΗΣΗ ΕΙΝΑΙ %s. ΙΣΧΥΕΙ ΜΕΧΡΙ ΤΗΝ %s";
    private final String failure_announcement_sms_txt_in_english = "PIN FOR FAULT REPORT IS %s. IT IS VALID UNTIL %s";
    private final String failure_recall_sms_txt_in_english = "PIN FOR REVOCATION REQUEST IS %s. VALID UNTIL %s";

    public List<UserDataRole> getDataRoles() {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            return datasecurityMapper.getDataRoles();
        } catch (Exception e) {
            logger.error("exception in getDataRoles", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public List<UserDataRoleWithItems> getDataRolesWithItems() {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            return datasecurityMapper.getUserDataRolesWithItems();
        } catch (Exception e) {
            logger.error("exception in getDataRolesWithItems", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public List<DataRoleItem> getDataRoleItems(long data_role_id) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            return datasecurityMapper.getDataRoleItems(data_role_id);
        } catch (Exception e) {
            logger.error("exception in getDataRoleItem", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public int addDataRoleItem(DataRoleItem dataRoleItem) {
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            result = datasecurityMapper.addDataRoleItem(dataRoleItem);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in addDataRoleItem", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public int deleteDataRoleItem(DataRoleItem dataRoleItem) {
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            result = datasecurityMapper.deleteDataRoleItem(dataRoleItem);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in deleteDataRoleItem", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public int updateDataRoleItem(DataRoleItem dataRoleItem) {
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            result = datasecurityMapper.updateDataRoleItem(dataRoleItem);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in updateDataRoleItem", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public int addDataRole(UserDataRole userDataRole) {
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            result = datasecurityMapper.addDataRole(userDataRole);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in addDataRole", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public int updateDataRole(UserDataRole userDataRole) {
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            result = datasecurityMapper.updateDataRole(userDataRole);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in updateDataRole", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public int deleteDataRole(UserDataRole userDataRole) throws Exception {
        int result = 0;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            result = datasecurityMapper.deleteDataRoleItems(userDataRole);
            if (result != 1 ) {
                throw new Exception("CascadeDeleteFailed");
            }
            result = datasecurityMapper.deleteDataRole(userDataRole);
            sqlSession.commit();
        }
        catch (PersistenceException e) {
            sqlSession.rollback();
            logger.error("exception in deleteDataRole", e);
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw (SQLIntegrityConstraintViolationException) e.getCause();
            }
            else {
                throw e;
            }
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public Otp sendOTP(Otp otp, String language) {
        // stores the existing OTP for the specific cell, if any
        Otp activeOtp = null;
        // serializable locks the table
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession(TransactionIsolationLevel.SERIALIZABLE);
        JacksonConfig jacksonConfig = new JacksonConfig();

        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            activeOtp = datasecurityMapper.getAnyActiveOtp(otp, Otp.VALIDITY_PERIOD_IN_SECONDS);
            if (activeOtp == null) {
                datasecurityMapper.addOtp(otp);
                Otp newOtp = datasecurityMapper.getOtp(otp);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newOtp.getCreated());
                calendar.add(Calendar.SECOND, Otp.VALIDITY_PERIOD_IN_SECONDS);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String message = null;
                if (newOtp.getType().getId() == Otp.OtpType.FAILURE_ANNOUNCEMENT.getId()) {
                    message = LocalizationUTIL.ENGLISH.equals(language) ? failure_announcement_sms_txt_in_english : failure_announcement_sms_txt;
                } else if (newOtp.getType().getId() == Otp.OtpType.FAILURE_RECALL.getId()) {
                    message = LocalizationUTIL.ENGLISH.equals(language) ? failure_recall_sms_txt_in_english : failure_recall_sms_txt;
                } else {
                    throw new Exception("otp.type.is.invalid");
                }
                String response = datasecurityMapper.sendSMS(newOtp.getCell(), String.format(message, newOtp.getPin(), simpleDateFormat.format(calendar.getTime())));
                ObjectMapper objectMapper = jacksonConfig.getContext(SendSMSResponse.class);
                SendSMSResponse sendSMSResponse = objectMapper.readValue(response, SendSMSResponse.class);
                if (sendSMSResponse.getError() != null) throw new Exception(sendSMSResponse.getError());
            }
            sqlSession.commit();
        } catch (Throwable t) {
            sqlSession.rollback();
            logger.error("Exception in sendOTP", t);
            return null;
        } finally {
            sqlSession.close();
        }
        return (activeOtp != null ? activeOtp : otp);
    }

    public int sendSMS(String cell, String text) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.sendSMS(cell, text);
            sqlSession.commit();
        } catch (Throwable t) {
            sqlSession.rollback();
            logger.error("Exception in sendSMS", t);
            return -1;
        } finally {
            sqlSession.close();
        }
        return 1;
    }

    public Otp getOtpOnlyIfActive(Otp otp) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            return datasecurityMapper.getOtpOnlyIfActive(otp, Otp.VALIDITY_PERIOD_IN_SECONDS);
        } catch (Exception e) {
            logger.error("Exception in getOtp", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }
}
