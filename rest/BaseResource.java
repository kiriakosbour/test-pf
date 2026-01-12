package gr.deddie.pfr.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by M.Masikos on 22/6/2016.
 */
public class BaseResource {
    private static Logger logger = LogManager.getLogger(BaseResource.class);

    protected Response errorResponse(Response.Status status, String errorMessage) {
        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("error", errorMessage);
        return Response.status(status).entity(responseObj.toString()).build();
    }

    protected Response errorResponseWithHash(Response.Status status, String errorMessage) {
        Map<String, String> responseObj = new HashMap<>();
        responseObj.put("error", errorMessage);
        logger.info(responseObj.toString());
        try {
            return Response.status(status).entity(responseObj.toString())
                    .header(PowerCutReportRESTService.INTEGRITY_PROPERTY, PowerCutReportRESTService.hashGenerator(responseObj.toString()))
                    .build();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | JsonProcessingException e) {
            logger.error("integrity.check.generation.error: ", e);
            return Response.status(status).entity(responseObj.toString())
                    .header(PowerCutReportRESTService.INTEGRITY_PROPERTY, "-1")
                    .build();
        }
    }
}
