package gr.deddie.pfr.model;

import java.util.Date;

/**
 * Entity class for logging API requests and responses.
 * Maps to the PFR_FIBERGRID_API_LOGS table.
 */
public class FibergridApiLog {

    public enum Direction {
        INBOUND,
        OUTBOUND
    }

    private Long id;
    private Direction direction;
    private String endpoint;
    private String method;
    private String requestId;
    private String clientName;
    private String clientVersion;
    private String requestBody;
    private String responseBody;
    private Integer httpStatus;
    private Long durationMs;
    private String errorMessage;
    private Date created;

    public FibergridApiLog() {
    }

    public static FibergridApiLog createInbound(String endpoint, String method, String requestId,
                                                  String clientName, String clientVersion) {
        FibergridApiLog log = new FibergridApiLog();
        log.setDirection(Direction.INBOUND);
        log.setEndpoint(endpoint);
        log.setMethod(method);
        log.setRequestId(requestId);
        log.setClientName(clientName);
        log.setClientVersion(clientVersion);
        return log;
    }

    public static FibergridApiLog createOutbound(String endpoint, String method) {
        FibergridApiLog log = new FibergridApiLog();
        log.setDirection(Direction.OUTBOUND);
        log.setEndpoint(endpoint);
        log.setMethod(method);
        return log;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
