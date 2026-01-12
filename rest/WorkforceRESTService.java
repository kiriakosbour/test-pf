package gr.deddie.pfr.rest;

import gr.deddie.pfr.managers.PFRDataManager;
import gr.deddie.pfr.model.TexnithsDTO;
import gr.deddie.pfr.services.WorkforceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/workforce")
public class WorkforceRESTService extends BaseResource {
    private static Logger logger = LogManager.getLogger(WorkforceRESTService.class);
    private static WorkforceService workforceService;
    private static PFRDataManager pfrDataManager;

    @RolesAllowed("pfr_user")
    @GET
    @Path("/getActiveFailuresByTexniths")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getActiveFailuresByTexniths(@HeaderParam(FailureRESTService.AUTHORIZATION_PROPERTY) String token) {
        workforceService = new WorkforceService();

        try {
            return Response.ok(workforceService.getActiveFailuresByTexniths(token)).build();
        } catch (Exception e) {
            logger.error("Unexpected error in getActiveFailuresByTexniths: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RolesAllowed("pfr_powercut_map_view")
    @GET
    @Path("/getTexnitesPositions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTexnitesPositions(@HeaderParam(FailureRESTService.AUTHORIZATION_PROPERTY) String token) {
        pfrDataManager = new PFRDataManager();

        try {
            List<TexnithsDTO> texnithsDTOList = pfrDataManager.getTexnitesPositions(token);
            return Response.ok(texnithsDTOList).build();
        } catch (Exception e) {
            logger.error("exception in getTexnitesPositions rest service: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("unexpected.error.occured").build();
        }
    }
}
