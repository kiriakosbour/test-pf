package gr.deddie.pfr.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gr.deddie.pfr.managers.FIBERGRIDDataManager;
import gr.deddie.pfr.model.*;
import gr.deddie.pfr.utilities.FibergridApiClient;
import gr.deddie.pfr.utilities.FibergridApiClient.FibergridApiException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.UUID;

/**
 * Service for outbound calls from PFR to Fibergrid.
 * Handles prerequisite validation, request building, and response processing.
 *
 * Features:
 * - Prerequisite validation (fibergrid_id must exist)
 * - Automatic trigger on status change to Resolved
 * - API call logging for audit
 * - Structured error handling
 */
public class FibergridOutboundService {

    private static final Logger logger = LogManager.getLogger(FibergridOutboundService.class);
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private final FIBERGRIDDataManager dataManager;
    private FibergridApiClient apiClient;
    private FibergridOutboundConfig config;

    public FibergridOutboundService() {
        this.dataManager = new FIBERGRIDDataManager();
        loadConfiguration();
    }

    /**
     * Constructor for testing with mock dependencies.
     */
    public FibergridOutboundService(FIBERGRIDDataManager dataManager, FibergridApiClient apiClient,
                                     FibergridOutboundConfig config) {
        this.dataManager = dataManager;
        this.apiClient = apiClient;
        this.config = config;
    }

    /**
     * Load outbound configuration from database.
     */
    private void loadConfiguration() {
        try {
            this.config = dataManager.getOutboundConfig();
            if (this.config != null && this.config.isOutboundAllowed()) {
                this.apiClient = new FibergridApiClient(config);
                logger.info("Outbound service initialized - baseUrl: {}, enabled: {}",
                        config.getBaseUrl(), config.isEnabled());
            } else {
                logger.info("Outbound service disabled or not configured");
            }
        } catch (Exception e) {
            logger.error("Failed to load outbound configuration", e);
            this.config = null;
            this.apiClient = null;
        }
    }

    /**
     * Reload configuration (call this if config changes).
     */
    public void reloadConfiguration() {
        loadConfiguration();
    }

    /**
     * Result object for outbound operations.
     */
    public static class OutboundResult {
        private final boolean success;
        private final String message;
        private final String errorCode;
        private final boolean retryable;
        private final int httpStatus;

        private OutboundResult(boolean success, String message, String errorCode,
                               boolean retryable, int httpStatus) {
            this.success = success;
            this.message = message;
            this.errorCode = errorCode;
            this.retryable = retryable;
            this.httpStatus = httpStatus;
        }

        public static OutboundResult success(String message) {
            return new OutboundResult(true, message, null, false, 200);
        }

        public static OutboundResult error(String message, String errorCode, boolean retryable) {
            return new OutboundResult(false, message, errorCode, retryable, 0);
        }

