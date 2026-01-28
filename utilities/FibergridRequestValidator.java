package gr.deddie.pfr.utilities;

import gr.deddie.pfr.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for FiberGrid API requests.
 * Validates required fields, enum values, and field formats.
 */
public class FibergridRequestValidator {

    /**
     * Result of validation containing errors categorized by type.
     */
    public static class ValidationResult {
        private final List<String> missingFields = new ArrayList<>();
        private final List<String> invalidEnums = new ArrayList<>();
        private final List<String> invalidFormats = new ArrayList<>();
        private String errorType = "VALIDATION_ERROR"; // Default error type

        public boolean isValid() {
            return missingFields.isEmpty() && invalidEnums.isEmpty() && invalidFormats.isEmpty();
        }

        public void addMissingField(String field) {
            missingFields.add(field + " is required");
        }

        public void addInvalidEnum(String message) {
            invalidEnums.add(message);
        }

        public void addInvalidFormat(String message) {
            invalidFormats.add(message);
        }

        public List<String> getAllErrors() {
            List<String> all = new ArrayList<>();
            all.addAll(missingFields);
            all.addAll(invalidEnums);
            all.addAll(invalidFormats);
            return all;
        }

        public boolean hasEnumErrors() {
            return !invalidEnums.isEmpty() && missingFields.isEmpty() && invalidFormats.isEmpty();
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public List<String> getMissingFields() {
            return missingFields;
        }

        public List<String> getInvalidEnums() {
            return invalidEnums;
        }

        public List<String> getInvalidFormats() {
            return invalidFormats;
        }
    }

    /**
     * Validate a fault creation request (POST /pfr/faults/create).
     * Required fields: fibergrid_id, address, coordinates, flag_related, status, date_created
     */
    public static ValidationResult validateCreateRequest(FibergridFaultCreateRequest request) {
        ValidationResult result = new ValidationResult();

        if (request == null) {
            result.addMissingField("request body");
            return result;
        }

        // Required fields
        if (isBlank(request.getFibergridId())) {
            result.addMissingField("fibergrid_id");
        } else if (request.getFibergridId().length() > 100) {
            result.addInvalidFormat("fibergrid_id must be 100 characters or less");
        }

        if (isBlank(request.getAddress())) {
            result.addMissingField("address");
        } else if (request.getAddress().length() > 500) {
            result.addInvalidFormat("address must be 500 characters or less");
        }

        if (request.getCoordinates() == null) {
            result.addMissingField("coordinates");
        } else {
            validateCoordinates(request.getCoordinates(), result);
        }

        if (request.getFlagRelated() == null) {
            result.addMissingField("flag_related");
        } else if (!FibergridFlagRelated.isValid(request.getFlagRelated())) {
            result.addInvalidEnum("flag_related must be " + FibergridFlagRelated.getValidValuesDescription());
        }

        if (isBlank(request.getStatus())) {
            result.addMissingField("status");
        } else if (!FibergridFaultStatus.isValid(request.getStatus())) {
            result.addInvalidEnum("status must be one of: " + FibergridFaultStatus.getValidValues());
        }

        if (request.getDateCreated() == null) {
            result.addMissingField("date_created");
        }

        // Optional field validation
        if (request.getNotes() != null && request.getNotes().length() > 4000) {
            result.addInvalidFormat("notes must be 4000 characters or less");
        }

        if (request.getContactInformation() != null) {
            validateContactInformation(request.getContactInformation(), result);
        }

        // Determine error type
        if (result.hasEnumErrors()) {
            result.setErrorType("INVALID_ENUM");
        }

        return result;
    }

    /**
     * Validate a fault update request (POST /pfr/faults/update).
     * Required field: fibergrid_id
     * Optional fields: status, notes, date_resolved, contact_information, photos
     */
    public static ValidationResult validateUpdateRequest(FibergridFaultUpdateRequest request) {
        ValidationResult result = new ValidationResult();

        if (request == null) {
            result.addMissingField("request body");
            return result;
        }

        // Required field
        if (isBlank(request.getFibergridId())) {
            result.addMissingField("fibergrid_id");
        }

        // Optional field validation
        if (request.getStatus() != null && !FibergridFaultStatus.isValid(request.getStatus())) {
            result.addInvalidEnum("status must be one of: " + FibergridFaultStatus.getValidValues());
            result.setErrorType("INVALID_STATUS");
        }

        if (request.getNotes() != null && request.getNotes().length() > 4000) {
            result.addInvalidFormat("notes must be 4000 characters or less");
        }

        if (request.getContactInformation() != null) {
            validateContactInformation(request.getContactInformation(), result);
        }

        return result;
    }

    /**
     * Validate request headers.
     * Required: Authorization
     * Optional: x-client-name, x-client-version, x-request-id
     */
    public static ValidationResult validateHeaders(String authorization, String clientName, 
                                                     String clientVersion, String requestId) {
        ValidationResult result = new ValidationResult();

        if (isBlank(authorization)) {
            result.addMissingField("Authorization header");
        } else if (!authorization.startsWith("Bearer ")) {
            result.addInvalidFormat("Authorization header must start with 'Bearer '");
        }

        // Note: x-client-name, x-client-version, x-request-id are optional per latest API spec
        // but we validate format if provided
        if (requestId != null && !requestId.isEmpty()) {
            // Basic UUID format check
            if (!requestId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
                result.addInvalidFormat("x-request-id must be a valid UUID");
            }
        }

        return result;
    }

    // ==================== Private Helper Methods ====================

    private static void validateCoordinates(FibergridCoordinates coords, ValidationResult result) {
        if (coords.getLatitude() == null) {
            result.addMissingField("coordinates.latitude");
        } else if (coords.getLatitude() < -90 || coords.getLatitude() > 90) {
            result.addInvalidFormat("coordinates.latitude must be between -90 and 90");
        }

        if (coords.getLongitude() == null) {
            result.addMissingField("coordinates.longitude");
        } else if (coords.getLongitude() < -180 || coords.getLongitude() > 180) {
            result.addInvalidFormat("coordinates.longitude must be between -180 and 180");
        }
    }

    private static void validateContactInformation(FibergridContactInformation contact, ValidationResult result) {
        if (contact.getName() != null && contact.getName().length() > 200) {
            result.addInvalidFormat("contact_information.name must be 200 characters or less");
        }

        if (contact.getPhone() != null && contact.getPhone().length() > 50) {
            result.addInvalidFormat("contact_information.phone must be 50 characters or less");
        }

        if (contact.getEmail() != null) {
            if (contact.getEmail().length() > 200) {
                result.addInvalidFormat("contact_information.email must be 200 characters or less");
            } else if (!contact.hasValidEmail()) {
                result.addInvalidFormat("contact_information.email must be a valid email address");
            }
        }
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
