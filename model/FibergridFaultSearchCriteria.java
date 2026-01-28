package gr.deddie.pfr.model;

import java.util.Date;

/**
 * Search criteria for querying FiberGrid faults.
 * Used by the mapper's dynamic query.
 */
public class FibergridFaultSearchCriteria {

    private String statusCode;
    private Integer flagRelated;
    private Date fromDate;
    private Date toDate;
    private Integer maxResults;

    public FibergridFaultSearchCriteria() {
        this.maxResults = 1000; // Default limit
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getFlagRelated() {
        return flagRelated;
    }

    public void setFlagRelated(Integer flagRelated) {
        this.flagRelated = flagRelated;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }
}