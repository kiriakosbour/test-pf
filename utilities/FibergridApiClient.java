package gr.deddie.pfr.utilities;

import gr.deddie.pfr.model.FibergridOutboundConfig;
import gr.deddie.pfr.model.FibergridOutboundUpdateRequest;
import gr.deddie.pfr.model.FibergridOutboundUpdateResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * HTTP client for outbound Fibergrid API calls with retry and idempotency support.
 *
 * Features:
 * - Exponential backoff retry for transient errors
 * - Idempotency via x-request-id header
 * - Configurable timeouts
 * - Distinguishes retryable vs non-retryable errors
 */
public class FibergridApiClient {

    private static final Logger logger = LogManager.getLogger(FibergridApiClient.class);

    private final FibergridOutboundConfig config;

    public FibergridApiClient(FibergridOutboundConfig config) {
        this.config = config;
    }

    /**
     * POST to /fibergrid/faults/update with retry logic.
     *
     * @param request   The update request
     * @param requestId UUID for idempotency (same ID = same request on retries)
     * @return Response from Fibergrid
     * @throws FibergridApiException on unrecoverable error
     */
    public FibergridOutboundUpdateResponse postUpdate(FibergridOutboundUpdateRequest request, String requestId)
            throws FibergridApiException {

        String url = config.getFullUrl("/fibergrid/faults/update");

        int attempt = 0;
        long delayMs = config.getInitialDelayMs();
        Exception lastException = null;
        int lastHttpStatus = 0;

        while (attempt <= config.getMaxRetries()) {
            attempt++;

            try {
                logger.info("Outbound API call attempt {}/{} - requestId: {}, fibergridId: {}, url: {}",
                        attempt, config.getMaxRetries() + 1, requestId, request.getFibergridId(), url);

                ApiCallResult result = executePost(url, request, requestId);
                lastHttpStatus = result.httpStatus;

                if (result.response != null && result.response.isSuccess()) {
                    logger.info("Outbound API call successful on attempt {} - requestId: {}",
                            attempt, requestId);
                    return result.response;
                }

                // Handle error response
                if (result.response != null) {
                    if (isRetryableHttpStatus(result.httpStatus)) {
                        throw new RetryableException(
                                "HTTP " + result.httpStatus + ": " + result.response.getError(),
                                result.response.getErrorCode(),
                                result.httpStatus
                        );
                    } else {
                        throw new NonRetryableException(
                                "HTTP " + result.httpStatus + ": " + result.response.getError(),
                                result.response.getErrorCode(),
                                result.httpStatus
                        );
                    }
                }

            } catch (RetryableException e) {
                lastException = e;
                lastHttpStatus = e.httpStatus;
                logger.warn("Retryable error on attempt {}: {} - {} (HTTP {})",
                        attempt, e.errorCode, e.getMessage(), e.httpStatus);

                if (attempt <= config.getMaxRetries()) {
                    logger.info("Waiting {}ms before retry...", delayMs);
                    sleep(delayMs);
                    delayMs = Math.min(
                            (long) (delayMs * config.getBackoffMultiplier()),
                            config.getMaxDelayMs()
                    );
                }

            } catch (NonRetryableException e) {
                logger.error("Non-retryable error: {} - {} (HTTP {})",
                        e.errorCode, e.getMessage(), e.httpStatus);
                throw new FibergridApiException(e.getMessage(), e.errorCode, e.httpStatus, false);
            }
        }

        // All retries exhausted
        String errorMessage = "Failed after " + (config.getMaxRetries() + 1) + " attempts";
        if (lastException != null) {
            errorMessage += ": " + lastException.getMessage();
        }
        logger.error("All {} attempts failed for requestId: {} - {}",
                config.getMaxRetries() + 1, requestId, errorMessage);

        throw new FibergridApiException(errorMessage, "RETRY_EXHAUSTED", lastHttpStatus, true);
    }

    /**
     * Execute a single POST request.
     */
    private ApiCallResult executePost(String url, FibergridOutboundUpdateRequest request, String requestId)
            throws RetryableException, NonRetryableException {

        Client client = ClientBuilder.newBuilder()
                .connectTimeout(config.getConnectTimeoutMs(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeoutMs(), TimeUnit.MILLISECONDS)
                .build();

        Response response = null;
        try {
            response = client.target(url)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + config.getApiToken())
                    .header("x-client-name", config.getClientName())
                    .header("x-client-version", config.getClientVersion())
                    .header("x-request-id", requestId)
                    .post(Entity.json(request));

            int status = response.getStatus();
            logger.debug("Received HTTP {} from Fibergrid", status);

            // Try to read response body
            FibergridOutboundUpdateResponse responseBody = null;
            try {
                responseBody = response.readEntity(FibergridOutboundUpdateResponse.class);
            } catch (Exception e) {
                logger.warn("Could not parse response body: {}", e.getMessage());
            }

            // Success case
            if (status >= 200 && status < 300) {
                if (responseBody == null) {
                    responseBody = FibergridOutboundUpdateResponse.success("Request accepted");
                }
                return new ApiCallResult(status, responseBody);
            }

            // Error case - create response if not parsed
            if (responseBody == null) {
                responseBody = FibergridOutboundUpdateResponse.error(
                        "HTTP " + status,
                        "HTTP_ERROR",
                        null
                );
            }

            return new ApiCallResult(status, responseBody);

        } catch (ProcessingException e) {
            // Network errors - retry
            logger.warn("Network error: {}", e.getMessage());
            throw new RetryableException("Network error: " + e.getMessage(), "NETWORK_ERROR", 0);

        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    logger.debug("Error closing response: {}", e.getMessage());
                }
            }
            try {
                client.close();
            } catch (Exception e) {
                logger.debug("Error closing client: {}", e.getMessage());
            }
        }
    }

    /**
     * Check if HTTP status code indicates a retryable error.
     */
    private boolean isRetryableHttpStatus(int status) {
        return status >= 500 ||  // Server errors
                status == 408 ||  // Request timeout
                status == 429;    // Too many requests (rate limit)
    }

    /**
     * Sleep for specified milliseconds.
     */
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted");
        }
    }

    /**
     * Internal class to hold API call result.
     */
    private static class ApiCallResult {
        final int httpStatus;
        final FibergridOutboundUpdateResponse response;

        ApiCallResult(int httpStatus, FibergridOutboundUpdateResponse response) {
            this.httpStatus = httpStatus;
            this.response = response;
        }
    }

    /**
     * Exception thrown when API call fails.
     */
    public static class FibergridApiException extends Exception {
        private final String errorCode;
        private final int httpStatus;
        private final boolean retryable;

        public FibergridApiException(String message, String errorCode, int httpStatus, boolean retryable) {
            super(message);
            this.errorCode = errorCode;
            this.httpStatus = httpStatus;
            this.retryable = retryable;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public boolean isRetryable() {
            return retryable;
        }
    }

    /**
     * Internal exception for retryable errors.
     */
    private static class RetryableException extends Exception {
        final String errorCode;
        final int httpStatus;

        RetryableException(String message, String errorCode, int httpStatus) {
            super(message);
            this.errorCode = errorCode;
            this.httpStatus = httpStatus;
        }
    }

    /**
     * Internal exception for non-retryable errors.
     */
    private static class NonRetryableException extends Exception {
        final String errorCode;
        final int httpStatus;

        NonRetryableException(String message, String errorCode, int httpStatus) {
            super(message);
            this.errorCode = errorCode;
            this.httpStatus = httpStatus;
        }
    }
}
