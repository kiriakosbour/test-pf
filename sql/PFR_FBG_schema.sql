-- ============================================================================
-- PFR FiberGrid Integration - Database Schema
-- Schema: APPSTD
-- Oracle 19c compatible
-- 
-- Naming Convention:
--   Data tables:   PFR_FBG_*
--   Lookup tables: H_PFR_FBG_*
-- ============================================================================

-- ============================================================================
-- LOOKUP TABLES (H_PFR_FBG_*)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- H_PFR_FBG_STATUS: Fault status lookup
-- ----------------------------------------------------------------------------
CREATE TABLE H_PFR_FBG_STATUS (
    ID              NUMBER(2) PRIMARY KEY,
    CODE            VARCHAR2(50) NOT NULL,
    DESCRIPTION     VARCHAR2(200),
    DESCRIPTION_GR  VARCHAR2(200),
    IS_ACTIVE       NUMBER(1) DEFAULT 1 NOT NULL,
    SORT_ORDER      NUMBER(3),
    CREATED         TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    
    CONSTRAINT UK_H_PFR_FBG_STATUS_CODE UNIQUE (CODE),
    CONSTRAINT CHK_H_PFR_FBG_STATUS_ACTIVE CHECK (IS_ACTIVE IN (0, 1))
);

COMMENT ON TABLE H_PFR_FBG_STATUS IS 'Lookup table for FiberGrid fault statuses';
COMMENT ON COLUMN H_PFR_FBG_STATUS.CODE IS 'Status code as used in API (English)';
COMMENT ON COLUMN H_PFR_FBG_STATUS.DESCRIPTION IS 'Status description (English)';
COMMENT ON COLUMN H_PFR_FBG_STATUS.DESCRIPTION_GR IS 'Status description (Greek)';

-- Insert status values
INSERT INTO H_PFR_FBG_STATUS (ID, CODE, DESCRIPTION, DESCRIPTION_GR, SORT_ORDER) VALUES 
    (1, 'Resolved', 'Fault has been resolved', 'Η βλάβη επιλύθηκε', 1);
INSERT INTO H_PFR_FBG_STATUS (ID, CODE, DESCRIPTION, DESCRIPTION_GR, SORT_ORDER) VALUES 
    (2, 'Pending Issues', 'Issues remain to be addressed', 'Εκκρεμή ζητήματα', 2);
INSERT INTO H_PFR_FBG_STATUS (ID, CODE, DESCRIPTION, DESCRIPTION_GR, SORT_ORDER) VALUES 
    (3, 'In process', 'Work in progress', 'Σε εξέλιξη', 3);
INSERT INTO H_PFR_FBG_STATUS (ID, CODE, DESCRIPTION, DESCRIPTION_GR, SORT_ORDER) VALUES 
    (4, 'Cancelled', 'Fault report cancelled', 'Ακυρώθηκε', 4);

-- ----------------------------------------------------------------------------
-- H_PFR_FBG_FLAG_RELATED: Flag indicating affected network
-- ----------------------------------------------------------------------------
CREATE TABLE H_PFR_FBG_FLAG_RELATED (
    ID              NUMBER(1) PRIMARY KEY,
    CODE            VARCHAR2(20) NOT NULL,
    DESCRIPTION     VARCHAR2(200),
    DESCRIPTION_GR  VARCHAR2(200),
    IS_ACTIVE       NUMBER(1) DEFAULT 1 NOT NULL,
    SORT_ORDER      NUMBER(3),
    CREATED         TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    
    CONSTRAINT UK_H_PFR_FBG_FLAG_CODE UNIQUE (CODE),
    CONSTRAINT CHK_H_PFR_FBG_FLAG_ACTIVE CHECK (IS_ACTIVE IN (0, 1))
);

COMMENT ON TABLE H_PFR_FBG_FLAG_RELATED IS 'Lookup table for flag_related values indicating affected network';
COMMENT ON COLUMN H_PFR_FBG_FLAG_RELATED.ID IS 'Flag value (1, 2, or 3) as used in API';
COMMENT ON COLUMN H_PFR_FBG_FLAG_RELATED.CODE IS 'Short code for the flag';

-- Insert flag values
INSERT INTO H_PFR_FBG_FLAG_RELATED (ID, CODE, DESCRIPTION, DESCRIPTION_GR, SORT_ORDER) VALUES 
    (1, 'HEDNO', 'Fault in HEDNO network', 'Βλάβη στο δίκτυο ΔΕΔΔΗΕ', 1);
