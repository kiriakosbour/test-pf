package gr.deddie.pfr.services;

import gr.deddie.pfr.managers.FIBERGRIDDataManager;
import gr.deddie.pfr.model.*;
import gr.deddie.pfr.utilities.FibergridRequestValidator;
import gr.deddie.pfr.utilities.FibergridRequestValidator.ValidationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Service layer for FiberGrid fault operations.
 * Handles business logic, validation, and coordinates with the data manager.
 */
public class FibergridService {

    private static final Logger logger = LogManager.getLogger(FibergridService.class);
    private final FIBERGRIDDataManager dataManager;
    private final FibergridOutboundService outboundService;

    public FibergridService() {
        this.dataManager = new FIBERGRIDDataManager();
        this.outboundService = new FibergridOutboundService();
    }

    // For testing with mock data manager
    public FibergridService(FIBERGRIDDataManager dataManager) {
        this.dataManager = dataManager;
        this.outboundService = new FibergridOutboundService();
    }

    // For testing with mock dependencies
    public FibergridService(FIBERGRIDDataManager dataManager, FibergridOutboundService outboundService) {
        this.dataManager = dataManager;
        this.outboundService = outboundService;
    }

    /**
     * Exception thrown when a service operation fails.
     */
    public static class FibergridServiceException extends Exception {
        private final ErrorType errorType;
        private final List<String> details;

        public enum ErrorType {
            VALIDATION_ERROR,
            INVALID_ENUM,
            INVALID_STATUS,
            DUPLICATE,
            NOT_FOUND,
            INTERNAL_ERROR
        }

        public FibergridServiceException(ErrorType errorType, String message) {
            super(message);
            this.errorType = errorType;
            this.details = null;
        }

        public FibergridServiceException(ErrorType errorType, String message, List<String> details) {
            super(message);
            this.errorType = errorType;
            this.details = details;
        }

        public FibergridServiceException(ErrorType errorType, String message, Throwable cause) {
            super(message, cause);
            this.errorType = errorType;
            this.details = null;
        }

        public ErrorType getErrorType() {
            return errorType;
        }

        public List<String> getDetails() {
            return details;
        }
    }

    // ==================== Authentication ====================

