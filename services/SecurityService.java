package gr.deddie.pfr.services;

import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.managers.SECURITYDataManager;
import gr.deddie.pfr.model.*;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Random;

/**
 * Created by M.Masikos on 30/11/2016.
 */
public class SecurityService {
    private static Logger logger = LogManager.getLogger(SecurityService.class);
    private static SECURITYDataManager securityDataManager;

    public List<UserDataRole> getDataRoles() throws Exception {
        securityDataManager = new SECURITYDataManager();
        List<UserDataRole> userDataRoleList = null;

        userDataRoleList = securityDataManager.getDataRoles();

        return userDataRoleList;
    }

    public List<UserDataRoleWithItems> getDataRolesWithItems() throws Exception {
        securityDataManager = new SECURITYDataManager();
        List<UserDataRoleWithItems> userDataRoleWithItemsList = null;

        userDataRoleWithItemsList = securityDataManager.getDataRolesWithItems();

        return userDataRoleWithItemsList;
    }

    public List<DataRoleItem> getDataRoleItems(long data_role_id) throws Exception {
        securityDataManager = new SECURITYDataManager();
        List<DataRoleItem> dataRoleItems = null;

        dataRoleItems = securityDataManager.getDataRoleItems(data_role_id);

        return dataRoleItems;
    }

    public int addDataRoleItem(DataRoleItem dataRoleItem) throws Exception {
        securityDataManager = new SECURITYDataManager();

        return securityDataManager.addDataRoleItem(dataRoleItem);
    }

    public int deleteDataRoleItem(DataRoleItem dataRoleItem) throws Exception {
        securityDataManager = new SECURITYDataManager();

        return securityDataManager.deleteDataRoleItem(dataRoleItem);
    }

    public int updateDataRoleItem(DataRoleItem dataRoleItem) throws Exception {
        securityDataManager = new SECURITYDataManager();

        return securityDataManager.updateDataRoleItem(dataRoleItem);
    }

    public int addDataRole(UserDataRole userDataRole) throws Exception {
        securityDataManager = new SECURITYDataManager();

        //logger.info("add data role: {" + userDataRole.getName() + "," + (userDataRole.getDescription().equals(null) ? "" : userDataRole.getDescription()) + "}");
        return securityDataManager.addDataRole(userDataRole);
    }

    public int updateDataRole(UserDataRole userDataRole) throws Exception {
        securityDataManager = new SECURITYDataManager();

        return securityDataManager.updateDataRole(userDataRole);
    }

    public int deleteDataRole(UserDataRole userDataRole) throws Exception {
        securityDataManager = new SECURITYDataManager();

        return securityDataManager.deleteDataRole(userDataRole);
    }

    public Otp sendOTP(String cell, Otp.OtpType otpType, String language) {
        securityDataManager = new SECURITYDataManager();
        return securityDataManager.sendOTP(new Otp(cell, new String(generateOTP(6)), otpType), language);
    }

    private char[] generateOTP(int length) {
        String numbers = "1234567890";
        Random random = new Random();
        char[] otp = new char[length];

        for(int i = 0; i< length ; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return otp;
    }

    public Otp getOtpOnlyIfActive(Otp otp) {
        securityDataManager = new SECURITYDataManager();

        return securityDataManager.getOtpOnlyIfActive(otp);
    }
}