INSERT INTO H_PFR_FBG_FLAG_RELATED (ID, CODE, DESCRIPTION, DESCRIPTION_GR, SORT_ORDER) VALUES 
    (2, 'FIBER', 'Fault in Fiber network', 'Βλάβη στο δίκτυο οπτικών ινών', 2);
INSERT INTO H_PFR_FBG_FLAG_RELATED (ID, CODE, DESCRIPTION, DESCRIPTION_GR, SORT_ORDER) VALUES 
    (3, 'BOTH', 'Fault in both networks', 'Βλάβη και στα δύο δίκτυα', 3);

-- ----------------------------------------------------------------------------
-- H_PFR_FBG_API_TOKENS: API authentication tokens
-- ----------------------------------------------------------------------------
CREATE TABLE H_PFR_FBG_API_TOKENS (
    ID              NUMBER(10) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TOKEN           VARCHAR2(500) NOT NULL,
    CLIENT_NAME     VARCHAR2(100) NOT NULL,
    DESCRIPTION     VARCHAR2(500),
    IS_ACTIVE       NUMBER(1) DEFAULT 1 NOT NULL,
    VALID_FROM      TIMESTAMP,
    VALID_TO        TIMESTAMP,
    CREATED         TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CREATED_BY      VARCHAR2(100),
    
    CONSTRAINT UK_H_PFR_FBG_TOKENS_TOKEN UNIQUE (TOKEN),
    CONSTRAINT CHK_H_PFR_FBG_TOKENS_ACTIVE CHECK (IS_ACTIVE IN (0, 1))
);

COMMENT ON TABLE H_PFR_FBG_API_TOKENS IS 'API tokens for FiberGrid system authentication';
COMMENT ON COLUMN H_PFR_FBG_API_TOKENS.TOKEN IS 'Bearer token value';
COMMENT ON COLUMN H_PFR_FBG_API_TOKENS.CLIENT_NAME IS 'Name of the authenticated client';
COMMENT ON COLUMN H_PFR_FBG_API_TOKENS.IS_ACTIVE IS '1=active, 0=revoked';
COMMENT ON COLUMN H_PFR_FBG_API_TOKENS.VALID_FROM IS 'Token valid from this date (null=no restriction)';
COMMENT ON COLUMN H_PFR_FBG_API_TOKENS.VALID_TO IS 'Token valid until this date (null=no expiry)';

CREATE INDEX IDX_H_PFR_FBG_TOKENS_LOOKUP ON H_PFR_FBG_API_TOKENS(TOKEN, IS_ACTIVE);

-- Insert test token (remove or replace in production)
INSERT INTO H_PFR_FBG_API_TOKENS (TOKEN, CLIENT_NAME, DESCRIPTION, IS_ACTIVE, CREATED_BY) VALUES 
    ('fibergrid-test-token-2025', 'FiberGrid', 'Test token for FiberGrid integration', 1, 'SYSTEM');


-- ============================================================================
-- DATA TABLES (PFR_FBG_*)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- PFR_FBG_FAULTS: Main fault records from FiberGrid
-- ----------------------------------------------------------------------------
CREATE TABLE PFR_FBG_FAULTS (
    ID                  NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    FIBERGRID_ID        VARCHAR2(100) NOT NULL,
    PFR_FAILURE_ID      NUMBER(19),
    ADDRESS             VARCHAR2(500) NOT NULL,
    LATITUDE            NUMBER(10,7),
    LONGITUDE           NUMBER(10,7),
    FLAG_RELATED        NUMBER(1) NOT NULL,
    STATUS_ID           NUMBER(2) NOT NULL,
    NOTES               CLOB,
    DATE_CREATED        TIMESTAMP NOT NULL,
    DATE_RESOLVED       TIMESTAMP,
    CONTACT_NAME        VARCHAR2(200),
    CONTACT_PHONE       VARCHAR2(50),
    CONTACT_EMAIL       VARCHAR2(200),
    CREATED             TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    LAST_UPDATED        TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CREATED_BY          VARCHAR2(100),
    UPDATED_BY          VARCHAR2(100),
    IS_DELETED          NUMBER(1) DEFAULT 0 NOT NULL,
    
    CONSTRAINT FK_PFR_FBG_FAULTS_STATUS 
        FOREIGN KEY (STATUS_ID) REFERENCES H_PFR_FBG_STATUS(ID),
    CONSTRAINT FK_PFR_FBG_FAULTS_FLAG 
        FOREIGN KEY (FLAG_RELATED) REFERENCES H_PFR_FBG_FLAG_RELATED(ID),
    CONSTRAINT CHK_PFR_FBG_FAULTS_DELETED CHECK (IS_DELETED IN (0, 1))
);