        public static OutboundResult error(String message, String errorCode, boolean retryable, int httpStatus) {
            return new OutboundResult(false, message, errorCode, retryable, httpStatus);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public boolean isRetryable() {
            return retryable;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        @Override
        public String toString() {
            return "OutboundResult{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", errorCode='" + errorCode + '\'' +
                    ", retryable=" + retryable +
                    '}';
        }
    }

    /**
     * Send an update to Fibergrid.
     *
     * @param fibergridId The Fibergrid fault ID (required, must exist in PFR)
     * @param request     The update data
     * @param triggeredBy Who triggered this call (user or system)
     * @return Result with success/failure info
     */
    public OutboundResult sendUpdate(String fibergridId, FibergridOutboundUpdateRequest request,
                                      String triggeredBy) {

        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        String requestBody = null;
        String responseBody = null;
        int httpStatus = 0;
        String errorMessage = null;

        try {
            // Check if outbound is enabled
            if (config == null || !config.isOutboundAllowed()) {
                logger.warn("Outbound API calls are disabled - fibergridId: {}", fibergridId);
                return OutboundResult.error(
                        "Outbound API calls are disabled", "OUTBOUND_DISABLED", false);
            }

            // Validate fibergrid_id
            if (fibergridId == null || fibergridId.trim().isEmpty()) {
                return OutboundResult.error(
                        "fibergrid_id is required", "VALIDATION_ERROR", false);
            }

            // Prerequisite: fibergrid_id must exist in PFR
            FibergridFault fault = dataManager.getFaultByFibergridId(fibergridId);
            if (fault == null) {
                logger.warn("Fault with fibergrid_id '{}' not found in PFR", fibergridId);
                return OutboundResult.error(
                        "Fault with fibergrid_id '" + fibergridId + "' not found in PFR",
                        "NOT_FOUND", false);
            }

            // Ensure fibergrid_id is set in request
            request.setFibergridId(fibergridId);
            requestBody = toJson(request);

            logger.info("Sending outbound update - requestId: {}, fibergridId: {}, triggeredBy: {}",
                    requestId, fibergridId, triggeredBy);

            // Call Fibergrid API
            FibergridOutboundUpdateResponse response = apiClient.postUpdate(request, requestId);
            httpStatus = 200;
            responseBody = toJson(response);

            logger.info("Outbound update successful - requestId: {}, message: {}",
                    requestId, response.getMessage());

            return OutboundResult.success(response.getMessage());

        } catch (FibergridApiException e) {
            httpStatus = e.getHttpStatus() > 0 ? e.getHttpStatus() : (e.isRetryable() ? 503 : 400);
            errorMessage = e.getMessage();
            responseBody = "{\"success\":false,\"error\":\"" + e.getMessage() +
                    "\",\"error_code\":\"" + e.getErrorCode() + "\"}";

            logger.error("Outbound update failed - requestId: {}, error: {}, code: {}, retryable: {}",
                    requestId, e.getMessage(), e.getErrorCode(), e.isRetryable());

            return OutboundResult.error(e.getMessage(), e.getErrorCode(), e.isRetryable(), httpStatus);

        } catch (Exception e) {
            httpStatus = 500;
            errorMessage = e.getMessage();
            logger.error("Unexpected error in outbound update - requestId: {}", requestId, e);

            return OutboundResult.error(
                    "Internal error: " + e.getMessage(), "INTERNAL_ERROR", false);

        } finally {
            // Log the API call
            long durationMs = System.currentTimeMillis() - startTime;
            logOutboundCall(requestId, fibergridId, triggeredBy, requestBody, responseBody,
                    httpStatus, durationMs, errorMessage);
        }
    }

    /**
     * Trigger automatic update when status changes to Resolved.
     * Called by FibergridService when a fault is resolved.
     *
     * @param fault The fault that was resolved
     * @return Result with success/failure info
     */
    public OutboundResult sendStatusResolved(FibergridFault fault) {
        if (fault == null) {
            return OutboundResult.error("Fault is null", "VALIDATION_ERROR", false);
        }

        if (fault.getFibergridId() == null || fault.getFibergridId().trim().isEmpty()) {
            logger.debug("No fibergrid_id linked to fault id: {}", fault.getId());
            return OutboundResult.error("No fibergrid_id linked", "NO_FIBERGRID_ID", false);
        }

        logger.info("Triggering outbound status update for resolved fault - id: {}, fibergridId: {}",
                fault.getId(), fault.getFibergridId());

        FibergridOutboundUpdateRequest request = FibergridOutboundUpdateRequest
                .builder(fault.getFibergridId())
                .status("Resolved")
                .dateResolved(fault.getDateResolved() != null ? fault.getDateResolved() : new Date())
                .rootCause(fault.getRootCause())
                .notes(fault.getNotes())
                .hednoEta(fault.getEstimatedArrivalTimeDeddie())
                .fibergridEta(fault.getEstimatedArrivalTimeFibergrid())
                .contactInformation(fault.getContactInformation())
                .build();

        return sendUpdate(fault.getFibergridId(), request, "SYSTEM_STATUS_RESOLVED");
    }

    /**
     * Send a general update from a fault entity.
     * Useful for manual updates from UI.
     *
     * @param fault       The fault with updated data
     * @param triggeredBy Who triggered this call
     * @return Result with success/failure info
     */
    public OutboundResult sendFaultUpdate(FibergridFault fault, String triggeredBy) {
        if (fault == null || fault.getFibergridId() == null) {
            return OutboundResult.error("Invalid fault or missing fibergrid_id", "VALIDATION_ERROR", false);
        }

        FibergridOutboundUpdateRequest request = FibergridOutboundUpdateRequest.fromFault(fault);
        return sendUpdate(fault.getFibergridId(), request, triggeredBy);
    }

    /**
     * Check if outbound calls are enabled.
     */
    public boolean isOutboundEnabled() {
        return config != null && config.isOutboundAllowed();
    }

    /**
     * Log outbound API call for audit purposes.
     */
    private void logOutboundCall(String requestId, String fibergridId, String triggeredBy,
                                  String requestBody, String responseBody,
                                  int httpStatus, long durationMs, String errorMessage) {
        try {
            FibergridApiLog log = FibergridApiLog.createOutbound("/fibergrid/faults/update", "POST");
            log.setRequestId(requestId);
            log.setClientName(config != null ? config.getClientName() : "PFR");
            log.setClientVersion(config != null ? config.getClientVersion() : "1.0");
            log.setRequestBody(truncate(requestBody, 4000));
            log.setResponseBody(truncate(responseBody, 4000));
            log.setHttpStatus(httpStatus);
            log.setDurationMs(durationMs);
            log.setErrorMessage(truncate(errorMessage, 4000));

            dataManager.logApiCall(log);

            logger.debug("Logged outbound API call - requestId: {}, httpStatus: {}, durationMs: {}",
                    requestId, httpStatus, durationMs);

        } catch (Exception e) {
            logger.warn("Failed to log outbound API call - requestId: {}", requestId, e);
        }
    }

    /**
     * Truncate string for logging to avoid database field overflow.
     */
    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
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
        } catch (Exception e) {
            logger.warn("Failed to serialize object to JSON", e);
            return obj.toString();
        }
    }
}
