package gr.deddie.pfr.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.deddie.pfr.model.*;
import gr.deddie.pfr.services.FibergridService;
import gr.deddie.pfr.services.FibergridService.FibergridServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

/**
 * REST endpoint for FiberGrid inbound API.
 * Handles fault creation and updates from FiberGrid to PFR.
 * 
 * Endpoints:
 * - POST /pfr/faults/create - Create a new fault in PFR
 * - POST /pfr/faults/update - Update an existing fault in PFR
 */
@Path("/pfr/faults")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class FibergridRestService {

    private static final Logger logger = LogManager.getLogger(FibergridRestService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final FibergridService service;

    public FibergridRestService() {
        this.service = new FibergridService();
    }

    // For testing
    public FibergridRestService(FibergridService service) {
        this.service = service;
    }

    /**
     * POST /pfr/faults/create
     * 
     * FiberGrid creates a fault in PFR.
     * 
     * @param authorization Bearer token for authentication
     * @param clientName Name of the client application
     * @param clientVersion Version of the client application
     * @param requestId Unique request ID (UUID)
     * @param request Fault creation request body
     * @return 201 with pfr_id on success, or error response
     */
    @POST
    @Path("/create")
    @PermitAll
    public Response createFault(
            @HeaderParam("Authorization") String authorization,
            @HeaderParam("x-client-name") String clientName,
            @HeaderParam("x-client-version") String clientVersion,
            @HeaderParam("x-request-id") String requestId,
            FibergridFaultCreateRequest request) {
        
        long startTime = System.currentTimeMillis();
        String requestBody = null;
        String responseBody = null;
        int httpStatus = 500;

        try {
            requestBody = toJson(request);
            logger.info("POST /pfr/faults/create - requestId: " + requestId + ", fibergridId: " + 
                (request != null ? request.getFibergridId() : "null"));

            // Validate token
            String authenticatedClient;
            try {
                authenticatedClient = service.validateToken(authorization);
            } catch (FibergridServiceException e) {
                httpStatus = 401;
                FibergridErrorResponse error = FibergridErrorResponse.unauthorized();
                responseBody = toJson(error);
                return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
            }

            // Use authenticated client name if header not provided
            if (clientName == null || clientName.isEmpty()) {
                clientName = authenticatedClient;
            }

            // Create fault
            FibergridFault created = service.createFault(request, clientName);

            // Build success response
            FibergridFaultCreateResponse response = FibergridFaultCreateResponse.success(created.getId());
            responseBody = toJson(response);
            httpStatus = 201;

            logger.info("Fault created successfully - id: " + created.getId());
            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (FibergridServiceException e) {
            return handleServiceException(e, requestId);
        } catch (Exception e) {
            logger.error("Unexpected error in createFault", e);
            httpStatus = 500;
            FibergridErrorResponse error = FibergridErrorResponse.internalError();
            responseBody = toJson(error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        } finally {
            // Log API call
            long duration = System.currentTimeMillis() - startTime;
            service.logApiCall(
                FibergridApiLog.Direction.INBOUND,
                "/pfr/faults/create",
                "POST",
                requestId,
                clientName,
                clientVersion,
                requestBody,
                responseBody,
                httpStatus,
                duration
            );
        }
    }

    /**
     * POST /pfr/faults/update
     * 
     * FiberGrid updates a fault in PFR.
     * 
     * @param authorization Bearer token for authentication
     * @param clientName Name of the client application
     * @param clientVersion Version of the client application
     * @param requestId Unique request ID (UUID)
     * @param request Fault update request body
     * @return 200 on success, or error response
     */
    @POST
    @Path("/update")
    @PermitAll
    public Response updateFault(
            @HeaderParam("Authorization") String authorization,
            @HeaderParam("x-client-name") String clientName,
            @HeaderParam("x-client-version") String clientVersion,
            @HeaderParam("x-request-id") String requestId,
            FibergridFaultUpdateRequest request) {
        
        long startTime = System.currentTimeMillis();
        String requestBody = null;
        String responseBody = null;
        int httpStatus = 500;

        try {
            requestBody = toJson(request);
            logger.info("POST /pfr/faults/update - requestId: " + requestId + ", fibergridId: " + 
                (request != null ? request.getFibergridId() : "null"));

            // Validate token
            String authenticatedClient;
            try {
                authenticatedClient = service.validateToken(authorization);
            } catch (FibergridServiceException e) {
                httpStatus = 401;
                FibergridErrorResponse error = FibergridErrorResponse.unauthorized();
                responseBody = toJson(error);
                return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
            }

            // Use authenticated client name if header not provided
            if (clientName == null || clientName.isEmpty()) {
                clientName = authenticatedClient;
            }

            // Update fault
            service.updateFault(request, clientName);

            // Build success response
            FibergridFaultUpdateResponse response = FibergridFaultUpdateResponse.success();
            responseBody = toJson(response);
            httpStatus = 200;

            logger.info("Fault updated successfully - fibergridId: " + request.getFibergridId());
            return Response.ok(response).build();

        } catch (FibergridServiceException e) {
            return handleServiceException(e, requestId);
        } catch (Exception e) {
            logger.error("Unexpected error in updateFault", e);
            httpStatus = 500;
            FibergridErrorResponse error = FibergridErrorResponse.internalError();
            responseBody = toJson(error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        } finally {
            // Log API call
            long duration = System.currentTimeMillis() - startTime;
            service.logApiCall(
                FibergridApiLog.Direction.INBOUND,
                "/pfr/faults/update",
                "POST",
                requestId,
                clientName,
                clientVersion,
                requestBody,
                responseBody,
                httpStatus,
                duration
            );
        }
    }

    // ==================== Error Handling ====================

    /**
     * Convert service exception to appropriate HTTP response.
     */
    private Response handleServiceException(FibergridServiceException e, String requestId) {
        FibergridErrorResponse error;
        Response.Status status;

        switch (e.getErrorType()) {
            case VALIDATION_ERROR:
                status = Response.Status.BAD_REQUEST;
                error = e.getDetails() != null ? 
                    FibergridErrorResponse.validationError(e.getDetails()) :
                    FibergridErrorResponse.validationError(e.getMessage());
                break;

            case INVALID_ENUM:
                status = Response.Status.BAD_REQUEST;
                error = e.getDetails() != null ?
                    FibergridErrorResponse.invalidEnum(e.getDetails()) :
                    new FibergridErrorResponse("Invalid enum value", "INVALID_ENUM", 
                        Collections.singletonList(e.getMessage()));
                break;

            case INVALID_STATUS:
                status = Response.Status.BAD_REQUEST;
                error = e.getDetails() != null ?
                    FibergridErrorResponse.invalidStatus(e.getDetails().get(0)) :
                    FibergridErrorResponse.invalidStatus(e.getMessage());
                break;

            case DUPLICATE:
                status = Response.Status.BAD_REQUEST;
                error = new FibergridErrorResponse("Duplicate record", "DUPLICATE",
                    Collections.singletonList(e.getMessage()));
                break;

            case NOT_FOUND:
                status = Response.Status.NOT_FOUND;
                // Extract fibergridId from message if available
                error = FibergridErrorResponse.notFound("unknown");
                if (e.getMessage() != null) {
                    error.setDetails(Collections.singletonList(e.getMessage()));
                }
                break;

            case INTERNAL_ERROR:
            default:
                status = Response.Status.INTERNAL_SERVER_ERROR;
                error = FibergridErrorResponse.internalError();
                logger.error("Internal error - requestId: " + requestId, e);
                break;
        }

        logger.warn("Request failed - requestId: " + requestId + ", status: " + status.getStatusCode() + 
            ", error: " + error.getErrorCode());
        return Response.status(status).entity(error).build();
    }

    /**
     * Convert object to JSON string for logging.
     */
    private String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.warn("Failed to serialize object to JSON", e);
            return obj.toString();
        }
    }
}
