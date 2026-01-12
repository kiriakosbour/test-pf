package gr.deddie.pfr.services;

import static gr.deddie.pfr.model.Message.FAILURE_RECOVERED_MESSAGE_ID;
import static gr.deddie.pfr.model.Message.FAILURE_RECOVERED_SMS_MESSAGE_ID;
import static gr.deddie.pfr.model.Message.SUCCESSFUL_REGISTRATION_MESSAGE_ID;
import static gr.deddie.pfr.utilities.CheckedFunctionUtil.wrap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.naming.AuthenticationException;
import javax.ws.rs.NotFoundException;

import gr.deddie.pfr.model.*;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.fileupload.FileItem;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.deddie.pfr.managers.AUTHDataManager;
import gr.deddie.pfr.managers.ERMINFODataManager;
import gr.deddie.pfr.managers.GRAFEIODataManager;
import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.managers.SECURITYDataManager;
import gr.deddie.pfr.managers.THALISDataManager;

/**
 * Created by M.Masikos on 22/6/2016.
 */
public class FailureService {
    private final String failure_announcement_sms_txt_GR = "Η αναγγελία σας για διακοπή ηλεκτροδότησης καταχωρίστηκε με Α/Α %s. Όλες οι απαραίτητες ενέργειες για τη διερεύνηση του προβλήματος ξεκίνησαν.";
    private final String failure_announcement_sms_txt_EN = "Your request for fault report is submitted with number %s. The necessary actions to investigate the problem have been initiated.";
    private final String nhz_announcement_sms_txt_GR = "Η αναγγελία σας για επικίνδυνη κατάσταση στο δίκτυο καταχωρήθηκε με Α/Α %s. Όλες οι απαραίτητες ενέργειες για τη διερεύνηση του προβλήματος ξεκίνησαν.";
    private final String nhz_announcement_sms_txt_EN = "Your request for hazardous situation report at the network is submitted with number %s. The necessary actions to investigate the problem have been initiated.";
    private static Logger logger = LogManager.getLogger(FailureService.class);
    private static PFRDataManager pfrDataManager;
    private static AUTHDataManager authDataManager;
    private static ERMINFODataManager erminfoDataManager;
    private static THALISDataManager thalisDataManager;
    private static GRAFEIODataManager grafeioDataManager;
    private static SECURITYDataManager securityDataManager;

    public Integer deleteFailureType(FailureType ft) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.deleteFailureType(ft);
            
