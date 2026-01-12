package gr.deddie.pfr.rest.callcenter;

import gr.deddie.pfr.model.Area;
import gr.deddie.pfr.model.Failure;
import gr.deddie.pfr.rest.BaseResource;
import gr.deddie.pfr.rest.FailureRESTService;
import gr.deddie.pfr.services.FailureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by m.masikos on 23/3/2017.
 */

@Path("/callcenter")
public class AreasRestService extends BaseResource{
    private static Logger logger = LogManager.getLogger(FailureRESTService.class);
    private static final String AUTHORIZATION_PROPERTY = "X-Auth-Token";

    @RolesAllowed({"pfr_admin", "pfr_callcenter_test"})
    @GET
    @Path("/getAreas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAreas() {
        List<Area> areas = new ArrayList<>();
        logger.info("Service \'getAreas\' has been invoked!");

        Area area = new Area(0L, "Περιοχή Α");
        areas.add(area);

        area = new Area(1L, "Περιοχή Β");
        areas.add(area);

        area = new Area(2L, "Περιοχή Γ");
        areas.add(area);

        return Response.ok(areas).build();
    }

    @RolesAllowed({"pfr_admin", "pfr_callcenter_test"})
    @GET
    @Path("/getActiveFailures")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveFailures() {
        List<Failure> activeFailures = new ArrayList<>();
        logger.info("Service \'getActiveFailures\' has been invoked!");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date date = new Date();
//        try {
//            Failure failure = new Failure(0, 0L, 25, simpleDateFormat.parse("01/04/2017 12:00"));
//            activeFailures.add(failure);
//
//            failure = new Failure(1, 1L, 25, simpleDateFormat.parse("01/04/2017 14:00"));
//            activeFailures.add(failure);
//
//            failure = new Failure(2, 2L, 25, simpleDateFormat.parse("01/04/2017 18:30"));
//            activeFailures.add(failure);
//
//            failure = new Failure(3, 0L, 25, simpleDateFormat.parse("31/03/2017 21:00"));
//            activeFailures.add(failure);
//
//        } catch (ParseException ex) {
//            logger.error("simpledateformatter" + ex);
//            return errorResponse(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage());
//        }
        return Response.ok(activeFailures).build();
    }
}
