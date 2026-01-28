package gr.deddie.pfr.dao;

import gr.deddie.pfr.model.FibergridApiLog;
import gr.deddie.pfr.model.FibergridFault;
import gr.deddie.pfr.model.FibergridFaultPhoto;
import gr.deddie.pfr.model.FibergridOutboundConfig;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * MyBatis mapper for FiberGrid fault operations.
 * 
 * Table naming convention:
 *   Data tables:   PFR_FBG_*
 *   Lookup tables: H_PFR_FBG_*
 */
public interface FIBERGRIDMapper {

    // ==================== Fault Operations ====================

    /**
     * Insert a new FiberGrid fault record.
     * STATUS_ID is resolved from H_PFR_FBG_STATUS by code.
     */
    @Insert("INSERT INTO PFR_FBG_FAULTS " +
            "(FIBERGRID_ID, PFR_FAILURE_ID, ADDRESS, LATITUDE, LONGITUDE, FLAG_RELATED, STATUS_ID, " +
            "NOTES, DATE_CREATED, DATE_RESOLVED, DATE_VISITED, ESTIMATED_ARRIVAL_TIME_DEDDIE, " +
            "ESTIMATED_ARRIVAL_TIME_FIBERGRID, ROOT_CAUSE, CONTACT_NAME, CONTACT_PHONE, CONTACT_EMAIL, CREATED_BY) " +
            "VALUES " +
            "(#{fibergridId}, #{pfrFailureId}, #{address}, #{latitude}, #{longitude}, #{flagRelated.value}, " +
            "(SELECT ID FROM H_PFR_FBG_STATUS WHERE CODE = #{status.value}), " +
            "#{notes}, #{dateCreated}, #{dateResolved}, #{dateVisited}, #{estimatedArrivalTimeDeddie}, " +
            "#{estimatedArrivalTimeFibergrid}, #{rootCause}, #{contactName}, #{contactPhone}, #{contactEmail}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertFault(FibergridFault fault);

    /**
     * Get fault by internal ID with status and flag descriptions.
     */
    @Select("SELECT f.*, s.CODE AS STATUS_CODE, fl.CODE AS FLAG_CODE " +
            "FROM PFR_FBG_FAULTS f " +
            "JOIN H_PFR_FBG_STATUS s ON f.STATUS_ID = s.ID " +
            "JOIN H_PFR_FBG_FLAG_RELATED fl ON f.FLAG_RELATED = fl.ID " +
            "WHERE f.ID = #{id} AND f.IS_DELETED = 0")
    @Results(id = "fibergridFaultResult", value = {
            @Result(property = "id", column = "ID"),
            @Result(property = "fibergridId", column = "FIBERGRID_ID"),
            @Result(property = "pfrFailureId", column = "PFR_FAILURE_ID"),
            @Result(property = "address", column = "ADDRESS"),
            @Result(property = "latitude", column = "LATITUDE"),
            @Result(property = "longitude", column = "LONGITUDE"),
            @Result(property = "flagRelated", column = "FLAG_RELATED", 
                    typeHandler = gr.deddie.pfr.dao.typehandler.FibergridFlagRelatedTypeHandler.class),
            @Result(property = "status", column = "STATUS_CODE", 
                    typeHandler = gr.deddie.pfr.dao.typehandler.FibergridFaultStatusTypeHandler.class),
            @Result(property = "statusId", column = "STATUS_ID"),
            @Result(property = "notes", column = "NOTES"),
            @Result(property = "dateCreated", column = "DATE_CREATED"),
            @Result(property = "dateResolved", column = "DATE_RESOLVED"),
            @Result(property = "dateVisited", column = "DATE_VISITED"),
            @Result(property = "estimatedArrivalTimeDeddie", column = "ESTIMATED_ARRIVAL_TIME_DEDDIE"),
            @Result(property = "estimatedArrivalTimeFibergrid", column = "ESTIMATED_ARRIVAL_TIME_FIBERGRID"),
            @Result(property = "rootCause", column = "ROOT_CAUSE"),
            @Result(property = "contactName", column = "CONTACT_NAME"),
            @Result(property = "contactPhone", column = "CONTACT_PHONE"),
            @Result(property = "contactEmail", column = "CONTACT_EMAIL"),
            @Result(property = "created", column = "CREATED"),
            @Result(property = "lastUpdated", column = "LAST_UPDATED"),
            @Result(property = "createdBy", column = "CREATED_BY"),
            @Result(property = "updatedBy", column = "UPDATED_BY"),
            @Result(property = "deleted", column = "IS_DELETED")
    })
    FibergridFault getFaultById(@Param("id") Long id);

    /**
     * Get fault by FiberGrid ID.
     */
    @Select("SELECT f.*, s.CODE AS STATUS_CODE, fl.CODE AS FLAG_CODE " +
            "FROM PFR_FBG_FAULTS f " +
            "JOIN H_PFR_FBG_STATUS s ON f.STATUS_ID = s.ID " +
            "JOIN H_PFR_FBG_FLAG_RELATED fl ON f.FLAG_RELATED = fl.ID " +
            "WHERE f.FIBERGRID_ID = #{fibergridId} AND f.IS_DELETED = 0")
    @ResultMap("fibergridFaultResult")
    FibergridFault getFaultByFibergridId(@Param("fibergridId") String fibergridId);

    /**
     * Get fault by PFR Failure ID.
     */
    @Select("SELECT f.*, s.CODE AS STATUS_CODE, fl.CODE AS FLAG_CODE " +
            "FROM PFR_FBG_FAULTS f " +
            "JOIN H_PFR_FBG_STATUS s ON f.STATUS_ID = s.ID " +
            "JOIN H_PFR_FBG_FLAG_RELATED fl ON f.FLAG_RELATED = fl.ID " +
            "WHERE f.PFR_FAILURE_ID = #{pfrFailureId} AND f.IS_DELETED = 0")
    @ResultMap("fibergridFaultResult")
    FibergridFault getFaultByPfrFailureId(@Param("pfrFailureId") Long pfrFailureId);

    /**
     * Check if a fault with the given FiberGrid ID exists.
     */
    @Select("SELECT COUNT(1) FROM PFR_FBG_FAULTS WHERE FIBERGRID_ID = #{fibergridId} AND IS_DELETED = 0")
    boolean existsByFibergridId(@Param("fibergridId") String fibergridId);

    /**
     * Update fault fields. Uses dynamic SQL via XML mapper.
     */
    int updateFault(FibergridFault fault);

    /**
     * Update only the status of a fault.
     */
    @Update("UPDATE PFR_FBG_FAULTS SET " +
            "STATUS_ID = (SELECT ID FROM H_PFR_FBG_STATUS WHERE CODE = #{status.value}), " +
            "UPDATED_BY = #{updatedBy} " +
            "WHERE ID = #{id}")
    int updateFaultStatus(@Param("id") Long id, 
                          @Param("status") gr.deddie.pfr.model.FibergridFaultStatus status,
                          @Param("updatedBy") String updatedBy);

    /**
     * Link a PFR Failure ID to a FiberGrid fault.
     */
    @Update("UPDATE PFR_FBG_FAULTS SET PFR_FAILURE_ID = #{pfrFailureId}, UPDATED_BY = #{updatedBy} WHERE ID = #{id}")
    int linkPfrFailureId(@Param("id") Long id, 
                         @Param("pfrFailureId") Long pfrFailureId,
                         @Param("updatedBy") String updatedBy);

    /**
     * Append notes to existing notes.
     */
    @Update("UPDATE PFR_FBG_FAULTS SET " +
            "NOTES = CASE WHEN NOTES IS NULL THEN #{notes} ELSE NOTES || CHR(10) || #{notes} END, " +
            "UPDATED_BY = #{updatedBy} " +
            "WHERE ID = #{id}")
    int appendNotes(@Param("id") Long id, 
                    @Param("notes") String notes,
                    @Param("updatedBy") String updatedBy);

    /**
     * Soft delete a fault.
     */
    @Update("UPDATE PFR_FBG_FAULTS SET IS_DELETED = 1, UPDATED_BY = #{updatedBy} WHERE ID = #{id}")
    int deleteFault(@Param("id") Long id, @Param("updatedBy") String updatedBy);

    /**
     * Get faults with filters. Uses dynamic SQL via XML mapper.
     */
    List<FibergridFault> getFaultsByCriteria(
            @Param("statusCode") String statusCode,
            @Param("flagRelated") Integer flagRelated,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("maxResults") Integer maxResults);

    // ==================== Photo Operations ====================

    /**
     * Insert a fault photo.
     */
    @Insert("INSERT INTO PFR_FBG_FAULT_PHOTOS (FAULT_ID, PHOTO_DATA, PHOTO_SIZE, CONTENT_TYPE, FILE_NAME) " +
            "VALUES (#{faultId}, #{photoData}, #{photoSize}, #{contentType}, #{fileName})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertPhoto(FibergridFaultPhoto photo);

    /**
     * Get photos for a fault.
     */
    @Select("SELECT * FROM PFR_FBG_FAULT_PHOTOS WHERE FAULT_ID = #{faultId}")
    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "faultId", column = "FAULT_ID"),
            @Result(property = "photoData", column = "PHOTO_DATA"),
            @Result(property = "photoSize", column = "PHOTO_SIZE"),
            @Result(property = "contentType", column = "CONTENT_TYPE"),
            @Result(property = "fileName", column = "FILE_NAME"),
            @Result(property = "created", column = "CREATED")
    })
    List<FibergridFaultPhoto> getPhotosByFaultId(@Param("faultId") Long faultId);

    /**
     * Get photo count for a fault.
     */
    @Select("SELECT COUNT(1) FROM PFR_FBG_FAULT_PHOTOS WHERE FAULT_ID = #{faultId}")
    int getPhotoCountByFaultId(@Param("faultId") Long faultId);

    // ==================== API Log Operations ====================

    /**
     * Insert an API log entry.
     */
    @Insert("INSERT INTO PFR_FBG_API_LOGS " +
            "(DIRECTION, ENDPOINT, METHOD, REQUEST_ID, CLIENT_NAME, CLIENT_VERSION, " +
            "REQUEST_BODY, RESPONSE_BODY, HTTP_STATUS, DURATION_MS, ERROR_MESSAGE) " +
            "VALUES " +
            "(#{direction}, #{endpoint}, #{method}, #{requestId}, #{clientName}, #{clientVersion}, " +
            "#{requestBody}, #{responseBody}, #{httpStatus}, #{durationMs}, #{errorMessage})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertApiLog(FibergridApiLog log);

    // ==================== Token Validation ====================

    /**
     * Validate API token. Returns the client name if valid, null otherwise.
     */
    @Select("SELECT CLIENT_NAME FROM H_PFR_FBG_API_TOKENS " +
            "WHERE TOKEN = #{token} AND IS_ACTIVE = 1 " +
            "AND (VALID_FROM IS NULL OR VALID_FROM <= SYSTIMESTAMP) " +
            "AND (VALID_TO IS NULL OR VALID_TO >= SYSTIMESTAMP)")
    String validateToken(@Param("token") String token);

    // ==================== Lookup Operations ====================

    /**
     * Get status ID by code.
     */
    @Select("SELECT ID FROM H_PFR_FBG_STATUS WHERE CODE = #{code} AND IS_ACTIVE = 1")
    Integer getStatusIdByCode(@Param("code") String code);

    /**
     * Get all active statuses.
     */
    @Select("SELECT ID, CODE, DESCRIPTION, DESCRIPTION_GR FROM H_PFR_FBG_STATUS " +
            "WHERE IS_ACTIVE = 1 ORDER BY SORT_ORDER")
    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "code", column = "CODE"),
            @Result(property = "description", column = "DESCRIPTION"),
            @Result(property = "descriptionGr", column = "DESCRIPTION_GR")
    })
    List<java.util.Map<String, Object>> getAllStatuses();

    /**
     * Get all active flag values.
     */
    @Select("SELECT ID, CODE, DESCRIPTION, DESCRIPTION_GR FROM H_PFR_FBG_FLAG_RELATED " +
            "WHERE IS_ACTIVE = 1 ORDER BY SORT_ORDER")
    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "code", column = "CODE"),
            @Result(property = "description", column = "DESCRIPTION"),
            @Result(property = "descriptionGr", column = "DESCRIPTION_GR")
    })
    List<java.util.Map<String, Object>> getAllFlags();

    // ==================== Outbound Configuration ====================

    /**
     * Get outbound API configuration.
     * Returns the active configuration for making outbound calls to Fibergrid.
     */
    @Select("SELECT * FROM H_PFR_FBG_OUTBOUND_CONFIG WHERE IS_ACTIVE = 1")
    @Results({
            @Result(property = "id", column = "ID"),
            @Result(property = "baseUrl", column = "BASE_URL"),
            @Result(property = "apiToken", column = "API_TOKEN"),
            @Result(property = "clientName", column = "CLIENT_NAME"),
            @Result(property = "clientVersion", column = "CLIENT_VERSION"),
            @Result(property = "enabled", column = "IS_ENABLED"),
            @Result(property = "active", column = "IS_ACTIVE"),
            @Result(property = "maxRetries", column = "MAX_RETRIES"),
            @Result(property = "initialDelayMs", column = "INITIAL_DELAY_MS"),
            @Result(property = "backoffMultiplier", column = "BACKOFF_MULTIPLIER"),
            @Result(property = "maxDelayMs", column = "MAX_DELAY_MS"),
            @Result(property = "connectTimeoutMs", column = "CONNECT_TIMEOUT_MS"),
            @Result(property = "readTimeoutMs", column = "READ_TIMEOUT_MS"),
            @Result(property = "created", column = "CREATED"),
            @Result(property = "lastUpdated", column = "LAST_UPDATED")
    })
    FibergridOutboundConfig getOutboundConfig();

    /**
     * Check if a request_id has already been successfully processed (for idempotency).
     * Used to prevent duplicate outbound calls on retries.
     */
    @Select("SELECT COUNT(1) FROM PFR_FBG_API_LOGS " +
            "WHERE DIRECTION = 'OUTBOUND' AND REQUEST_ID = #{requestId} AND HTTP_STATUS BETWEEN 200 AND 299")
    boolean isRequestIdProcessed(@Param("requestId") String requestId);

}
