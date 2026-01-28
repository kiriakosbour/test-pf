package gr.deddie.pfr.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.deddie.pfr.model.*;
import gr.deddie.pfr.services.FibergridOutboundService;
import gr.deddie.pfr.services.FibergridService;
import gr.deddie.pfr.services.FibergridService.FibergridServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST endpoint for FiberGrid inbound API.
 * Handles fault creation and updates from FiberGrid to PFR.
 * * Endpoints:
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
    private static final int DEFAULT_MAX_RESULTS = 1000;
    
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
     * * FiberGrid creates a fault in PFR.
     * * @param authorization Bearer token for authentication
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
     * * FiberGrid updates a fault in PFR.
     * * @param authorization Bearer token for authentication
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
     /**
     * GET /pfr/faults/getFibergridFaults
     * * Retrieve faults from PFR_FBG_FAULTS for frontend display.
     * Supports filtering by status, flag_related, and date range.
     * * @param authorization Bearer token for authentication
     * @param status Filter by status (optional): Resolved, Pending Issues, In process, Cancelled
     * @param flagRelated Filter by flag (optional): 1=HEDNO, 2=Fiber, 3=Both
     * @param fromDate Filter from date (optional): ISO format yyyy-MM-dd
     * @param toDate Filter to date (optional): ISO format yyyy-MM-dd
     * @param maxResults Limit results (optional, default 1000)
     * @return List of FibergridFaultDTO
     */
    @GET
    @Path("/getFibergridFaults")
    @RolesAllowed({"pfr_user", "pfr_admin", "pfr_fibergrid"})
    public Response getFaults(
            @HeaderParam("Authorization") String authorization,
            @QueryParam("status") String status,
            @QueryParam("flag_related") Integer flagRelated,
            @QueryParam("from_date") String fromDate,
            @QueryParam("to_date") String toDate,
            @QueryParam("max_results") Integer maxResults) {

        logger.info("GET /pfr/faults/getFibergridFaults - status={}, flag_related={}, from_date={}, to_date={}, max_results={}",
                status, flagRelated, fromDate, toDate, maxResults);

        try {
            // Validate status if provided
            if (status != null && !status.isEmpty() && !FibergridFaultStatus.isValid(status)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(FibergridErrorResponse.validationError(
                                "Invalid status value. Must be one of: " + FibergridFaultStatus.getValidValues()))
                        .build();
            }

            // Validate flag_related if provided
            if (flagRelated != null && !FibergridFlagRelated.isValid(flagRelated)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(FibergridErrorResponse.validationError(
                                "Invalid flag_related value. Must be " + FibergridFlagRelated.getValidValuesDescription()))
                        .build();
            }

            // Parse dates
            Date parsedFromDate = parseDate(fromDate);
            Date parsedToDate = parseDate(toDate);

            // Set default max results if not provided
            int effectiveMaxResults = (maxResults != null && maxResults > 0) ? maxResults : DEFAULT_MAX_RESULTS;

            // Get faults from service
            List<FibergridFault> faults = service.getFaultsByCriteria(
                    status, flagRelated, parsedFromDate, parsedToDate, effectiveMaxResults);

            // Convert to DTOs
            List<FibergridFaultDTO> dtos = faults.stream()
                    .map(FibergridFaultDTO::fromEntity)
                    .collect(Collectors.toList());

            logger.info("GET /pfr/faults/getFibergridFaults - returning {} faults", dtos.size());

            return Response.ok(dtos).build();

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request parameters: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(FibergridErrorResponse.validationError(e.getMessage()))
                    .build();
        } catch (FibergridServiceException e) {
            logger.error("Service error in getFaults", e);
            return handleServiceException(e, null);
        } catch (Exception e) {
            logger.error("Unexpected error in getFaults", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(FibergridErrorResponse.internalError())
                    .build();
        }
    }

    /**
     * GET /pfr/faults/{id}
     * * Retrieve a single fault by internal ID.
     * * @param id Internal fault ID
     * @return FibergridFaultDTO or 404
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"pfr_user", "pfr_admin", "pfr_fibergrid"})
     public Response getFaultById(
            @HeaderParam("Authorization") String authorization,
            @PathParam("id") Long id) {

        logger.info("GET /pfr/faults/{} - retrieving fault by internal ID", id);

        try {
            if (id == null || id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(FibergridErrorResponse.validationError("Invalid fault ID"))
                        .build();
            }

            FibergridFault fault = service.getFaultById(id);

            FibergridFaultDTO dto = FibergridFaultDTO.fromEntity(fault);

            logger.info("GET /pfr/faults/{} - fault found", id);

            return Response.ok(dto).build();

        } catch (FibergridServiceException e) {
            logger.warn("Fault not found with ID: {}", id);
            return handleServiceException(e, null);
        } catch (Exception e) {
            logger.error("Unexpected error in getFaultById", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(FibergridErrorResponse.internalError())
                    .build();
        }
    }

    /**
     * GET /pfr/faults/by-fibergrid-id/{fibergridId}
     * * Retrieve a fault by FiberGrid external ID.
     * * @param fibergridId FiberGrid external ID
     * @return FibergridFaultDTO or 404
     */
    @GET
    @Path("/by-fibergrid-id/{fibergridId}")
    @RolesAllowed({"pfr_user", "pfr_admin", "pfr_fibergrid"})
    public Response getFaultByFibergridId(
            @HeaderParam("Authorization") String authorization,
            @PathParam("fibergridId") String fibergridId) {

        logger.info("GET /pfr/faults/by-fibergrid-id/{} - retrieving fault by FiberGrid ID", fibergridId);

        try {
            if (fibergridId == null || fibergridId.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(FibergridErrorResponse.validationError("FiberGrid ID is required"))
                        .build();
            }

            FibergridFault fault = service.getFaultByFibergridId(fibergridId);

            FibergridFaultDTO dto = FibergridFaultDTO.fromEntity(fault);

            logger.info("GET /pfr/faults/by-fibergrid-id/{} - fault found with internal ID: {}", 
                    fibergridId, fault.getId());

            return Response.ok(dto).build();

        } catch (FibergridServiceException e) {
            logger.warn("Fault not found with FiberGrid ID: {}", fibergridId);
            return handleServiceException(e, null);
        } catch (Exception e) {
            logger.error("Unexpected error in getFaultByFibergridId", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(FibergridErrorResponse.internalError())
                    .build();
        }
    }

    // ==================== Outbound Operations ====================

    /**
     * POST /pfr/faults/sendToFibergrid
     *
     * PFR sends an update to Fibergrid.
     * Used for manual updates from the PFR UI.
     *
     * @param authorization Bearer token for authentication
     * @param request Outbound update request body
     * @return 200 on success, or error response
     */
    @POST
    @Path("/sendToFibergrid")
    @RolesAllowed({"pfr_admin", "pfr_fibergrid"})
    public Response sendToFibergrid(
            @HeaderParam("Authorization") String authorization,
            FibergridOutboundUpdateRequest request) {

        logger.info("POST /pfr/faults/sendToFibergrid - fibergridId: {}",
                request != null ? request.getFibergridId() : "null");

        try {
            // Validate request
            if (request == null || request.getFibergridId() == null ||
                    request.getFibergridId().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(FibergridErrorResponse.validationError("fibergrid_id is required"))
                        .build();
            }

            // Get user from token for audit
            String user = "system";
            try {
                user = service.validateToken(authorization);
            } catch (FibergridServiceException e) {
                // Use default user if token validation fails
                logger.warn("Token validation failed for sendToFibergrid, using default user");
            }

            // Call outbound service
            FibergridOutboundService outboundService = new FibergridOutboundService();
            FibergridOutboundService.OutboundResult result =
                    outboundService.sendUpdate(request.getFibergridId(), request, user);

            if (result.isSuccess()) {
                FibergridFaultUpdateResponse response = new FibergridFaultUpdateResponse(
                        true, result.getMessage());
                logger.info("sendToFibergrid successful for fibergridId: {}", request.getFibergridId());
                return Response.ok(response).build();
            } else {
                // Return error with appropriate status code
                Response.Status status = result.isRetryable() ?
                        Response.Status.SERVICE_UNAVAILABLE : Response.Status.BAD_REQUEST;

                FibergridErrorResponse error = new FibergridErrorResponse(
                        result.getMessage(), result.getErrorCode(), null);
                logger.warn("sendToFibergrid failed for fibergridId: {} - {} ({})",
                        request.getFibergridId(), result.getMessage(), result.getErrorCode());
                return Response.status(status).entity(error).build();
            }

        } catch (Exception e) {
            logger.error("Unexpected error in sendToFibergrid", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(FibergridErrorResponse.internalError())
                    .build();
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Convert entity to DTO for frontend.
     */
    private FibergridFaultDTO toDTO(FibergridFault fault) {
        FibergridFaultDTO dto = new FibergridFaultDTO();
        dto.setId(fault.getId());
        dto.setFibergridId(fault.getFibergridId());
        dto.setPfrFailureId(fault.getPfrId());
        dto.setAddress(fault.getAddress());
        dto.setLatitude(fault.getLatitude());
        dto.setLongitude(fault.getLongitude());
        dto.setFlagRelated(fault.getFlagRelated() != null ? fault.getFlagRelated().getValue() : null);
        dto.setFlagRelatedDescription(fault.getFlagRelated() != null ? fault.getFlagRelated().getDescription() : null);
        dto.setStatus(fault.getStatus() != null ? fault.getStatus().getValue() : null);
        dto.setNotes(fault.getNotes());
        dto.setDateCreated(fault.getDateCreated() != null ? fault.getDateCreated().getTime() : 0L);
        dto.setDateResolved(fault.getDateResolved() != null ? fault.getDateResolved().getTime() : 0L);
        dto.setContactName(fault.getContactName());
        dto.setContactPhone(fault.getContactPhone());
        dto.setContactEmail(fault.getContactEmail());
        dto.setCreated(fault.getCreated() != null ? fault.getCreated().getTime() : 0L);
        dto.setLastUpdated(fault.getLastUpdated() != null ? fault.getLastUpdated().getTime() : 0L);
        return dto;
    }

    /**
     * Parse date string to Date object.
     */
    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.warn("Invalid date format: " + dateStr);
            return null;
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