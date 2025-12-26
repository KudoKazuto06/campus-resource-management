package com.campus_resource_management.instructorservice.constant;

public class StatusCode{

    // ===== Success codes =====
    public static final int SUCCESS                 = 300200; // OK
    public static final int CREATED                 = 300201; // Created
    public static final int NO_DATA                 = 300204;

    // ===== Client error codes =====
    public static final int ERROR                   = 300400; // Bad request (generic)
    public static final int NOT_FOUND               = 300404; // Resource not found
    public static final int EXISTED                 = 300409; // Resource already exists
    public static final int UNAUTHORIZED            = 300401; // Unauthorized access
    public static final int FORBIDDEN               = 300403; // Forbidden
    public static final int VALIDATION_ERR          = 300422; // Validation failed

    // ===== Server error codes =====
    public static final int SERVER_ERROR            = 300500; // Internal server error
    public static final int SERVICE_UNAVAILABLE     = 300503; // Service unavailable
    public static final int TIMEOUT                 = 300504; // Gateway timeout

    private StatusCode() {
        // Prevent instantiation
    }

}