        return result;
    }
    
    public Integer deleteMessage(Message m) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.deleteMessage(m);

        return result;
    }  
    
    public Integer updateFailureType(FailureType ft) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.updateFailureType(ft);
            
        return result;
    }
    
    public Integer updateMessage(Message m) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.updateMessage(m);
            
        return result;
    }
    
    public Integer insertFailureType(FailureType ft) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.insertFailureType(ft);
            
        return result;
    }
    
    public Integer insertMessage(Message m) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.insertMessage(m);
            
        return result;
    }

    public Long insertFailure(Failure f, String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        grafeioDataManager = new GRAFEIODataManager();
        authDataManager = new AUTHDataManager();
        Long result;

        //check whether the grafeio is active
        if (!grafeioDataManager.isGrafeioActive(f.getAssignments().get(0).getAssignee().getGraf())) {
            sendEmailviaOracleProc("m.masikos@deddie.gr", "m.paschou@deddie.gr","ΔΕΝ ΥΠΑΡΧΕΙ ΑΝΤΙΣΤΟΙΧΗΣΗ ΓΡΑΦΕΙΟΥ-ΤΚ",f.toString());
            throw new NotFoundException();
        }

        String user = pfrDataManager.getUser(token);
        if (user == null) {
            return -1L;
        }

        //assign value to input channel property based on user's roles
        List<String> roles = authDataManager.getUserRoles(token);
        if (roles.contains("pfr_user")) {
            f.setInput_channel(Failure.InputChannel.DEDDIE_USER);
        } else {
            f.setInput_channel(Failure.InputChannel.CALLCENTER_AGENT);
        }

        // insert failure, result = -1 means unsuccessful insert
        result = pfrDataManager.insertFailure(f, user);

        // send message, if it fails, the user is not informed
        if ((result == 1) && (!f.getEmail().equals(null) && (f.getEmail() != "") && (f.getFailureType().getId() != FailureType.TRANSIENT_MV_FAULT_ID))) {
            Message message = pfrDataManager.getMessage(SUCCESSFUL_REGISTRATION_MESSAGE_ID);
            if (!message.equals(null)) {
                sendEmailviaOracleProc(f.getEmail(), null,message.getMessage_header() + " με Α/Α " + f.getId().toString(), message.getMessage_description());
            }
        }
            
        return result;
    }

    public void markFailureAsRecalled (Failure failure) throws Exception {
        pfrDataManager = new PFRDataManager();
        securityDataManager = new SECURITYDataManager();

        Message message = pfrDataManager.getMessage(Message.FAILURE_RECALLED_MESSAGE_ID);
        if (message == null) {
            throw new Exception("cannot.retrieve.message");
        }
        String contact_method = null;
        Integer message_id = null;
        String creator = null;

        if ((failure.getInput_channel() == Failure.InputChannel.WEBSITE) || (failure.getInput_channel() == Failure.InputChannel.MOBILE_APP_ANDROID) || (failure.getInput_channel() == Failure.InputChannel.MOBILE_APP_IOS)) {
            contact_method = FailureHistory.ContactMethod.sms.toString();
            message_id = Message.FAILURE_RECALLED_MESSAGE_ID;
            creator = failure.getOtp().getCell();
        }
        if (failure.getEmail() != null) {
            contact_method = FailureHistory.ContactMethod.email.toString();
            message_id = Message.FAILURE_RECALLED_MESSAGE_ID;
            creator = failure.getCreator();
        }
        Integer result = pfrDataManager.markFailureAsRecalledByUser(new FailureHistory(failure.getId(), message_id,
                contact_method, Failure.FailureStatus.RECALLED_BY_ANNOUNCER.toString(), null, creator), failure);

        // send SMS, if it fails, the user is not informed
        if (contact_method.equals(FailureHistory.ContactMethod.sms.toString()) && (result != null) && (result > 0)) {
            //TODO uncomment the following line of code when the text of id 14 in PFR_MESSAGES is updated and accepts parameters
            //securityDataManager.sendSMS(failure.getOtp().getCell(), String.format(message.getMessage_description(), failure.getFailureType().getId() == FailureType.DANGEROUS_STATE_ID?"επικίνδυνης κατάστασης στο δίκτυο":"βλάβης", failure.getId()));
            securityDataManager.sendSMS(failure.getOtp().getCell(), message.getMessage_description());
        } else {
            throw new Exception("cannot.add.recall.event");
        }
   }

   //insert failure come from WEBSITE channel or mobile apps
    public Long insertFailureNoSecurity(Failure f, String language) throws Exception {
        pfrDataManager = new PFRDataManager();
        securityDataManager = new SECURITYDataManager();

        // insert failure, result = -1 means unsuccessful insert
        Long result = pfrDataManager.insertFailure(f, f.getCell());

        // send SMS, if it fails, the user is not informed
        if (result > 0) {
            securityDataManager.sendSMS(f.getCell(),
                    String.format((language.equals(LocalizationUTIL.ENGLISH) ? failure_announcement_sms_txt_EN : failure_announcement_sms_txt_GR),result.toString()));
        }

        return result;
    }

    public Integer updateFailures(List<Failure> failures, String token) throws Exception{
        MailService mailService = new MailService();
        pfrDataManager = new PFRDataManager();
        authDataManager = new AUTHDataManager();
        Integer result;

        //retrieve masked fields of the failures in case the user belongs to callcenter's agents so as not to update them
        List<String> roles = authDataManager.getUserRoles(token);
        List<Failure> failureList = failures;
        if (roles.contains("pfr_callcenter") && !roles.contains("pfr_user")) {
            failureList = failures.stream().map(wrap(failure -> {
                Failure oldFailure = pfrDataManager.getFailure(failure.getId(), token);
                if (oldFailure == null) {
                    throw new Exception("updating.not.existing.failure");
                }
                failure.setAssigned_texniths(oldFailure.getAssigned_texniths());
                failure.setAssigned_texniths_contractor(oldFailure.getAssigned_texniths_contractor());
                failure.setTexniths_assignment_timestamp(oldFailure.getTexniths_assignment_timestamp());
                return failure;
            })).collect(Collectors.toList());
        };
        // update failures, result = 1 means successful update
        result = pfrDataManager.updateFailures(failureList, token);

//        for (Failure f : failureList) {
//            Integer message_id = f.getEvents().stream().max(Comparator.comparing(FailureHistory::getId)).get().getMessage_id();
//
//            if (message_id != null && f.getEmail() != null) {
//                Optional<Message> message = Optional.ofNullable(pfrDataManager.getMessage(message_id));
//                if (message.isPresent()) {
//                    mailService.sendEmailviaOracleProc(f.getEmail(), null,"Αρ. Αιτήματος " + f.getId().toString() + ": " + message.get().getMessage_header(), message.get().getMessage_description());
//                }
//                else {
//                    throw new Exception("no.message.found.for.id." + message_id.toString());
//                }
//            }
//        }

        return result;
    }

    public Integer addFailuresHistory (List<Failure> failures, String token) throws Exception {
        authDataManager = new AUTHDataManager();
        pfrDataManager = new PFRDataManager();
        String user = authDataManager.getUser(token);
        Integer result = 0;

        for (Failure f : failures) {
            Integer message_id = null;
            String comments = null;
            String status = null;

            for (FailureHistory fh : f.getEvents())
            {
                message_id = fh.getMessage_id();
                comments = fh.getComments();
                status = fh.getStatus();
            }

            result += this.insertFailureHistory(new FailureHistory(f.getId(), ( message_id != null && f.getEmail() != null ?
                    message_id : null),
                    ( message_id != null && f.getEmail() != null ? FailureHistory.ContactMethod.email.toString() : FailureHistory.ContactMethod.no_contact.toString()),
                    status, comments, user));

            if (message_id != null && f.getEmail() != null) {
                Message message = pfrDataManager.getMessage(message_id);
                if (!message.equals(null)) {
                    sendEmailviaOracleProc(f.getEmail(), null,"Αρ. Αιτήματος " + f.getId().toString() + ": " + message.getMessage_header(), message.getMessage_description());
                }
            }
        }

        return result;
    }

    public Integer addFailuresAssignments (List<FailureAssignment> failureAssignments) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result = 0;

        for (FailureAssignment failureAssignment : failureAssignments) {
            result += pfrDataManager.insertFailureAssignment(failureAssignment, "DEDDHE_GLOBAL");
        }

        return result;
    }

    public Integer updateFailuresGroup (List<FailureDTO> failureDTOS, GeneralFailureGroup generalFailureGroup, String token) throws Exception {
        pfrDataManager = new PFRDataManager();

        return pfrDataManager.updateFailuresGroup(failureDTOS, generalFailureGroup, token);
    }

    public GeneralFailureGroup addGeneralFailureGroup (String label, String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        authDataManager = new AUTHDataManager();

        String user = authDataManager.getUser(token);

        if (user == null)
            throw new AuthenticationException("user.is.not.loggedin");

        GeneralFailureGroup generalFailureGroup = new GeneralFailureGroup();
        generalFailureGroup.setCreator(user);
        generalFailureGroup.setLabel(label);

        return pfrDataManager.addGeneralFailureGroup(generalFailureGroup);
    }

    public void sendEmailviaOracleProc(String emailTo, String emailCC, String subject, String body) {
        MailService mailService = new MailService();

        try {
            mailService.sendEmailviaOracleProc(emailTo, emailCC,subject, body);
        } catch (PersistenceException ex) {
            logger.error("PersistenceException: ", ex);
        }
    }

    public void sendEmailviaJava(String email, String subject, String body) {
        MailService mailService = new MailService();

        try {
            mailService.sendEmail(email,subject, body);
        } catch (MessagingException ex) {
            logger.error("MessagingException: ", ex);
        } catch (ConfigurationException ex) {
            logger.error("ConfigurationException: ", ex);
        }

    }

    public Integer markGeneralFailuresinaPostalCodeAsRecovered (String token, String postal_code, String last_update_comments) throws Exception {
        pfrDataManager = new PFRDataManager();

        List<FailureDTO> failures = pfrDataManager.getActiveGeneralFailuresByPostalCodeByGraf(token, postal_code);

        for (FailureDTO f : failures) {
            f.setLast_update_comments(last_update_comments);
        }

        return markFailuresAsRecovered(failures, token);
    }

    public Integer alterFailuresGeneralFlag (List<FailureDTO> failures, Boolean generalFlag) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result = 0;

        result = pfrDataManager.alterFailuresGeneralFlag(failures, generalFlag);

        return result;
    }

    public Integer markFailuresAsRecovered (List<FailureDTO> recoveredFailures, String token) throws Exception {
        authDataManager = new AUTHDataManager();
        pfrDataManager = new PFRDataManager();
        securityDataManager = new SECURITYDataManager();
        Integer result = 0;

        String user = authDataManager.getUser(token);

        Message email_message = pfrDataManager.getMessage(Message.FAILURE_RECOVERED_MESSAGE_ID);
        Message sms_message = pfrDataManager.getMessage(Message.FAILURE_RECOVERED_SMS_MESSAGE_ID);

        for (FailureDTO f : recoveredFailures) {
            Failure failure = pfrDataManager.getFailure(f.getId(), token);
            Integer message_id = null;
            String contact_method = FailureHistory.ContactMethod.no_contact.toString();

            // if failure is not dangerous state in network, estimated restore time is not null, contacts are not null, previous status is IN_PROGRESS and estimated_restore_time + 5hours > now()
            // send message (#813)
            if (failure.getEstimated_restore_time() != null) {
                Calendar extendedEstimatedRestoreTime = Calendar.getInstance();
                extendedEstimatedRestoreTime.setTime(failure.getEstimated_restore_time());
                extendedEstimatedRestoreTime.add(Calendar.HOUR, 5);
                failure.getEvents().sort(Comparator.comparing(FailureHistory::getId).reversed());

                if (failure.getEvents().get(0).getStatus().equals(Failure.FailureStatus.IN_PROGRESS.toString())
                        && ((failure.getEmail() != null) || (failure.getCell() != null)) && (failure.getFailureType().getId() != FailureType.DANGEROUS_STATE_ID)
                        && (Calendar.getInstance().before(extendedEstimatedRestoreTime))) {
                    if (failure.getCell() != null) {
                        message_id = FAILURE_RECOVERED_SMS_MESSAGE_ID;
                        contact_method = FailureHistory.ContactMethod.sms.toString();
                    } else if (failure.getEmail() != null) {
                        message_id = FAILURE_RECOVERED_MESSAGE_ID;
                        contact_method = FailureHistory.ContactMethod.email.toString();
                    }
                }
            }

            result += this.insertFailureHistory(new FailureHistory(f.getId(), message_id, contact_method,
                    Failure.FailureStatus.RESOLVED.toString(), f.getLast_update_comments(), user));

            // send message
            // message should leave after the event is persisted
            if (contact_method == FailureHistory.ContactMethod.sms.toString()) {
                securityDataManager.sendSMS(failure.getCell(), sms_message.getMessage_description());
            } else if (contact_method == FailureHistory.ContactMethod.email.toString()) {
                sendEmailviaOracleProc(failure.getEmail(), null, "Αρ. Αιτήματος " + failure.getId().toString() + ": " + email_message.getMessage_header(), email_message.getMessage_description());
            }
        }

        return result;
    }

    public Integer insertFailureHistory(FailureHistory fh) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.insertFailureHistory(fh);
            
        return result;
    }

    public Integer insertFailureAssignment(FailureAssignment failureAssignment) throws Exception {
        pfrDataManager = new PFRDataManager();
        Integer result;

        result = pfrDataManager.insertFailureAssignment(failureAssignment, "DEDDHE_GLOBAL");

        return result;
    }

    public List<Failure> getFailuresAnnouncements() throws Exception {
        pfrDataManager = new PFRDataManager();
        List<Failure> failures = null;

        //try {
            failures = pfrDataManager.getFailuresAnnouncements();

            return failures;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        //return null;
    }
    
    public List<FailureType> getFailureTypes() throws Exception {
        pfrDataManager = new PFRDataManager();

        return pfrDataManager.getFailureTypes();
    }
    
    public List<FailureHistory> getFailureHistory(String failure_id) throws Exception {
        pfrDataManager = new PFRDataManager();
        List<FailureHistory> failureHistoryList = null;

        failureHistoryList = pfrDataManager.getFailureHistory(failure_id);

        return failureHistoryList;
    }

    public Failure getFailure(String token, Long failure_id) throws Exception {
        pfrDataManager = new PFRDataManager();
        authDataManager = new AUTHDataManager();

        Failure failure = pfrDataManager.getFailure(failure_id, "DEDDHE_GLOBAL");

        //mask some fields of the results in case the user belongs to callcenter's agents
        List<String> roles = authDataManager.getUserRoles(token);
        if ((failure.getAssigned_texniths() != null) && roles.contains("pfr_callcenter") && !roles.contains("pfr_user")) {
            failure.setAssigned_texniths(new Misthotos(Long.parseLong("1"), "***", "***", "***"));
            failure.setTexniths_assignment_timestamp(null);
        };
        return failure;
    }

    public Map<String, Object> getSupply(String supply_no) throws Exception {
        //erminfoDataManager = new ERMINFODataManager();
        thalisDataManager = new THALISDataManager();
        pfrDataManager = new PFRDataManager();
        grafeioDataManager = new GRAFEIODataManager();
        Supply supply = null;

        if ((supply_no !=null) && (supply_no.length()>8))
        {
            supply_no = supply_no.substring(1,9);
        }
        supply = pfrDataManager.getSupply(supply_no);
        supply.setKlot(pfrDataManager.getKLOTbyErmhsKLOT(supply.getAcct_muncp_code()));

        if (supply != null) {
            String paddedLastTenant = "0" + supply.getLastTenant().toString(); //padded with 0 is necessary when tenant number consists of a single digit
            String lastTenant = paddedLastTenant.substring(Math.max(paddedLastTenant.length() - 2,0)); // correct lastTenant if tenant number consists of two digits
            supply.setLastTenantName(pfrDataManager.getSupplysLastTenantName(supply.getPrefecture() + supply.getNo() + lastTenant));
            supply.setDisconnectedForDebt(thalisDataManager.isSupplyDisconnectedForDebts(supply.getPrefecture() + supply.getNo(),lastTenant));
            supply.setGrafeio(pfrDataManager.getGrafeio(supply.getGrafeio().getGraf(), "DEDDHE_GLOBAL"));
        }

        Map<String, Object> supplyInfo = new HashMap<>();
        supplyInfo.put("supply", supply);
        supplyInfo.put("isGrafeioActive", supply == null ? null : grafeioDataManager.isGrafeioActive(supply.getGrafeio().getGraf()));

        return supplyInfo;
    }

    public FailureDTOsLimitedList getFailureDTOsLimitedList() throws Exception {
        pfrDataManager = new PFRDataManager();
        FailureDTOsLimitedList failureDTOsLimitedList = null;

        failureDTOsLimitedList = pfrDataManager.getFailureDTOsLimitedList();

        return failureDTOsLimitedList;
    }
    
    public List<FailureDTO> getFailureDTOs() throws Exception {
        pfrDataManager = new PFRDataManager();

        List<FailureDTO> failureDTOs = pfrDataManager.getFailureDTOs();

        return failureDTOs;
    }

    public List<FailureDTO> getActiveRestNonGeneralFailureDTOsByGraf(String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        List<FailureDTO> failureDTOs = null;

        failureDTOs = pfrDataManager.getActiveRestFailureDTOsByGraf(token);

        return failureDTOs;
    }

    public List<FailureDTO> getFailureDTOsRecalledInLast24hByGraf(String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        List<FailureDTO> failureDTOs = null;

        // fetch the announcements recalled during the last 24 hours
        Date fromTimestamp = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24));

        failureDTOs = pfrDataManager.getFailureDTOsByStatusByDateByGraf(token, Failure.FailureStatus.RECALLED_BY_ANNOUNCER, fromTimestamp);

        return failureDTOs;
    }

    public List<FailureDTO> getActiveFailureDTOsBySupplyNoOrPostalCode(String token, String supply_no, String postal_code) throws Exception {
        pfrDataManager = new PFRDataManager();

        return pfrDataManager.getActiveFailureDTOsBySupplyNoOrPostalCode(token, supply_no, postal_code);
    }

    public Map<String, List<Grafeio>> getResponsibleGrafsperPostalCode(String token, String postal_code, String language) throws Exception {
        pfrDataManager = new PFRDataManager();
        List<Grafeio> responsibleGrafeiaAll = pfrDataManager.getResponsibleGrafsperPostalCode("DEDDHE_GLOBAL", postal_code, language);
        List<Grafeio> responsibleGrafeiaActive = pfrDataManager.getResponsibleGrafsperPostalCode(token, postal_code, language);

        // remove active grafeia so as to have the inactive ones
        responsibleGrafeiaAll.removeAll(responsibleGrafeiaActive);
        Map<String, List<Grafeio>> responsibleGrafeia = new HashMap<>();
        responsibleGrafeia.put("activeGrafeia", responsibleGrafeiaActive);
        // responsibleGrafeiaAll contains the inactive grafeia
        responsibleGrafeia.put("inactiveGrafeia", responsibleGrafeiaAll);

        return responsibleGrafeia;
    }

    public List<DangerousStateDTO> getActiveDangerousStatesByGraf(String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        List<DangerousStateDTO> dangerousStateDTOS = null;

        dangerousStateDTOS = pfrDataManager.getActiveDangerousStatesByGraf(token);

        return dangerousStateDTOS;
    }

    public List<FailureStatsDTO> getActiveGeneralFailuresSumsByGraf(String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        grafeioDataManager = new GRAFEIODataManager();

        List<FailureDTO> failureDTOS = pfrDataManager.getActiveGeneralFailuresByVPD(token);

        List<GeneralFailureGroup> generalFailureGroups = failureDTOS.stream().map(item -> item.getGeneral_failure_group()).distinct().collect(Collectors.toList());

        List<FailureStatsDTO> failureStatsDTOS = generalFailureGroups.stream().map((item) -> {
            //List<FailureDTO> groupFailureDTOS = failureDTOS.stream().filter(failureDTO -> item.getId().equals(failureDTO.getGeneral_failure_group().getId())).collect(Collectors.toList());
            List<FailureDTO> groupFailureDTOS = pfrDataManager.getActiveGeneralFailuresByGroupID(item.getId());
            if (groupFailureDTOS == null) {
                return null;
            }
            FailureDTO lastAnnouncedFailureInTheGroup = groupFailureDTOS.stream().sorted(Comparator.comparing(FailureDTO::getAnnounced)).collect(Collectors.toList()).get(groupFailureDTOS.size()-1);
            Boolean isDangerous = groupFailureDTOS.stream().anyMatch(FailureDTO::getDangerous);

            return new FailureStatsDTO(item,groupFailureDTOS.size(),lastAnnouncedFailureInTheGroup.getAnnounced(),groupFailureDTOS.get(0).getLast_updated(),groupFailureDTOS,isDangerous,groupFailureDTOS.get(0).getStatus());
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return failureStatsDTOS;
    }

    public List<FailureDTO> getActiveGeneralFailuresByPostalCodeByGraf(String token, String postal_code) throws Exception {
        pfrDataManager = new PFRDataManager();
        List<FailureDTO> failureDTOs = null;

        failureDTOs = pfrDataManager.getActiveGeneralFailuresByPostalCodeByGraf(token, postal_code);

        return failureDTOs;
    }

    public List<FailureDTO> getFailureDTOsByIDs(List<Long> ids) throws Exception {
        pfrDataManager = new PFRDataManager();
        //failureDTOs = null;

        List<FailureDTO> failureDTOs = pfrDataManager.getFailureDTOsByIDs(ids);

        return failureDTOs;
    }
    
    public List<Message> getMessages() throws Exception {
        pfrDataManager = new PFRDataManager();

        List<Message> messages = pfrDataManager.getMessages();
        return messages;
    }

    public List<Failure.FailureStatus> getStatuses() throws Exception {

        return Arrays.asList(Failure.FailureStatus.values());
    }

    public Map<String, List<GeneralFailureGroup>> getGeneralFailureGroups(String token) {

        pfrDataManager = new PFRDataManager();
        List<GeneralFailureGroup> activeGeneralFailureGroups = pfrDataManager.getActiveGeneralFailuresGroupsByVPD(token);
        List<GeneralFailureGroup> proposedGeneralFailureGroups = Arrays.stream(Failure.GeneralFailureGroup.values()).limit(10).filter((group) -> {
            return !activeGeneralFailureGroups.stream().anyMatch(e -> e.getLabel().equals(group.getValue()));
        }).map(group -> new GeneralFailureGroup(null, group.getValue(), null, null)).collect(Collectors.toList());

        Map<String, List<GeneralFailureGroup>> generalFailureGroups = new HashMap<>();
        generalFailureGroups.put("proposedGeneralFailureGroups", proposedGeneralFailureGroups);
        generalFailureGroups.put("activeGeneralFailureGroups", activeGeneralFailureGroups);

        return generalFailureGroups;
    }

    public FailureDTOsLimitedList getFailureDTOsByCriteria(FailureSearchParameters searchParameters, String token) throws Exception {
        pfrDataManager = new PFRDataManager();
        authDataManager = new AUTHDataManager();
        FailureDTOsLimitedList failureDTOsLimitedList;

        failureDTOsLimitedList = pfrDataManager.getFailureDTOsByCriteria(searchParameters, token);

        //mask some fields of the results in case the user belongs to callcenter's agents
        List<String> roles = authDataManager.getUserRoles(token);
        if (roles.contains("pfr_callcenter") && !roles.contains("pfr_user")) {
            List<FailureDTO> failureDTOList = failureDTOsLimitedList.getAnnouncements().stream().map(failureDTO -> {
                if (failureDTO.getAssigned_texniths() != null) {
                    failureDTO.setAssigned_texniths(new Misthotos(Long.parseLong("1"), "***", "***", "***"));
                    failureDTO.setTexniths_assignment_timestamp(null);
                }
                return failureDTO;
            }).collect(Collectors.toList());
            failureDTOsLimitedList.setAnnouncements(failureDTOList);
        };

        return failureDTOsLimitedList;
    }

    public List<FailureDTO> getFailuresBookContent(FailuresBookSearchParameters searchParameters, String token) throws Exception {
        pfrDataManager = new PFRDataManager();

        return pfrDataManager.getFailuresBookContent(searchParameters, token);
    }

    public static java.sql.Date stringToSqlDate(String date) throws ParseException {
		Calendar cal = stringToCalendar(date);
		java.sql.Date sql_date = new java.sql.Date(cal.getTime().getTime());
		return sql_date;
	}
    
    public static Calendar stringToCalendar(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		
		//FIXME: change date format
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
		cal.setTime(sdf.parse(date));
		return cal;
	}

    public List<Failure> getActiveFailuresWithCoords(String token) {
        pfrDataManager = new PFRDataManager();

        List<Failure> failures = pfrDataManager.getActiveFailuresWithCoords(token);

        return failures;
    }

    public List<Failure> getActiveFailuresWithCoordsPerTexnith(String am) {
        pfrDataManager = new PFRDataManager();

        return pfrDataManager.getActiveFailuresWithCoords(am);
    }

    public List<Announcement> getValidAnnouncements() throws Exception {
        pfrDataManager = new PFRDataManager();
        List<Announcement> announcements = null;

        return pfrDataManager.getValidAnnouncements();
    }

    public List<Misthotos> getTexnitesByGraf(String token) throws Exception {
        pfrDataManager = new PFRDataManager();

        List<Misthotos> texnites = pfrDataManager.getTexnitesByGraf(token);

        return texnites;
    }

    public List<ContractorMisthotos> getTexnitesContractorByGraf(String token) throws Exception {
        pfrDataManager = new PFRDataManager();

        List<ContractorMisthotos> contractorTexnites = pfrDataManager.getTexnitesContractorByGraf(token);

        return contractorTexnites;
    }

	public Long insertNetworkHazardReport(Failure f, List<FileItem> files, String language) {
		 pfrDataManager = new PFRDataManager();
	     securityDataManager = new SECURITYDataManager();
	     
	     List<FailureAssignment> failureAssignmentList = new ArrayList<>();
         failureAssignmentList.add(new FailureAssignment(pfrDataManager.getGrafeio(f.getGrafeio().getGraf(), "DEDDHE_GLOBAL"), f.getCell()));
         f.setAssignments(failureAssignmentList);
   
	     // insert failure, result = -1 means unsuccessful insert
	     Long result = pfrDataManager.insertNetworkHazardReport(f, f.getCell(), files);

	     logger.info("language="+language);
	     logger.info("cell="+f.getCell());
	     logger.info("result="+ result.toString());

	     // send SMS, if it fails, the user is not informed
	     if (result > 0) {
	    	 securityDataManager.sendSMS(f.getCell(),String.format((language.equals(LocalizationUTIL.ENGLISH) ? nhz_announcement_sms_txt_EN : nhz_announcement_sms_txt_GR), result.toString()));
	     }

	     return result;
	}

	public List<Question> getQuestionsForNetworkHazard(String language) {
		  pfrDataManager = new PFRDataManager();

		  return pfrDataManager.getQuestionsForNetworkHazard(language);
	}

	public Integer getFailureType(Failure f) {
		 pfrDataManager = new PFRDataManager();

		 return pfrDataManager.getFailureType(f.getId());
	}
}
