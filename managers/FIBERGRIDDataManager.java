package gr.deddie.pfr.managers;

import gr.deddie.pfr.dao.FIBERGRIDMapper;
import gr.deddie.pfr.dao.MyBatisConnectionFactory;
import gr.deddie.pfr.model.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Data Manager for FiberGrid fault operations.
 * Follows the same patterns as PFRDataManager and POWEROUTAGEDataManager.
 * 
 * All database operations are performed through MyBatis sessions with proper
 * transaction management and error handling.
 */
public class FIBERGRIDDataManager {
    
    private static final Logger logger = LogManager.getLogger(FIBERGRIDDataManager.class);

    // ==================== Fault Creation ====================

    /**
     * Create a new FiberGrid fault record.
     * 
     * @param fault The fault entity to persist
     * @param photos Optional list of base64-encoded photos
     * @return The created fault with generated ID, or null on error
     */
    public FibergridFault createFault(FibergridFault fault, List<String> photos) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);

            // Insert the fault record
            int result = mapper.insertFault(fault);
            if (result != 1) {
                logger.error("Failed to insert fault record");
                return null;
            }

            // Insert photos if provided
            if (photos != null && !photos.isEmpty()) {
                int photoIndex = 1;
                for (String base64Photo : photos) {
                    try {
                        byte[] photoData = Base64.getDecoder().decode(base64Photo);
                        FibergridFaultPhoto photo = new FibergridFaultPhoto(fault.getId(), photoData);
                        photo.setContentType("image/jpeg"); // Default, could be detected
                        photo.setFileName("photo_" + photoIndex + ".jpg");
                        mapper.insertPhoto(photo);
                        photoIndex++;
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid base64 photo data, skipping: " + e.getMessage());
                    }
                }
            }

            sqlSession.commit();
            logger.info("Created FiberGrid fault with ID: " + fault.getId() + ", fibergridId: " + fault.getFibergridId());
            return fault;

        } catch (Exception e) {
            sqlSession.rollback();
            logger.error("Exception in createFault", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    // ==================== Fault Retrieval ====================

    /**
     * Get a fault by its internal database ID.
     */
    public FibergridFault getFaultById(Long id) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            return mapper.getFaultById(id);
        } catch (Exception e) {
            logger.error("Exception in getFaultById", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Get a fault by its FiberGrid ID.
     */
    public FibergridFault getFaultByFibergridId(String fibergridId) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            return mapper.getFaultByFibergridId(fibergridId);
        } catch (Exception e) {
            logger.error("Exception in getFaultByFibergridId", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Get a fault by its PFR Failure ID.
     */
    public FibergridFault getFaultByPfrFailureId(Long pfrFailureId) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            return mapper.getFaultByPfrFailureId(pfrFailureId);
        } catch (Exception e) {
            logger.error("Exception in getFaultByPfrFailureId", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Check if a fault with the given FiberGrid ID already exists.
     */
    public boolean existsByFibergridId(String fibergridId) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            return mapper.existsByFibergridId(fibergridId);
        } catch (Exception e) {
            logger.error("Exception in existsByFibergridId", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Get faults with optional filters.
     */
    public List<FibergridFault> getFaultsByCriteria(String status, Integer flagRelated,
                                                      Date fromDate, Date toDate, Integer maxResults) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            return mapper.getFaultsByCriteria(status, flagRelated, fromDate, toDate, maxResults);
        } catch (Exception e) {
            logger.error("Exception in getFaultsByCriteria", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    // ==================== Fault Updates ====================

    /**
     * Update a fault record.
     * 
     * @param fault The fault with updated fields
     * @param photos Optional list of new base64-encoded photos to add
     * @return Number of rows updated (1 on success)
     */
    public int updateFault(FibergridFault fault, List<String> photos) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);

            int result = mapper.updateFault(fault);

            // Insert new photos if provided
            if (photos != null && !photos.isEmpty()) {
                // Get current photo count to continue numbering
                int existingCount = mapper.getPhotoCountByFaultId(fault.getId());
                int photoIndex = existingCount + 1;
                
                for (String base64Photo : photos) {
                    try {
                        byte[] photoData = Base64.getDecoder().decode(base64Photo);
                        FibergridFaultPhoto photo = new FibergridFaultPhoto(fault.getId(), photoData);
                        photo.setContentType("image/jpeg");
                        photo.setFileName("photo_" + photoIndex + ".jpg");
                        mapper.insertPhoto(photo);
                        photoIndex++;
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid base64 photo data, skipping: " + e.getMessage());
                    }
                }
            }

            sqlSession.commit();
            logger.info("Updated FiberGrid fault with ID: " + fault.getId());
            return result;

        } catch (Exception e) {
            sqlSession.rollback();
            logger.error("Exception in updateFault", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Update only the status of a fault.
     */
    public int updateFaultStatus(Long id, FibergridFaultStatus status, String updatedBy) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            int result = mapper.updateFaultStatus(id, status, updatedBy);
            sqlSession.commit();
            logger.info("Updated status for fault ID: " + id + " to: " + status);
            return result;
        } catch (Exception e) {
            sqlSession.rollback();
            logger.error("Exception in updateFaultStatus", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Append notes to a fault. Preserves existing notes with newline separator.
     */
    public int appendNotes(Long id, String notes, String updatedBy) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            int result = mapper.appendNotes(id, notes, updatedBy);
            sqlSession.commit();
            return result;
        } catch (Exception e) {
            sqlSession.rollback();
            logger.error("Exception in appendNotes", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Link a PFR Failure ID to a FiberGrid fault.
     */
    public int linkPfrFailureId(Long id, Long pfrFailureId, String updatedBy) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            int result = mapper.linkPfrFailureId(id, pfrFailureId, updatedBy);
            sqlSession.commit();
            logger.info("Linked PFR Failure ID: " + pfrFailureId + " to FiberGrid fault ID: " + id);
            return result;
        } catch (Exception e) {
            sqlSession.rollback();
            logger.error("Exception in linkPfrFailureId", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * Soft delete a fault.
     */
    public int deleteFault(Long id, String updatedBy) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            int result = mapper.deleteFault(id, updatedBy);
            sqlSession.commit();
            logger.info("Soft deleted FiberGrid fault with ID: " + id);
            return result;
        } catch (Exception e) {
            sqlSession.rollback();
            logger.error("Exception in deleteFault", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    // ==================== Photo Operations ====================

    /**
     * Get all photos for a fault.
     */
    public List<FibergridFaultPhoto> getPhotosByFaultId(Long faultId) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            return mapper.getPhotosByFaultId(faultId);
        } catch (Exception e) {
            logger.error("Exception in getPhotosByFaultId", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    // ==================== Token Validation ====================

    /**
     * Validate an API token.
     * 
     * @param token The bearer token (without "Bearer " prefix)
     * @return The client name if valid, null otherwise
     */
    public String validateToken(String token) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            return mapper.validateToken(token);
        } catch (Exception e) {
            logger.error("Exception in validateToken", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    // ==================== API Logging ====================

    /**
     * Log an API request/response.
     */
    public void logApiCall(FibergridApiLog log) {
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FIBERGRIDMapper mapper = sqlSession.getMapper(FIBERGRIDMapper.class);
            mapper.insertApiLog(log);
            sqlSession.commit();
        } catch (Exception e) {
            // Don't fail the main operation if logging fails
            logger.error("Exception in logApiCall", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }
}
