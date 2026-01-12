package gr.deddie.pfr.rest;

import gr.deddie.pfr.model.AssignmentDTO;
import gr.deddie.pfr.model.UserDataRole;
import gr.deddie.pfr.model.UserInfoDTO;
import gr.deddie.pfr.services.ManageUsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by M.Masikos on 24/10/2016.
 */
@Path("/Users")
public class ManageUsersRESTService extends BaseResource  {
    private static Logger logger = LogManager.getLogger(ManageUsersRESTService.class);
    private static ManageUsersService manageUsersServiceManager;

    @RolesAllowed("pfr_admin")
    @POST
    @Path("/addAssignment")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response addAssignment(AssignmentDTO assignmentDTO) {
        logger.info("Operation \'/addAssignment\' has been invoked!");
        manageUsersServiceManager = new ManageUsersService();
        Integer result;

        try {

            result = manageUsersServiceManager.addAssignment(assignmentDTO);
            logger.info("addAssignment response: " + result);

        } catch (Exception e) {
            logger.error("exception in addAssignment", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(result).build();
    }

//    @RolesAllowed("pfr_admin")
//    @PUT
//    @Path("/updateAssignment")
//    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
//    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
//    public Response updateAssignment() {
//        logger.info("Operation \'/updateAssignment\' has been invoked!");
//        manageUsersServiceManager = new ManageUsersService();
//        Integer result;
//
//        try {
//
//            result = manageUsersServiceManager..updateFailureType(ft);
//            logger.info("updateFailureType response: " + result);
//
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//
//        return Response.ok(result).build();
//    }

    @RolesAllowed("pfr_admin")
    @GET
    @Path("/getAssignments/{app_id}")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response getAssignments(@PathParam("app_id") Integer app_id) {
        logger.info("Operation \'/getAssignments\' has been invoked!");
        manageUsersServiceManager = new ManageUsersService();
        List<AssignmentDTO> assignmentDTOs = null;

        try {

            assignmentDTOs = manageUsersServiceManager.getAssignments(app_id);
            if (!assignmentDTOs.equals(null)) {
                logger.info("getAssignments response: " + assignmentDTOs.toString());
            }

        } catch (Exception e) {
            logger.error("exception in getAssignments", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(assignmentDTOs).build();
    }

    @RolesAllowed("pfr_admin")
    @GET
    @Path("/searchUser")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response searchUser(@QueryParam("searchText") final String searchText) {
        logger.info("Operation \'/searchUser\' has been invoked!");
        manageUsersServiceManager = new ManageUsersService();
        UserInfoDTO result = null;

        try {
            result = manageUsersServiceManager.searchUser(searchText);
            logger.info("user found: " + (result == null ? "none" : result.toString()));
        } catch (Exception e) {
            logger.error("exception in searchUser", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return (result == null ? errorResponse(Response.Status.NOT_FOUND, "No User found") : Response.ok(result).build());
    }

    @RolesAllowed("pfr_admin")
    @PUT
    @Path("/delinkUser/{user_id:[0-9]+}/dataRole/{data_role_id:[0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response delinkUserDataRole(@PathParam("user_id") Long user_id, @PathParam("data_role_id") Long data_role_id) {
        logger.info("Operation \'/delinkUserDataRole\' has been invoked!");
        manageUsersServiceManager = new ManageUsersService();
        int result = 0;

        try {
            result = manageUsersServiceManager.delinkUserDataRole(user_id, data_role_id);
        } catch (Exception e) {
            logger.error("exception in delinkUserDataRole", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("delinked", String.valueOf(result));
        return Response.ok(responseObj).build();
    }

    @RolesAllowed("pfr_admin")
    @POST
    @Path("/linkUser/{user_id:[0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON  + ";charset=utf-8")
    public Response linkUserDataRole(@PathParam("user_id") Long user_id, UserDataRole dataRole) {
        logger.info("Operation \'/linkUserDataRole\' has been invoked!");
        manageUsersServiceManager = new ManageUsersService();
        int result = 0;

        try {
            result = manageUsersServiceManager.linkUserDataRole(user_id, dataRole.getId());
        } catch (Exception e) {
            logger.error("exception in linkUserDataRole", e);
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("linked", String.valueOf(result));
        return Response.ok(responseObj).build();
    }
}