    /**
     * Validate API token and return client name if valid.
     * 
     * @param authorizationHeader The full Authorization header value
     * @return Client name if valid
     * @throws FibergridServiceException if token is invalid
     */
    public String validateToken(String authorizationHeader) throws FibergridServiceException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.VALIDATION_ERROR, 
                "Invalid Authorization header format"
            );
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {
            String clientName = dataManager.validateToken(token);
            if (clientName == null) {
                throw new FibergridServiceException(
                    FibergridServiceException.ErrorType.VALIDATION_ERROR, 
                    "Invalid or expired token"
                );
            }
            return clientName;
        } catch (FibergridServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error validating token", e);
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.INTERNAL_ERROR, 
                "Error validating token", 
                e
            );
        }
    }

    // ==================== Fault Creation ====================

    /**
     * Create a new fault from a FiberGrid request.
     * 
     * @param request The fault creation request
     * @param createdBy The authenticated client name
     * @return The created fault with generated IDs
     * @throws FibergridServiceException on validation or persistence errors
     */
    public FibergridFault createFault(FibergridFaultCreateRequest request, String createdBy) 
            throws FibergridServiceException {
        
        // Validate request
        ValidationResult validation = FibergridRequestValidator.validateCreateRequest(request);
        if (!validation.isValid()) {
            FibergridServiceException.ErrorType errorType = 
                validation.hasEnumErrors() ? 
                    FibergridServiceException.ErrorType.INVALID_ENUM :
                    FibergridServiceException.ErrorType.VALIDATION_ERROR;
            throw new FibergridServiceException(errorType, "Validation failed", validation.getAllErrors());
        }

        // Check for duplicate
        if (dataManager.existsByFibergridId(request.getFibergridId())) {
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.DUPLICATE,
                "Fault with fibergrid_id '" + request.getFibergridId() + "' already exists"
            );
        }

        try {
            // Convert request to entity
            FibergridFault fault = request.toEntity();
            fault.setCreatedBy(createdBy);

            // Persist fault and photos
            FibergridFault created = dataManager.createFault(fault, request.getPhotos());
            if (created == null) {
                throw new FibergridServiceException(
                    FibergridServiceException.ErrorType.INTERNAL_ERROR,
                    "Failed to create fault"
                );
            }

            logger.info("Created fault: id=" + created.getId() + ", fibergridId=" + created.getFibergridId());
            return created;

        } catch (FibergridServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating fault", e);
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.INTERNAL_ERROR,
                "Error creating fault",
                e
            );
        }
    }

    // ==================== Fault Updates ====================

    /**
     * Update an existing fault from a FiberGrid request.
     * 
     * @param request The fault update request
     * @param updatedBy The authenticated client name
     * @return The updated fault
     * @throws FibergridServiceException on validation, not found, or persistence errors
     */
    public FibergridFault updateFault(FibergridFaultUpdateRequest request, String updatedBy) 
            throws FibergridServiceException {
        
        // Validate request
        ValidationResult validation = FibergridRequestValidator.validateUpdateRequest(request);
        if (!validation.isValid()) {
            FibergridServiceException.ErrorType errorType;
            if ("INVALID_STATUS".equals(validation.getErrorType())) {
                errorType = FibergridServiceException.ErrorType.INVALID_STATUS;
            } else if (validation.hasEnumErrors()) {
                errorType = FibergridServiceException.ErrorType.INVALID_ENUM;
            } else {
                errorType = FibergridServiceException.ErrorType.VALIDATION_ERROR;
            }
            throw new FibergridServiceException(errorType, "Validation failed", validation.getAllErrors());
        }

        try {
            // Find existing fault
            FibergridFault existing = dataManager.getFaultByFibergridId(request.getFibergridId());
            if (existing == null) {
                throw new FibergridServiceException(
                    FibergridServiceException.ErrorType.NOT_FOUND,
                    "Fault not found"
                );
            }

            // Track if status is changing to Resolved for outbound trigger
            FibergridFaultStatus previousStatus = existing.getStatus();
            FibergridFaultStatus newStatus = request.getStatus() != null ?
                FibergridFaultStatus.fromValue(request.getStatus()) : null;
            boolean statusChangingToResolved = newStatus == FibergridFaultStatus.RESOLVED
                && previousStatus != FibergridFaultStatus.RESOLVED;

            // Apply updates
            existing.setUpdatedBy(updatedBy);
            if (newStatus != null) {
                existing.setStatus(newStatus);
            }
            if (request.getDateResolved() != null) {
                existing.setDateResolved(request.getDateResolved());
            }
            if (request.getContactInformation() != null) {
                existing.setContactInformation(request.getContactInformation());
            }
            if (request.getEstimatedArrivalTimeDeddie() != null) {
                existing.setEstimatedArrivalTimeDeddie(request.getEstimatedArrivalTimeDeddie());
            }
            if (request.getEstimatedArrivalTimeFibergrid() != null) {
                existing.setEstimatedArrivalTimeFibergrid(request.getEstimatedArrivalTimeFibergrid());
            }
            if (request.getRootCause() != null) {
                existing.setRootCause(request.getRootCause());
            }

            // Handle notes - append rather than replace
            if (request.getNotes() != null) {
                dataManager.appendNotes(existing.getId(), request.getNotes(), updatedBy);
            }

            // Update fault and add photos
            dataManager.updateFault(existing, request.getPhotos());

            logger.info("Updated fault: id=" + existing.getId() + ", fibergridId=" + existing.getFibergridId());

            // Trigger outbound call to Fibergrid if status changed to Resolved
            if (statusChangingToResolved) {
                triggerOutboundStatusResolved(existing);
            }

            return existing;

        } catch (FibergridServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating fault", e);
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.INTERNAL_ERROR,
                "Error updating fault",
                e
            );
        }
    }

    /**
     * Trigger outbound call to Fibergrid when fault status changes to Resolved.
     * This is a fire-and-forget operation - failures are logged but don't affect the main update.
     *
     * @param fault The fault that was resolved
     */
    private void triggerOutboundStatusResolved(FibergridFault fault) {
        try {
            if (outboundService == null || !outboundService.isOutboundEnabled()) {
                logger.debug("Outbound service is disabled, skipping status resolved notification");
                return;
            }

            logger.info("Triggering outbound status resolved for fault: id={}, fibergridId={}",
                fault.getId(), fault.getFibergridId());

            FibergridOutboundService.OutboundResult result = outboundService.sendStatusResolved(fault);

            if (result.isSuccess()) {
                logger.info("Outbound status resolved notification sent successfully for fault: {}",
                    fault.getFibergridId());
            } else {
                logger.warn("Outbound status resolved notification failed for fault: {} - {} ({})",
                    fault.getFibergridId(), result.getMessage(), result.getErrorCode());
            }
        } catch (Exception e) {
            // Don't fail the main update operation if outbound call fails
            logger.error("Error triggering outbound status resolved for fault: {}",
                fault.getFibergridId(), e);
        }
    }

    // ==================== Fault Retrieval ====================

    /**
     * Get a fault by its internal ID.
     */
    public FibergridFault getFaultById(Long id) throws FibergridServiceException {
        try {
            FibergridFault fault = dataManager.getFaultById(id);
            if (fault == null) {
                throw new FibergridServiceException(
                    FibergridServiceException.ErrorType.NOT_FOUND,
                    "Fault not found"
                );
            }
            return fault;
        } catch (FibergridServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving fault by id", e);
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.INTERNAL_ERROR,
                "Error retrieving fault",
                e
            );
        }
    }

    /**
     * Get a fault by its FiberGrid ID.
     */
    public FibergridFault getFaultByFibergridId(String fibergridId) throws FibergridServiceException {
        try {
            FibergridFault fault = dataManager.getFaultByFibergridId(fibergridId);
            if (fault == null) {
                throw new FibergridServiceException(
                    FibergridServiceException.ErrorType.NOT_FOUND,
                    "Fault not found"
                );
            }
            return fault;
        } catch (FibergridServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving fault", e);
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.INTERNAL_ERROR,
                "Error retrieving fault",
                e
            );
        }
    }

    /**
     * Get faults with optional filters.
     */
    public List<FibergridFault> getFaultsByCriteria(String status, Integer flagRelated,
                                                      Date fromDate, Date toDate,
                                                      Integer maxResults) throws FibergridServiceException {
        try {
            return dataManager.getFaultsByCriteria(status, flagRelated, fromDate, toDate, maxResults);
        } catch (Exception e) {
            logger.error("Error retrieving faults", e);
            throw new FibergridServiceException(
                FibergridServiceException.ErrorType.INTERNAL_ERROR,
                "Error retrieving faults",
                e
            );
        }
    }

    /**
     * Get faults with optional filters using search criteria object.
     */
    public List<FibergridFault> getFaultsByCriteria(FibergridFaultSearchCriteria criteria)
            throws FibergridServiceException {
        return getFaultsByCriteria(
            criteria.getStatusCode(),
            criteria.getFlagRelated(),
            criteria.getFromDate(),
            criteria.getToDate(),
            criteria.getMaxResults()
        );
    }

    // ==================== API Logging ====================

    /**
     * Log an API call for audit purposes.
     */
    public void logApiCall(FibergridApiLog.Direction direction, String endpoint, String method,
                           String requestId, String clientName, String clientVersion,
                           String requestBody, String responseBody, int httpStatus, long durationMs) {
        try {
            FibergridApiLog log = new FibergridApiLog();
            log.setDirection(direction);
            log.setEndpoint(endpoint);
            log.setMethod(method);
            log.setRequestId(requestId);
            log.setClientName(clientName);
            log.setClientVersion(clientVersion);
            log.setRequestBody(truncateForLog(requestBody, 4000));
            log.setResponseBody(truncateForLog(responseBody, 4000));
            log.setHttpStatus(httpStatus);
            log.setDurationMs(durationMs);

            dataManager.logApiCall(log);
        } catch (Exception e) {
            // Don't fail the main operation if logging fails
            logger.warn("Failed to log API call", e);
        }
    }

    /**
     * Truncate string for logging to avoid database field overflow.
     */
    private String truncateForLog(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }
    
}