COMMENT ON TABLE PFR_FBG_FAULTS IS 'Stores fault records received from FiberGrid system';
COMMENT ON COLUMN PFR_FBG_FAULTS.FIBERGRID_ID IS 'Unique identifier assigned by FiberGrid';
COMMENT ON COLUMN PFR_FBG_FAULTS.PFR_FAILURE_ID IS 'Reference to PFR_FAILURES.ID when linked (Phase 2)';
COMMENT ON COLUMN PFR_FBG_FAULTS.FLAG_RELATED IS 'FK to H_PFR_FBG_FLAG_RELATED: 1=HEDNO, 2=Fiber, 3=Both';
COMMENT ON COLUMN PFR_FBG_FAULTS.STATUS_ID IS 'FK to H_PFR_FBG_STATUS';
COMMENT ON COLUMN PFR_FBG_FAULTS.DATE_CREATED IS 'Date fault was created in FiberGrid (from API)';
COMMENT ON COLUMN PFR_FBG_FAULTS.IS_DELETED IS 'Soft delete flag: 0=active, 1=deleted';

-- Unique index on FIBERGRID_ID for non-deleted records
CREATE UNIQUE INDEX UK_PFR_FBG_FAULTS_FG_ID ON PFR_FBG_FAULTS(FIBERGRID_ID) 
    WHERE IS_DELETED = 0;

-- Index for PFR_FAILURE_ID lookup (Phase 2 integration)
CREATE INDEX IDX_PFR_FBG_FAULTS_PFR_ID ON PFR_FBG_FAULTS(PFR_FAILURE_ID);

-- Index for status filtering
CREATE INDEX IDX_PFR_FBG_FAULTS_STATUS ON PFR_FBG_FAULTS(STATUS_ID, IS_DELETED);

-- Index for date range queries
CREATE INDEX IDX_PFR_FBG_FAULTS_DATE ON PFR_FBG_FAULTS(DATE_CREATED, IS_DELETED);

-- Index for flag filtering
CREATE INDEX IDX_PFR_FBG_FAULTS_FLAG ON PFR_FBG_FAULTS(FLAG_RELATED, IS_DELETED);

-- Trigger to update LAST_UPDATED
CREATE OR REPLACE TRIGGER TRG_PFR_FBG_FAULTS_UPD
    BEFORE UPDATE ON PFR_FBG_FAULTS
    FOR EACH ROW
BEGIN
    :NEW.LAST_UPDATED := SYSTIMESTAMP;
END;
/

-- ----------------------------------------------------------------------------
-- PFR_FBG_FAULT_PHOTOS: Photo attachments for faults
-- ----------------------------------------------------------------------------
CREATE TABLE PFR_FBG_FAULT_PHOTOS (
    ID              NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    FAULT_ID        NUMBER(19) NOT NULL,
    PHOTO_DATA      BLOB NOT NULL,
    PHOTO_SIZE      NUMBER(19),
    CONTENT_TYPE    VARCHAR2(100) DEFAULT 'image/jpeg',
    FILE_NAME       VARCHAR2(255),
    CREATED         TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    
    CONSTRAINT FK_PFR_FBG_PHOTOS_FAULT 
        FOREIGN KEY (FAULT_ID) REFERENCES PFR_FBG_FAULTS(ID)
);

COMMENT ON TABLE PFR_FBG_FAULT_PHOTOS IS 'Stores photos attached to FiberGrid faults';
COMMENT ON COLUMN PFR_FBG_FAULT_PHOTOS.PHOTO_DATA IS 'Binary photo data (decoded from base64)';
COMMENT ON COLUMN PFR_FBG_FAULT_PHOTOS.PHOTO_SIZE IS 'Size of photo in bytes';

CREATE INDEX IDX_PFR_FBG_PHOTOS_FAULT ON PFR_FBG_FAULT_PHOTOS(FAULT_ID);

