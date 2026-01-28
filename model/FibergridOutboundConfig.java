package gr.deddie.pfr.model;

import java.util.Date;

/**
 * Configuration for outbound Fibergrid API calls.
 * Loaded from H_PFR_FBG_OUTBOUND_CONFIG table.
 */
public class FibergridOutboundConfig {

    private Long id;

    // API Configuration
    private String baseUrl;
    private String apiToken;
    private String clientName;
    private String clientVersion;
    private boolean enabled;
    private boolean active;

    // Retry Configuration
    private int maxRetries;
    private long initialDelayMs;
    private double backoffMultiplier;
    private long maxDelayMs;

    // Timeout Configuration
    private int connectTimeoutMs;
    private int readTimeoutMs;

    // Audit fields
    private Date created;
    private Date lastUpdated;

    public FibergridOutboundConfig() {
        // Set defaults
        this.clientName = "PFR_HEDNO";
        this.clientVersion = "1.0.0";
        this.enabled = false;
        this.active = true;
        this.maxRetries = 3;
        this.initialDelayMs = 1000L;
        this.backoffMultiplier = 2.0;
        this.maxDelayMs = 30000L;
        this.connectTimeoutMs = 5000;
        this.readTimeoutMs = 30000;
    }

    /**
     * Check if outbound calls are allowed.
     */
    public boolean isOutboundAllowed() {
        return enabled && active && baseUrl != null && apiToken != null;
    }

    /**
     * Get the full URL for an endpoint.
     */
    public String getFullUrl(String endpoint) {
        if (baseUrl == null) {
            return endpoint;
        }
        String base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String path = endpoint.startsWith("/") ? endpoint : "/" + endpoint;
        return base + path;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getInitialDelayMs() {
        return initialDelayMs;
    }

    public void setInitialDelayMs(long initialDelayMs) {
        this.initialDelayMs = initialDelayMs;
    }

    public double getBackoffMultiplier() {
        return backoffMultiplier;
    }

    public void setBackoffMultiplier(double backoffMultiplier) {
        this.backoffMultiplier = backoffMultiplier;
    }

    public long getMaxDelayMs() {
        return maxDelayMs;
    }

    public void setMaxDelayMs(long maxDelayMs) {
        this.maxDelayMs = maxDelayMs;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "FibergridOutboundConfig{" +
                "id=" + id +
                ", baseUrl='" + baseUrl + '\'' +
                ", clientName='" + clientName + '\'' +
                ", enabled=" + enabled +
                ", maxRetries=" + maxRetries +
                '}';
    }
}
