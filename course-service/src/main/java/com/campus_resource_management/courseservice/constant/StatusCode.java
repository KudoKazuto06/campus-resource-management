package com.campus_resource_management.courseservice.constant;

public class StatusCode {

    // ===== Success codes =====
    public static final int SUCCESS                 = 500200; // OK
    public static final int CREATED                 = 500201; // Created
    public static final int NO_DATA                 = 500204;

    // ===== Client error codes =====
    public static final int ERROR                   = 500400; // Bad request (generic)
    public static final int NOT_FOUND               = 500404; // Resource not found
    public static final int EXISTED                 = 500409; // Resource already exists
    public static final int UNAUTHORIZED            = 500401; // Unauthorized access
    public static final int FORBIDDEN               = 500403; // Forbidden
    public static final int VALIDATION_ERR          = 500422; // Validation failed

    // ===== Server error codes =====
    public static final int SERVER_ERROR            = 500500; // Internal server error
    public static final int SERVICE_UNAVAILABLE     = 500503; // Service unavailable
    public static final int TIMEOUT                 = 500504; // Gateway timeout

    private StatusCode() {
        // Prevent instantiation
    }
}