-- ----------------------------------------------------------------------------
-- PFR_FBG_API_LOGS: API request/response audit logging
-- ----------------------------------------------------------------------------
CREATE TABLE PFR_FBG_API_LOGS (
    ID              NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    DIRECTION       VARCHAR2(20) NOT NULL,
    ENDPOINT        VARCHAR2(200) NOT NULL,
    METHOD          VARCHAR2(10) NOT NULL,
    REQUEST_ID      VARCHAR2(50),
    CLIENT_NAME     VARCHAR2(100),
    CLIENT_VERSION  VARCHAR2(20),
    REQUEST_BODY    CLOB,
    RESPONSE_BODY   CLOB,
    HTTP_STATUS     NUMBER(3),
    DURATION_MS     NUMBER(10),
    ERROR_MESSAGE   VARCHAR2(4000),
    CREATED         TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    
    CONSTRAINT CHK_PFR_FBG_LOGS_DIR CHECK (DIRECTION IN ('INBOUND', 'OUTBOUND'))
);

COMMENT ON TABLE PFR_FBG_API_LOGS IS 'Audit log for all FiberGrid API requests and responses';
COMMENT ON COLUMN PFR_FBG_API_LOGS.DIRECTION IS 'INBOUND=from FiberGrid to PFR, OUTBOUND=from PFR to FiberGrid';
COMMENT ON COLUMN PFR_FBG_API_LOGS.REQUEST_ID IS 'UUID from x-request-id header';
COMMENT ON COLUMN PFR_FBG_API_LOGS.DURATION_MS IS 'Request processing time in milliseconds';

CREATE INDEX IDX_PFR_FBG_LOGS_CREATED ON PFR_FBG_API_LOGS(CREATED);
CREATE INDEX IDX_PFR_FBG_LOGS_REQ_ID ON PFR_FBG_API_LOGS(REQUEST_ID);
CREATE INDEX IDX_PFR_FBG_LOGS_DIR ON PFR_FBG_API_LOGS(DIRECTION, CREATED);


-- ============================================================================
-- VIEWS (Optional - for easier querying)
-- ============================================================================

-- View that joins faults with lookup tables for readable output
CREATE OR REPLACE VIEW V_PFR_FBG_FAULTS AS
SELECT 
    f.ID,
    f.FIBERGRID_ID,
    f.PFR_FAILURE_ID,
    f.ADDRESS,
    f.LATITUDE,
    f.LONGITUDE,
    f.FLAG_RELATED,
    fl.CODE AS FLAG_CODE,
    fl.DESCRIPTION AS FLAG_DESCRIPTION,
    fl.DESCRIPTION_GR AS FLAG_DESCRIPTION_GR,
    f.STATUS_ID,
    s.CODE AS STATUS_CODE,
    s.DESCRIPTION AS STATUS_DESCRIPTION,
    s.DESCRIPTION_GR AS STATUS_DESCRIPTION_GR,
    f.NOTES,
    f.DATE_CREATED,
    f.DATE_RESOLVED,
    f.CONTACT_NAME,
    f.CONTACT_PHONE,
    f.CONTACT_EMAIL,
    f.CREATED,
    f.LAST_UPDATED,
    f.CREATED_BY,
    f.IS_DELETED
FROM PFR_FBG_FAULTS f
JOIN H_PFR_FBG_STATUS s ON f.STATUS_ID = s.ID
JOIN H_PFR_FBG_FLAG_RELATED fl ON f.FLAG_RELATED = fl.ID
WHERE f.IS_DELETED = 0;

COMMENT ON TABLE V_PFR_FBG_FAULTS IS 'View of active faults with status and flag descriptions';


-- ============================================================================
-- SUMMARY
-- ============================================================================
/*
Tables created:
  - H_PFR_FBG_STATUS        : Lookup - Fault statuses
  - H_PFR_FBG_FLAG_RELATED  : Lookup - Network flag values
  - H_PFR_FBG_API_TOKENS    : Lookup - API authentication tokens
  - PFR_FBG_FAULTS          : Data   - Main fault records
  - PFR_FBG_FAULT_PHOTOS    : Data   - Photo attachments
  - PFR_FBG_API_LOGS        : Data   - API audit logs

Views created:
  - V_PFR_FBG_FAULTS        : Faults with lookup descriptions

Notes:
  - PFR_FAILURE_ID column is prepared for Phase 2 integration
  - All tables use IDENTITY columns (no sequences needed)
  - Test token inserted - replace/remove for production
*/

COMMIT;
