package gr.deddie.pfr.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.deddie.pfr.model.*;
import gr.deddie.pfr.services.PowerCutReportService;
import gr.deddie.pfr.services.SecurityService;
import gr.deddie.pfr.utilities.EncryptDecrypt;
import gr.deddie.pfr.utilities.LocalizationUTIL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by M.Masikos on 30/11/2016.
 */
@Path("/Security")
public class SecurityRESTService extends BaseResource {
    private static Logger logger = LogManager.getLogger(SecurityRESTService.class);
    private static final String AUTHORIZATION_PROPERTY = "X-Auth-Token";
    private static SecurityService securityServiceManager;
    private static PowerCutReportService powerCutReportService;

    @PermitAll
    @GET
    @Path("/getDataRoles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersDataRoles() {
        securityServiceManager = new SecurityService();
        List<UserDataRole> userDataRoleList;

        try {
            userDataRoleList = securityServiceManager.getDataRoles();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(userDataRoleList).build();

    }

    @RolesAllowed("pfr_admin")
    @GET
    @Path("/getDataRolesWithItems")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataRolesWithItems() {
        securityServiceManager = new SecurityService();
        List<UserDataRoleWithItems> userDataRoleWithItemsList;

        try {
            userDataRoleWithItemsList = securityServiceManager.getDataRolesWithItems();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(userDataRoleWithItemsList).build();
    }

    @RolesAllowed("pfr_admin")
    @GET
    @Path("/getDataRoleItems/{data_role_id:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataRoleItems(@PathParam("data_role_id") Long data_role_id) {
        securityServiceManager = new SecurityService();
        List<DataRoleItem> dataRoleItemList;

        try {
            dataRoleItemList = securityServiceManager.getDataRoleItems(data_role_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return (dataRoleItemList == null ? errorResponse(Response.Status.NOT_FOUND, "No item for the specific data role") : Response.ok(dataRoleItemList).build());
    }

    @RolesAllowed("pfr_admin")
    @POST
    @Path("/addDataRoleItem/{data_role_id:[0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDataRoleItem(@PathParam("data_role_id") Long data_role_id, DataRoleItem dataRoleItem) {
        securityServiceManager = new SecurityService();
        int result = 0;
        dataRoleItem.setData_role_id(data_role_id);

        try {
            if (data_role_id == 0) {
                return errorResponse(Response.Status.BAD_REQUEST, "data role is not selected");
            }
            result = securityServiceManager.addDataRoleItem(dataRoleItem);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("Role item added", String.valueOf(result));
        return Response.ok(responseObj).build();
    }

    @RolesAllowed("pfr_admin")
    @DELETE
    @Path("/deleteDataRoleItem")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDataRoleItem(DataRoleItem dataRoleItem) {
        securityServiceManager = new SecurityService();
        int result;

        try {
            result = securityServiceManager.deleteDataRoleItem(dataRoleItem);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("Role item deleted", String.valueOf(result));
        return Response.ok(responseObj).build();
    }

    @RolesAllowed("pfr_admin")
    @PUT
    @Path("/updateDataRoleItem")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDataRoleItem(DataRoleItem dataRoleItem) {
        securityServiceManager = new SecurityService();
        int result;

        try {
            result = securityServiceManager.updateDataRoleItem(dataRoleItem);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("Role item updated", String.valueOf(result));
        return Response.ok(responseObj).build();
    }

    @RolesAllowed("pfr_admin")
    @POST
    @Path("/addDataRole")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDataRoleItem(UserDataRole userDataRole) {
        securityServiceManager = new SecurityService();
        int result = 0;

        try {
            result = securityServiceManager.addDataRole(userDataRole);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("Data role added", String.valueOf(result));
        return Response.ok(responseObj).build();
    }

    @RolesAllowed("pfr_admin")
    @PUT
    @Path("/updateDataRole")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDataRole(UserDataRole userDataRole) {
        securityServiceManager = new SecurityService();
        int result;

        try {
            result = securityServiceManager.updateDataRole(userDataRole);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("Data role updated", String.valueOf(result));
        return Response.ok(responseObj).build();
    }

    @RolesAllowed("pfr_admin")
    @DELETE
    @Path("/deleteDataRole")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDataRole(UserDataRole userDataRole) {
        securityServiceManager = new SecurityService();
        int result;

        try {
            result = securityServiceManager.deleteDataRole(userDataRole);
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.CONFLICT, String.valueOf(e.getErrorCode()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, String.valueOf(e.getMessage()));
        }

        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("deleted", String.valueOf(result));
        return Response.ok(responseObj).build();
    }

    @PermitAll
    @POST
    @Path("/sendOTP")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendOTP(Otp otp) {
        securityServiceManager = new SecurityService();

        if ((otp.getCell() == null) || (otp.getType() == null)) {
            return errorResponse(Response.Status.BAD_REQUEST, "parameters.are.missing");
        }

        if (securityServiceManager.sendOTP(otp.getCell(), otp.getType(), LocalizationUTIL.GREEK) == null)
        {
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, "error.in.sendOTP");
        } else {
            return Response.ok(1).build();
        }
    }

    @PermitAll
    @POST
    @Path("/v2/sendOTP")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendOTPv2(@HeaderParam(PowerCutReportRESTService.INTEGRITY_PROPERTY) String integrityHash,
                              @HeaderParam(LocalizationUTIL.LANGUAGE_PROPERTY) String language, InputStream inputStream) {
        securityServiceManager = new SecurityService();
        powerCutReportService = new PowerCutReportService();
        Otp otp = null;

        String jsonString = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .map(line -> line.trim())
                .collect(Collectors.joining(""));


        try {
            String correctedLanguage = powerCutReportService.en4EnglishElseGreek(language);

            // check integrity
            if (!integrityHash.equals(PowerCutReportRESTService.hashGenerator(jsonString))) {
                logger.info("integrity.check.failed");
                return errorResponseWithHash(Response.Status.FORBIDDEN, "access.not.allowed");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            otp = objectMapper.readValue(jsonString, Otp.class);

            //decrypt inputs
            EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
            otp.setCell(encryptDecrypt.decrypt(otp.getCell()));

        if ((otp.getCell() == null) || (otp.getType() == null)) {
            return errorResponseWithHash(Response.Status.BAD_REQUEST, "parameters.are.missing");
        }

        if (securityServiceManager.sendOTP(otp.getCell(), otp.getType(), correctedLanguage) == null)
        {
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, "error.in.sendOTP");
        } else {
            return Response.ok(1L)
                    .header(PowerCutReportRESTService.INTEGRITY_PROPERTY, PowerCutReportRESTService.hashGenerator(1L))
                    .build();
        }
        } catch (Exception e) {
            logger.error("Exception in sendOTPv2 rest service: ", e);
            return errorResponseWithHash(Response.Status.INTERNAL_SERVER_ERROR, "system.error");
        }
    }
}
