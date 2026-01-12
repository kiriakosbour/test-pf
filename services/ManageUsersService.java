package gr.deddie.pfr.services;

import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.managers.USERSDataManager;
import gr.deddie.pfr.model.AssignmentDTO;
import gr.deddie.pfr.model.UserInfoDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.eclipse.persistence.annotations.Convert;

import java.util.List;

/**
 * Created by M.Masikos on 24/10/2016.
 */
public class ManageUsersService {
    private static Logger logger = LogManager.getLogger(ManageUsersService.class);
    private static USERSDataManager usersDataManager;

    public List<AssignmentDTO> getAssignments(Integer app_id) throws Exception {
        usersDataManager = new USERSDataManager();
        List<AssignmentDTO> assignmentDTOs = null;

        assignmentDTOs = usersDataManager.getAssignments(app_id);
        if (assignmentDTOs != null) {
            logger.info(assignmentDTOs.toString());
        }
        return assignmentDTOs;
    }

    public Integer addAssignment(AssignmentDTO assignmentDTO) throws Exception {
        usersDataManager = new USERSDataManager();
        Integer result;

        result = usersDataManager.insertAssignment(assignmentDTO);

        logger.info("insertAssignment result: " + result);

        return result;
    }

    public UserInfoDTO searchUser(String searchText) throws Exception {
        usersDataManager = new USERSDataManager();
        UserInfoDTO result = null;

        logger.info("search user with AM or fullname:" + searchText);

        result = usersDataManager.searchUser(searchText);

        return result;
    }

    public int delinkUserDataRole(long user_id, long data_role_id) throws Exception {
        usersDataManager = new USERSDataManager();
        int result = 0;

        logger.info("deactivate data role with id:" + String.valueOf(data_role_id) + " from user with id: " + String.valueOf(user_id));

        result = usersDataManager.delinkUserDataRole(user_id, data_role_id);

        return result;
    }

    public int linkUserDataRole(long user_id, long data_role_id) throws Exception {
        usersDataManager = new USERSDataManager();
        int result = 0;

        logger.info("add data role with id:" + String.valueOf(data_role_id) + " to user with id: " + String.valueOf(user_id));

        result = usersDataManager.linkUserDataRole(user_id, data_role_id);

        return result;
    }
}
