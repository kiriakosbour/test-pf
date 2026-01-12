package gr.deddie.pfr.rest;

import gr.deddie.pfr.model.GrafeioWithPostalCodes;
import gr.deddie.pfr.model.Grafeio;
import gr.deddie.pfr.model.UserDataRoleWithItems;
import gr.deddie.pfr.services.FailureService;
import gr.deddie.pfr.services.GrafeioService;
import gr.deddie.pfr.services.SecurityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by M.Masikos on 5/1/2017.
 */

@Path("/grafeia/")
public class GrafeioRESTService extends BaseResource {
    private static Logger logger = LogManager.getLogger(GrafeioRESTService.class);
    private static final String AUTHORIZATION_PROPERTY = "X-Auth-Token";
    private static GrafeioService grafeioServiceManager;

    @RolesAllowed("pfr_admin")
    @GET
    @Path("/getGrafeiaWithPostalCodes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGrafeiaWithPostalCodes(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
        grafeioServiceManager = new GrafeioService();
        List<GrafeioWithPostalCodes> grafeioWithPostalCodesList;

        try {
            grafeioWithPostalCodesList = grafeioServiceManager.getGrafeiaWithPostalCodes(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(grafeioWithPostalCodesList).build();
    }
    
    @RolesAllowed({"pfr_user", "pfr_affected_areas_per_line_mgt", "pfr_outage_mgt", "pfr_callcenter"})
    @GET
    @Path("/getActiveGrafeia")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveGrafeia(@HeaderParam(AUTHORIZATION_PROPERTY) String token) {
        grafeioServiceManager = new GrafeioService();
        Map<String,List<Grafeio>> activeGrafeia;

        try {
            activeGrafeia = grafeioServiceManager.getLoggedOnUserAndRestActiveGrafeia(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Response.ok(activeGrafeia).build();
    }

}
