/*
 * Created by M.Masikos on 21/6/2016.
   Contributors: Kleopatra Konstanteli
 */

package gr.deddie.pfr.managers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.naming.AuthenticationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import gr.deddie.pfr.dao.*;
import gr.deddie.pfr.model.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;
import gr.deddie.pfr.services.FailureService;
import gr.deddie.pfr.utilities.RestClient;


public class PFRDataManager {
    private static Logger logger = LogManager.getLogger(PFRDataManager.class);
    private static SECURITYDataManager securityDataManager;
    private static final String url = "http://10.13.49.113:7001/AppWS/OMServices/sendPushByAM";
    private static final String assignmentMessage = "Σας έχει ανατεθεί η βλάβη με αριθμό %d";
    private static final String[] handheldNotificationResponses = {"NO_PERMISSIONS","SUCCESS","ERROR","NOT_FOUND_ACTIVE_DEVICE"};

    public Integer deleteFailureType(FailureType ft) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            result = pfrMapper.deleteFailureType(ft);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in deleteFailureType", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer deleteMessage(Message m) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            result = pfrMapper.deleteMessage(m);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in deleteMessage", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer updateFailureType(FailureType ft) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            result = pfrMapper.updateFailureType(ft);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in updateFailureType", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer updateMessage(Message m) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            result = pfrMapper.updateMessage(m);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in updateMessage", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer alterFailuresGeneralFlag(List<FailureDTO> failureDTOS, Boolean generalFlag) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            result = pfrMapper.alterFailuresGeneralFlag(failureDTOS, generalFlag);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in alterFailuresGeneralFlag", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer insertFailureType(FailureType ft) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            result = pfrMapper.insertFailureType(ft);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in insertFailureType", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer insertMessage(Message m) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            result = pfrMapper.insertMessage(m);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in insertMessage", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public FailureDTOsLimitedList getFailureDTOsByCriteria(FailureSearchParameters searchParameters, String token) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            List<FailureDTO> failureDTOs = pfrMapper.getFailureDTOsByCriteriaByVPD(searchParameters.getAnnouncedFrom(),
                    searchParameters.getAnnouncedTo(), searchParameters.getFailure_type_id(), searchParameters.getFailure_extent(),
                    searchParameters.getStatus(), null, FailureDTOsLimitedList.FAILURES_LIST_MAX_SIZE, null,
                    searchParameters.getGrafeio().getGraf());

            FailureDTOsLimitedList failureDTOsLimitedList = new FailureDTOsLimitedList();
            failureDTOsLimitedList.setPartial_response(failureDTOs.size() > FailureDTOsLimitedList.FAILURES_LIST_MAX_SIZE ? true : false);

            if (failureDTOs.size() > FailureDTOsLimitedList.FAILURES_LIST_MAX_SIZE) {
                failureDTOs.remove(FailureDTOsLimitedList.FAILURES_LIST_MAX_SIZE);
            }

            failureDTOsLimitedList.setAnnouncements(failureDTOs);

            sqlSession.commit();
            return failureDTOsLimitedList;
        } catch (Exception e) {
            logger.error("exception in getFailureDTOsByCriteria", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public List<FailureDTO> getFailuresBookContent(FailuresBookSearchParameters searchParameters, String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            String[] tokens = searchParameters.getMonthyear().split("/");
            List<FailureDTO> failureDTOList = pfrMapper.getFailuresBookContentbyVPD(searchParameters.getGrafeioList(),
                    tokens[0], tokens[1]);

            sqlSession.commit();
            return failureDTOList;
        } catch (Exception e) {
            logger.error("exception in getFailuresBookContent", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public List<Message> getMessages() {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getMessages();
        } catch (Exception e) {
            logger.error("exception in getMessages", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public Message getMessage(Integer id) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getMessage(id);
        } catch (Exception e) {
            logger.error("exception in getMessage", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public SabInfoType getSabInfoType(String code) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getSabInfoType(code);
        } catch (Exception e) {
            logger.error("exception in getSabInfoType", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public GeneralFailureGroup getGeneralFailureGroup(Long id) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getGeneralFailureGroupbyId(id);
        } catch (Exception e) {
            logger.error("exception in getGeneralFailureGroupbyId", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public GeneralFailureGroup getGeneralFailureGroup(String label) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getGeneralFailureGroupbyLabel(label.toUpperCase());
        } catch (Exception e) {
            logger.error("exception in getGeneralFailureGroupbyLabel", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public GeneralFailureGroup addGeneralFailureGroup(GeneralFailureGroup generalFailureGroup) throws Exception {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            pfrMapper.addGeneralFailureGroup(generalFailureGroup);

            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in addGeneralFailureGroup", e);
            throw e;
        } finally {
            sqlSession.close();
        }

        return generalFailureGroup;
    }

    public Grafeio getGrafeio(String graf, String token) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getGrafeio(graf);
        } catch (PersistenceException e) {
            logger.error("persistence exception in getGrafeio", e);
            throw e;
        } catch (Exception e) {
            logger.error("exception in getGrafeio", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public List<FailureDTO> getFailureDTOsByIDs(List<Long> ids) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            Long ids_array[] = new Long[ids.size()];

            if (ids_array.length != 0) {
                for (int i = 0; i < ids_array.length; i++) {
                    ids_array[i] = ids.get(i);
                }
            }
            return pfrMapper.getFailureDTOsByIDs(ids_array);
        } catch (Exception e) {
            logger.error("exception in getFailureDTOsByIDs", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public FailureDTOsLimitedList getFailureDTOsLimitedList() {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            List<FailureDTO> failureDTOs = pfrMapper.getFailureDTOsLimited();

            FailureDTOsLimitedList failureDTOsLimitedList = new FailureDTOsLimitedList();
            failureDTOsLimitedList.setPartial_response(failureDTOs.size() > 2000 ? true : false);

            if (failureDTOs.size() > 2000) {
                failureDTOs.remove(failureDTOs.size() - 1);
            }

            failureDTOsLimitedList.setAnnouncements(failureDTOs);

            return failureDTOsLimitedList;
        } catch (Exception e) {
            logger.error("exception in getFailureDTOsLimitedList", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public List<FailureDTO> getFailureDTOs() {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getFailureDTOs();
        } catch (Exception e) {
            logger.error("exception in getFailureDTOs", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public List<FailureDTO> getActiveRestFailureDTOsByGraf(String token) {

        List<FailureDTO> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getActiveRestFailuresDTOsByGraf(FailureType.DANGEROUS_STATE_ID,
                    Failure.FailureStatus.ANNOUNCED.toString(), Failure.FailureStatus.IN_PROGRESS.toString());
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveRestFailureDTOsByGraf", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<FailureDTO> getFailureDTOsByStatusByDateByGraf(String token, Failure.FailureStatus status, Date fromTimestamp) {

        List<FailureDTO> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getFailureDTOsByCriteriaByVPD(null,null,null,null, status.toString(), fromTimestamp, null,null,null);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getRecalledFailureDTOsByGraf", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<FailureDTO> getActiveFailureDTOsBySupplyNoOrPostalCode(String token, String supply_no, String postal_code) {

        List<FailureDTO> result = null;

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getActiveFailureDTOsBySupplyNoOrPostalCode(supply_no, postal_code, Failure.FailureStatus.ANNOUNCED.toString(),
                    Failure.FailureStatus.IN_PROGRESS.toString());
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveFailureDTOsBySupplyNoOrPostalCode", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<Failure> getSupplysActiveFailures(String supply_no) {

        List<Failure> result = null;

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getSupplysActiveFailuresIVR(supply_no, Failure.FailureStatus.ANNOUNCED.toString(),
                    Failure.FailureStatus.IN_PROGRESS.toString());
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveFailureDTOsBySupplyNo", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<Grafeio> getResponsibleGrafsperPostalCode(String token, String postal_code, String language) {

        List<Grafeio> result = null;

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getResponsibleGrafsperPostalCode(postal_code, language);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getResponsibleGrafsperPostalCode", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<DangerousStateDTO> getActiveDangerousStatesByGraf(String token) {

        List<DangerousStateDTO> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getActiveDangerousStatesByGraf(FailureType.DANGEROUS_STATE_ID, Failure.FailureStatus.ANNOUNCED.toString(), Failure.FailureStatus.IN_PROGRESS.toString());
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveDangerousStatesByGraf", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public List<FailureDTO> getActiveGeneralFailuresByPostalCodeByGraf(String token, String postal_code) {

        List<FailureDTO> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getActiveFailureDTOsByCriteriaByVPD(null, null, null,true,
                    null, FailureDTOsLimitedList.FAILURES_LIST_MAX_SIZE, postal_code,
                    Failure.FailureStatus.ANNOUNCED.toString(), Failure.FailureStatus.IN_PROGRESS.toString());
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveGeneralFailuresByPostalCodeByGraf", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<FailureDTO> getActiveGeneralFailuresByVPD(String token) {

        List<FailureDTO> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getActiveFailureDTOsByCriteriaByVPD(null, null, null,true,
                    null, null, null,
                    Failure.FailureStatus.ANNOUNCED.toString(), Failure.FailureStatus.IN_PROGRESS.toString());
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveGeneralFailuresByVPD", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<FailureDTO> getActiveGeneralFailuresByGroupID(Long groupId) {

        List<FailureDTO> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getActiveGeneralFailuresByGroupID(groupId);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveGeneralFailuresByVPD", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<GeneralFailureGroup> getActiveGeneralFailuresGroupsByVPD(String token) {

        List<GeneralFailureGroup> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getActiveGeneralFailuresGroupsByGraf(Failure.FailureStatus.ANNOUNCED.toString(),
                    Failure.FailureStatus.IN_PROGRESS.toString());
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getActiveGeneralFailuresGroupsByVPD", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public Failure getFailure(Long failure_id, String token) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getFailure(failure_id);
        } catch (Exception e) {
            logger.error("exception in getFailure", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public String getUser(String token) throws Exception{

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            String username = pfrMapper.getUser(token);
            if (username == null) {
                throw new AuthenticationException("no.active.session.found");
            }
            return username;
        } catch (AuthenticationException e) {
            logger.error("no.active.session.found", e);
            throw e;
        } catch (Exception e) {
            logger.error("exception in getUser", e);
            throw new Exception("exception in getUser", e);
        } finally {
            sqlSession.close();
        }
    }

    public NetworkHazardPhoto getPhoto(long file_id) throws Exception{

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            return pfrMapper.getPhoto(file_id);
        } catch (Exception e) {
            logger.error("exception in getPhoto", e);
            throw new Exception("exception in getPhoto", e);
        } finally {
            sqlSession.close();
        }
    }

    public List<FailureHistory> getFailureHistory(String failure_id) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getFailureHistory(failure_id);
        } catch (Exception e) {
            logger.error("exception in getFailureHistory", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public Long insertFailure(Failure f, String user) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);

            // fill the status field in failure object based on failure type, transient MV faults are considered as recovered at time of registration
            if (f.getFailureType().getId() == FailureType.TRANSIENT_MV_FAULT_ID) {
                f.setStatus(Failure.FailureStatus.RESOLVED);
            } else {
                f.setStatus(Failure.FailureStatus.ANNOUNCED);
            }

            //fill the grafeio assignment
            f.setGrafeio(f.getAssignments().get(0).getAssignee());

            //insert failure
            result = pfrMapper.insertFailure(f);

            String contact_method = FailureHistory.ContactMethod.no_contact.toString();
            if ((f.getEmail() != null) && (f.getEmail() != "") && (f.getFailureType().getId() != FailureType.TRANSIENT_MV_FAULT_ID)) {
                contact_method = FailureHistory.ContactMethod.email.toString();
            }
            if ((f.getInput_channel() == Failure.InputChannel.WEBSITE) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_ANDROID) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_IOS)) {
                contact_method = FailureHistory.ContactMethod.sms.toString();
            }

            //insert failure history
            FailureHistory fh = new FailureHistory(f.getId(), (contact_method != FailureHistory.ContactMethod.no_contact.toString() ? Message.FAILURE_ANNOUNCED_MESSAGE_ID : null),
                    contact_method, Failure.FailureStatus.ANNOUNCED.toString(), null, user);
            result = pfrMapper.insertFailureHistory(fh);

            // in case of transient MV faults the fault is considered as recovered at time of registration
            if (f.getFailureType().getId() == FailureType.TRANSIENT_MV_FAULT_ID) {
                fh = new FailureHistory(f.getId(), (f.getEmail() == null || f.getEmail() == "" ? null : 1),FailureHistory.ContactMethod.no_contact.toString(),
                        Failure.FailureStatus.RESOLVED.toString(), null, user);
                result = pfrMapper.insertFailureHistory(fh);
            }

            //insert failure questions
            List<Question> questionsList = f.getQuestions();
            for (Iterator<Question> questionIterator = questionsList.iterator(); questionIterator.hasNext(); ) {
                Question question = questionIterator.next();
                question.setFailure_id(f.getId());
                result = pfrMapper.insertFailureQuestion(question);
            }

            //insert failure landmarks and coordinates
            List<Landmark> landmarkList = f.getLandmarks();
            for (Iterator<Landmark> landmarkIterator = landmarkList.iterator(); landmarkIterator.hasNext(); ) {
                Landmark landmark = landmarkIterator.next();
                landmark.setFailure_id(f.getId());
                result = pfrMapper.insertFailureLandmark(landmark);
                for (Iterator<Point> pointIterator = landmark.getPointsList().iterator(); pointIterator.hasNext(); ) {
                    Point point = pointIterator.next();
                    point.setLandmark_id(landmark.getId());
                    result = pfrMapper.insertLandmarkPoint(point);
                }
            }

            //insert failure assignment
            FailureAssignment failureAssignment = f.getAssignments().get(0);
            failureAssignment.setFailure_id(f.getId());
            result = pfrMapper.insertFailureAssignment(failureAssignment);

            //invalidate otp in case of failures reported in website's relevant page or in mobile apps
            if ((f.getInput_channel() == Failure.InputChannel.WEBSITE) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_IOS) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_ANDROID)) {
                result = datasecurityMapper.invalidateOtp(f.getOtp());
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in insertFailure", e);
            result = -1;
        } finally {
            sqlSession.close();
        }

        return (result == -1 ? -1L : f.getId());
    }

    public Integer updateFailures(List<Failure> failures, String token) throws Exception{
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        securityDataManager = new SECURITYDataManager();

        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            //retrieve user based on token
            String user = pfrMapper.getUser(token);

            //retrieve project settings
            Settings settings = pfrMapper.getSettings();

            for (Failure f : failures) {
                List<FailureAudit> failureAuditList = new ArrayList<>();

                Failure oldFailure = getFailure(f.getId(), token);
                if (oldFailure == null) {
                    throw new Exception("updating.not.existing.failure");
                }

                // generate object with differences in power outage audit
                DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(f, oldFailure);

                FailureHistory latestFailureHistory = f.getEvents().stream().max(Comparator.comparing(FailureHistory::getId)).get();
                Message message = null;
                String contact_method = FailureHistory.ContactMethod.no_contact.toString();
                String comments = latestFailureHistory.getComments();
                String status = latestFailureHistory.getStatus();
                // final restoration time is allowed only in case the failure status is "resolved"
                if ((status != null) && (!status.equals(Failure.FailureStatus.RESOLVED.toString()))) {
                    f.setFinal_restoration_time(null);
                }
                // if no texniths is defined, then texniths_assignment_timestamp is set to null
                if ((f.getAssigned_texniths() == null) && (f.getAssigned_texniths_contractor() == null)) {
                    f.setTexniths_assignment_timestamp(null);
                }

                // generate audits for changes in the failure object
                if (diffNode.hasChanges()) {
                    diffNode.visit(new DiffNode.Visitor()
                    {
                        @Override
                        public void node(final DiffNode node, final Visit visit)
                        {
                            //logger.info(node.getPath().toString() + "," + node.getPropertyName());
                            if (!node.isRootNode() && !node.hasChildren() && Arrays.stream(Failure.auditedFields).anyMatch(node.getPath().toString()::equals)) {
                                //logger.info("property: " + node.getPath().toString() + ", action: " + node.getState().toString() + ", newValue: " + node.canonicalGet(f));
                                FailureAudit failureAudit = new FailureAudit(node.getPath().toString(),node.getState().toString(),node.canonicalGet(f) == null ? null : node.canonicalGet(f).toString(), user, f.getId());
                                failureAuditList.add(failureAudit);
                                //logger.info("failureAudit:" + failureAudit.toString());

                            }
                        }
                    });
                }

                if ((failureAuditList.size() == 0) && (status == null)) {
                    throw new Exception("no.changes.found.in.failure." + f.getId().toString());
                }

                // persist changes in failure and audits
                if (failureAuditList.size() > 0) {
                    pfrMapper.insertFailureAudits(failureAuditList);
                    pfrMapper.updateFailure(f);
                }

                // send notification to handheld devices in case of new assignment
                Optional<FailureAudit> assignedTexnithsAudit = failureAuditList.stream()
                        .filter(audit -> audit.getProperty().equals(Failure.auditedFields[0]) || audit.getProperty().equals(Failure.auditedFields[5]))
                        .findAny();

                if (assignedTexnithsAudit.isPresent()) {
                    Map<String, Object> parametersMap = new HashMap<>();
                    parametersMap.put("token", "");
                    parametersMap.put("am", assignedTexnithsAudit.get().getNew_value());
                    parametersMap.put("sxol", String.format(assignmentMessage, f.getId()));
                    parametersMap.put("audit_flg", settings.getHandheld_notific_audit_flg());
                    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
                    headers.putSingle("token", token);
                    String apantisi = RestClient.doGet(settings.getHandheld_notific_endpoint(), parametersMap, headers, new GenericType<String>() {});
                    if (!apantisi.equals(handheldNotificationResponses[1])) {
                        logger.error("Handheld notification for failure assignment endpoint error: " + apantisi);
                    }
                }

                // set contact_method and message_id, send message
                if ((status != null) && (oldFailure.getFailureType().getId() != FailureType.DANGEROUS_STATE_ID) && ((oldFailure.getEmail() != null) || (oldFailure.getCell() != null))) {

                    // when we set failure status IN_PROGRESS
                    // if failure is not dangerous state in network, estimated restore time has changed and its value is not null, status is IN_PROGRESS and cell is not null
                    // send sms (#770)
                    if (status.equals(Failure.FailureStatus.IN_PROGRESS.toString())) {
                        // check if estimated restore time has changed
                        Optional<FailureAudit> estimatedRestoreTimeAudit = failureAuditList.stream()
                                .filter(audit -> audit.getProperty().equals(Failure.auditedFields[1]))
                                .findAny();
                        Calendar estimatedRestoreTime = Calendar.getInstance();
                        if (f.getEstimated_restore_time() != null) {
                            estimatedRestoreTime.setTime(f.getEstimated_restore_time());
                        }
                        if (estimatedRestoreTimeAudit.isPresent() && (estimatedRestoreTimeAudit.get().getNew_value() != null)
                                && (Calendar.getInstance().before(estimatedRestoreTime)) && (oldFailure.getCell() != null)) {
                            contact_method = FailureHistory.ContactMethod.sms.toString();
                            message = pfrMapper.getMessage(Message.FAILURE_ESTIMATED_RESTORE_TIME_MESSAGE_ID);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm", new Locale("el"));
                            message.setMessage_description(String.format(message.getMessage_description(), simpleDateFormat.format(f.getEstimated_restore_time())));
                        }
                    }

                    // when we set failure status RESOLVED
                    // if failure is not dangerous state in network, estimated restore time is not null, contacts are not null, previous status is IN_PROGRESS and estimated_restore_time + 5hours > now()
                    // send message (#813)
                    if (status.equals(Failure.FailureStatus.RESOLVED.toString()) && (oldFailure.getEstimated_restore_time() != null)) {
                        Calendar extendedEstimatedRestoreTime = Calendar.getInstance();
                        extendedEstimatedRestoreTime.setTime(oldFailure.getEstimated_restore_time());
                        extendedEstimatedRestoreTime.add(Calendar.HOUR, 5);
                        FailureHistory oldFailureHistory = oldFailure.getEvents().stream().max(Comparator.comparing(FailureHistory::getId)).get();

                        if (oldFailureHistory.equals(Failure.FailureStatus.IN_PROGRESS.toString())
                                && (Calendar.getInstance().before(extendedEstimatedRestoreTime))) {
                            if (oldFailure.getCell() != null) {
                                contact_method = FailureHistory.ContactMethod.sms.toString();
                                message = pfrMapper.getMessage(Message.FAILURE_RECOVERED_SMS_MESSAGE_ID);
                            } else if (oldFailure.getEmail() != null) {
                                contact_method = FailureHistory.ContactMethod.email.toString();
                                message = pfrMapper.getMessage(Message.FAILURE_RECOVERED_MESSAGE_ID);
                            }
                        }
                    }

                }

                //persist new event
                if (status != null) {
                    pfrMapper.insertFailureHistory(new FailureHistory(f.getId(), message != null ? message.getMessage_id() : null, contact_method, status, comments, user));
                    // fill status field in failure object
                    f.setStatus(Failure.FailureStatus.valueOf(status));
                    pfrMapper.updateFailureStatus(f);
                }

                // send message
                // message should leave after the event is persisted
                if (contact_method == FailureHistory.ContactMethod.sms.toString()) {
                    securityDataManager.sendSMS(oldFailure.getCell(), message.getMessage_description());
                } else if (contact_method == FailureHistory.ContactMethod.email.toString()) {
                    FailureService failureService = new FailureService();
                    failureService.sendEmailviaOracleProc(oldFailure.getEmail(), null, "Αρ. Αιτήματος " + oldFailure.getId().toString() + ": " + message.getMessage_header(), message.getMessage_description());
                }
            }

            //persist sab
            if (failures.get(0).getSab_MT() != null) {
                failures.get(0).getSab_MT().setVltype(pfrMapper.getSabInfoType(failures.get(0).getSab_MT().getVlkind()).getTypedes());

                pfrMapper.insertSABMT(failures.get(0).getSab_MT());
            } else if (failures.get(0).getSab_XT() != null) {
                failures.get(0).getSab_XT().setVltype(pfrMapper.getSabInfoType(failures.get(0).getSab_XT().getVlkind()).getTypedes());

                pfrMapper.insertSABXT(failures.get(0).getSab_XT());
            } else if (failures.get(0).getSab_Paroxis() != null) {
                failures.get(0).getSab_Paroxis().setVltype(pfrMapper.getSabInfoType(failures.get(0).getSab_Paroxis().getVlkind()).getTypedes());

                pfrMapper.insertSABParoxis(failures.get(0).getSab_Paroxis());
            } else if (failures.get(0).getSab_Diafora() != null) {
                failures.get(0).getSab_Diafora().setVltype(pfrMapper.getSabInfoType(failures.get(0).getSab_Diafora().getVlkind()).getTypedes());

                pfrMapper.insertSABDiafora(failures.get(0).getSab_Diafora());
            }

            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in updateFailures", e);
            throw e;
        } finally {
            sqlSession.close();
        }

        return 1;
    }

    public Integer insertFailureHistory(FailureHistory fh) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.updateFailureStatus2(fh);
            result = pfrMapper.insertFailureHistory(fh);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in insertFailureHistory", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer markFailureAsRecalledByUser(FailureHistory failureHistory, Failure failure) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            pfrMapper.insertFailureHistory(failureHistory);
            pfrMapper.updateFailureStatus2(failureHistory);

            if ((failure.getInput_channel() == Failure.InputChannel.WEBSITE) || (failure.getInput_channel() == Failure.InputChannel.MOBILE_APP_ANDROID) || (failure.getInput_channel() == Failure.InputChannel.MOBILE_APP_IOS)) {
                datasecurityMapper.invalidateOtp(failure.getOtp());
            }

            sqlSession.commit();
        } catch (Exception e) {
            logger.error("Exception in markFailureAsRecalledByUser: ", e);
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }

        return 1;
    }

    public Integer insertFailureAssignment(FailureAssignment failureAssignment, String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.deleteGeneralFailureGroup(failureAssignment.getFailure_id());

            result = pfrMapper.updateFailureAssignment(failureAssignment); // update assignment in pfr_failures table
            result = pfrMapper.insertFailureAssignment(failureAssignment);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in insertFailureAssignment", e);
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public Integer updateFailuresGroup(List<FailureDTO> failureDTOS, GeneralFailureGroup generalFailureGroup, String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result;

        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            //retrieve user based on token
            String user = pfrMapper.getUser(token);

            // system updates the last update info of the new members of the group
            if (generalFailureGroup != null) {
                List<FailureDTO> groupExistingFailures = pfrMapper.getActiveFailureDTOsByCriteria(null, null, null, null, null,
                        generalFailureGroup.getId(), null, null, Failure.FailureStatus.ANNOUNCED.toString(), Failure.FailureStatus.IN_PROGRESS.toString());

                // the last update info of the existing members of the group is used
                if (groupExistingFailures.size() > 0) {
                    failureDTOS.stream().forEach((f) -> {
                        List<FailureAudit> failureAuditList = new ArrayList<>();

                        FailureHistory failureHistory = new FailureHistory(f.getId(), null, "no_contact", groupExistingFailures.get(0).getStatus(), groupExistingFailures.get(0).getLast_update_comments(), "system");
                        pfrMapper.insertFailureHistory(failureHistory);
                        pfrMapper.updateFailureStatus2(failureHistory);
                        Failure failure = new Failure();
                        failure.setId(f.getId());
                        failure.setAssigned_texniths(groupExistingFailures.get(0).getAssigned_texniths());
                        failure.setAssigned_texniths_contractor(groupExistingFailures.get(0).getAssigned_texniths_contractor());
                        failure.setTexniths_assignment_timestamp(groupExistingFailures.get(0).getTexniths_assignment_timestamp());
                        failure.setEstimated_restore_time(groupExistingFailures.get(0).getEstimated_restore_time());
                        failure.setFinal_restoration_time(groupExistingFailures.get(0).getFinal_restoration_time());
                        failure.setGeneral_failure_group(groupExistingFailures.get(0).getGeneral_failure_group());
                        Failure oldFailure = pfrMapper.getFailure(f.getId());
                        DiffNode diffNode = ObjectDifferBuilder.buildDefault().compare(failure, oldFailure);
                        // generate audits for changes in the failure object
                        if (diffNode.hasChanges()) {
                            diffNode.visit(new DiffNode.Visitor()
                            {
                                @Override
                                public void node(final DiffNode node, final Visit visit)
                                {
                                    //logger.info(node.getPath().toString() + "," + node.getPropertyName());
                                    if (!node.isRootNode() && !node.hasChildren() && Arrays.stream(Failure.auditedFields).anyMatch(node.getPath().toString()::equals)) {
                                        //logger.info("property: " + node.getPath().toString() + ", action: " + node.getState().toString() + ", newValue: " + node.canonicalGet(f));
                                        FailureAudit failureAudit = new FailureAudit(node.getPath().toString(),node.getState().toString(),node.canonicalGet(failure) == null ? null : node.canonicalGet(failure).toString(), user, oldFailure.getId());
                                        failureAuditList.add(failureAudit);
                                        //logger.info("failureAudit:" + failureAudit.toString());

                                    }
                                }
                            });
                        }

                        // persist changes in failure and audits
                        if (failureAuditList.size() > 0) {
                            pfrMapper.insertFailureAudits(failureAuditList);
                            pfrMapper.updateFailure(failure);
                        }
                    });
                }
                // custom info is used in case no other members exist in the group
                else {
                    // retrieve failureDTOS data from dB
                    List<FailureDTO> retrievedFailureDTOs = pfrMapper.getFailureDTOsByIDs(failureDTOS.stream().map(failureDTO -> failureDTO.getId()).toArray(Long[]::new));
                    Failure.FailureStatus status = (retrievedFailureDTOs.stream().anyMatch(failureDTO -> failureDTO.getStatus() == Failure.FailureStatus.IN_PROGRESS.toString()) ? Failure.FailureStatus.IN_PROGRESS : Failure.FailureStatus.ANNOUNCED);
                    failureDTOS.stream().forEach((f) -> {
                        FailureHistory failureHistory = new FailureHistory(f.getId(), null, "no_contact", status.toString(), "Αρχικοποίηση ομάδας γενικών διακοπών με id " + generalFailureGroup.getId().toString(), "system");
                        pfrMapper.insertFailureHistory(failureHistory);
                        pfrMapper.updateFailureStatus2(failureHistory);
                    });
                }
            }
            // add an event for dissolving a group
            else {
                // retrieve failureDTOS data from dB
                List<FailureDTO> retrievedFailureDTOs = pfrMapper.getFailureDTOsByIDs(failureDTOS.stream().map(failureDTO -> failureDTO.getId()).toArray(Long[]::new));
                Failure.FailureStatus status = (retrievedFailureDTOs.stream().anyMatch(failureDTO -> failureDTO.getStatus() == Failure.FailureStatus.IN_PROGRESS.toString()) ? Failure.FailureStatus.IN_PROGRESS : Failure.FailureStatus.ANNOUNCED);
                failureDTOS.stream().forEach((f) -> {
                    FailureHistory failureHistory = new FailureHistory(f.getId(), null, "no_contact", status.toString(), "Αφαίρεση από την ομάδα γενικών διακοπών με id " + retrievedFailureDTOs.get(0).getGeneral_failure_group().getId().toString(), "system");
                    pfrMapper.insertFailureHistory(failureHistory);
                    pfrMapper.updateFailureStatus2(failureHistory);
                });
            }

            result = pfrMapper.updateFailuresGroup(failureDTOS, generalFailureGroup);

            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in updateFailuresGroup", e);
            sqlSession.rollback();
            result = null;
        } finally {
            sqlSession.close();
        }

        return result;
    }

    public List<FailureType> getFailureTypes() {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getFailureTypes();
        } catch (Exception e) {
            logger.error("exception in getFailureTypes", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public List<Failure> getFailuresAnnouncements() {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getFailuresAnnouncements();
        } catch (Exception e) {
            logger.error("exception in getFailuresAnnouncements", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public UserLoginInfo authenticate(String username, String password) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.authenticate(username, password);
        } catch (Exception e) {
            logger.error("exception in authenticate", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public List<Failure> getActiveFailuresWithCoords(String token) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getActiveFailuresWithCoords(Failure.FailureStatus.ANNOUNCED.toString(), Failure.FailureStatus.IN_PROGRESS.toString());
        } catch (Exception e) {
            logger.error("exception in getActiveFailuresWithCoords", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public List<TexnithsDTO> getTexnitesPositions(String token) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getTexnitesPositions();
        } catch (Exception e) {
            logger.error("exception in getTexnitesPositions: ", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public List<Failure> getActiveFailuresWithCoordsperTexnith(String am) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            return pfrMapper.getActiveFailuresWithCoordsPerTexnith(Failure.FailureStatus.ANNOUNCED.toString(),
                    Failure.FailureStatus.IN_PROGRESS.toString(), am);
        } finally {
            sqlSession.close();
        }
    }

    public List<String> getGrafeiaAssignedToPostalCode(String postal_code, String token) {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getGrafeiaAssignedToPostalCode(postal_code);
        } catch (Exception e) {
            logger.error("exception in getGrafeiaAssignedToPostalCode", e);
        } finally {
            sqlSession.close();
        }
        return null;
    }


    public List<Announcement> getValidAnnouncements() {

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            return pfrMapper.getValidAnnouncements();
        } catch (Exception e) {
            logger.error("exception in getValidAnnouncements", e);
        } finally {
            sqlSession.close();
        }

        return null;
    }

    public List<Misthotos> getTexnitesByGraf(String token) {

        List<Misthotos> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getTexnitesByGraf();
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getTexnitesByGraf", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public List<ContractorMisthotos> getTexnitesContractorByGraf(String token) {

        List<ContractorMisthotos> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getTexnitesContractorByGraf();
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("exception in getTexnitesContractorByGraf", e);
        } finally {
            sqlSession.close();
        }
        return result;
    }

    public Long insertNetworkHazardReport(Failure f, String user, List<FileItem> files) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        Integer result = null;
        final int max_file_size = 10000000; //max size per photo is 10MB
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);

            // fill the status field in failure object based on failure type, transient MV faults are considered as recovered at time of registration
            if (f.getFailureType().getId() == FailureType.TRANSIENT_MV_FAULT_ID) {
                f.setStatus(Failure.FailureStatus.RESOLVED);
            } else {
                f.setStatus(Failure.FailureStatus.ANNOUNCED);
            }

            //fill the grafeio assignment
            f.setGrafeio(f.getAssignments().get(0).getAssignee());

            //insert failure
            //result = pfrMapper.insertNetworkHazardReport(f);
            result = pfrMapper.insertFailure(f);

            String contact_method = FailureHistory.ContactMethod.no_contact.toString();
            if ((f.getEmail() != null) && (f.getEmail() != "") && (f.getFailureType().getId() != FailureType.TRANSIENT_MV_FAULT_ID)) {
                contact_method = FailureHistory.ContactMethod.email.toString();
            }
            if ((f.getInput_channel() == Failure.InputChannel.WEBSITE) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_ANDROID) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_IOS)) {
                contact_method = FailureHistory.ContactMethod.sms.toString();
            }

            //insert failure history
            FailureHistory fh = new FailureHistory(f.getId(), (contact_method != FailureHistory.ContactMethod.no_contact.toString() ? Message.FAILURE_ANNOUNCED_MESSAGE_ID : null),
                    contact_method, Failure.FailureStatus.ANNOUNCED.toString(), null, user);
            result = pfrMapper.insertFailureHistory(fh);

            // in case of transient MV faults the fault is considered as recovered at time of registration
            if (f.getFailureType().getId() == FailureType.TRANSIENT_MV_FAULT_ID) {
                fh = new FailureHistory(f.getId(), (f.getEmail() == null || f.getEmail() == "" ? null : 1),FailureHistory.ContactMethod.no_contact.toString(),
                        Failure.FailureStatus.RESOLVED.toString(), null, user);
                result = pfrMapper.insertFailureHistory(fh);
            }

            //insert failure questions
            List<Question> questionsList = f.getQuestions();
            for (Iterator<Question> questionIterator = questionsList.iterator(); questionIterator.hasNext(); ) {
                Question question = questionIterator.next();
                question.setFailure_id(f.getId());
                result = pfrMapper.insertFailureQuestion(question);
            }

            //insert failure landmarks and coordinates
            List<Landmark> landmarkList = f.getLandmarks();
            for (Iterator<Landmark> landmarkIterator = landmarkList.iterator(); landmarkIterator.hasNext(); ) {
                Landmark landmark = landmarkIterator.next();
                landmark.setFailure_id(f.getId());
                result = pfrMapper.insertFailureLandmark(landmark);
                for (Iterator<Point> pointIterator = landmark.getPointsList().iterator(); pointIterator.hasNext(); ) {
                    Point point = pointIterator.next();
                    point.setLandmark_id(landmark.getId());
                    result = pfrMapper.insertLandmarkPoint(point);
                }
            }

            //insert failure assignment
            FailureAssignment failureAssignment = f.getAssignments().get(0);
            failureAssignment.setFailure_id(f.getId());
            result = pfrMapper.insertFailureAssignment(failureAssignment);

            //invalidate otp in case of failures reported in website's relevant page or in mobile apps
            if ((f.getInput_channel() == Failure.InputChannel.WEBSITE) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_IOS) || (f.getInput_channel() == Failure.InputChannel.MOBILE_APP_ANDROID)) {
                result = datasecurityMapper.invalidateOtp(f.getOtp());
            }
            
            if (files.size() > 0) {
            	for (FileItem file : files) {
            		if (file.getSize() > max_file_size || file.getContentType().indexOf("image") == -1) {
            			result = -1;
            		}
            	}
            	
            	if (result > 0) {
            		result = storePhotos(files, pfrMapper, f.getId().intValue())?1:-1;
            	}
            }
            
            if (result > 0) {
            	sqlSession.commit();
            }
        } catch (Exception e) {
            logger.error("exception in insertNetworkHazardReport", e);
            result = -1;
        } finally {
            sqlSession.close();
        }

        return (result == -1 ? -1L : f.getId());
    }
    
    public Boolean storePhotos(List<FileItem> fis, PFRMapper mapper, Integer failure_id) {
		logger.info("Inserting photos into database, number of records: " + fis.size());
		try {
			List<NetworkHazardPhoto> nhps = new ArrayList<NetworkHazardPhoto>();
			NetworkHazardPhoto  nhp = new NetworkHazardPhoto();

			for (FileItem fi : fis) {
				logger.info("file item" + fi.getName());
				nhp = new NetworkHazardPhoto();

				nhp.setFailure_id(failure_id);
				//nhp.setPhoto(sun.misc.IOUtils.readFully(fi.getInputStream(), -1, true));
				try (InputStream is = fi.getInputStream()) {
				    byte[] photoBytes = toByteArray(is);
				    nhp.setPhoto(photoBytes);
				}
				nhp.setName(fi.getName());
				nhp.setPhoto_size(fi.getSize());
				nhp.setContent_type(fi.getContentType());
				nhps.add(nhp);
			}

			return insertPhotos(nhps, mapper) == 1 ? true : false;

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
    
    public int insertPhotos(List<NetworkHazardPhoto> nhps, PFRMapper mapper) {
		logger.info("Inserting NetworkHazardPhoto into database, number of records: " + nhps.size());

		try {
			for (NetworkHazardPhoto nhp : nhps) {
				if (mapper.insertNetworkHazardPhoto(nhp) != 1)
					return 0;
			}
			return 1;

		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

    private static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
	public List<Question> getQuestionsForNetworkHazard(String language) {

        List<Question> result = null;
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
            result = pfrMapper.getQuestionsForNetworkHazard(language);
        } catch (Exception e) {
            logger.error("exception in getTexnitesByGraf", e);
        } finally {
            sqlSession.close();
        }
        return result;
	}

	public Integer getFailureType(Long failure_id) {
		 SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
	     try {
	            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);
	            return pfrMapper.getTypeOfFailure(failure_id);
	     } catch (Exception e) {
	            logger.error("exception in getValidAnnouncements", e);
	     } finally {
	            sqlSession.close();
	     }

	     return null;
	}

    public List<CallcenterPerformanceMetricsDTO> getCallcenterPerformanceMetrics(Date requestedDate) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            return pfrMapper.getCallcenterPerformanceMetrics(requestedDate);
        } catch (Exception e) {
            logger.error("exception in getCallcenterPerformanceMetrics", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public List<KallikratikosOTA> getKLOT(String language) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            return pfrMapper.getKLOT(language);
        } catch (Exception e) {
            logger.error("exception in getKLOT", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public List<KallikratikosOTA> getKLOTbyVPD(String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            DATASECURITYMapper datasecurityMapper = sqlSession.getMapper(DATASECURITYMapper.class);
            datasecurityMapper.setAppUserToken(token);
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            return pfrMapper.getKLOTbyVPD();
        } catch (Exception e) {
            logger.error("exception in getKLOTbyVPD", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public KallikratikosOTA getKLOTbyErmhsKLOT(String ermhsKLOT) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            PFRMapper pfrMapper = sqlSession.getMapper(PFRMapper.class);

            return pfrMapper.getKLOTbyErmhsKLOT(ermhsKLOT);
        } catch (Exception e) {
            logger.error("exception in getKLOTbyErmhsKLOT", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

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

    public boolean isIVRTokenValid(String ivrToken) {
        try (SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession()){
            PFRMapper mapper = sqlSession.getMapper(PFRMapper.class);
            String validToken = mapper.getIVRToken();
            return validToken.equals(ivrToken);
         }catch (Exception e) {
            logger.error("exception in isIVRTokenValid: ", e);
            throw e;
        }
    }
}