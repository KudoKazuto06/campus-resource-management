package com.campus_resource_management.studentservice.constant;

public class StatusCode {

    // ===== Success codes =====
    public static final int SUCCESS        = 400200; // OK
    public static final int CREATED        = 400201; // Created

    // ===== Client error codes =====
    public static final int ERROR          = 400400; // Bad request (generic)
    public static final int NOT_FOUND      = 400404; // Resource not found
    public static final int EXISTED        = 400409; // Resource already exists
    public static final int UNAUTHORIZED   = 400401; // Unauthorized access
    public static final int FORBIDDEN      = 400403; // Forbidden
    public static final int VALIDATION_ERR = 400422; // Validation failed

    // ===== Server error codes =====
    public static final int SERVER_ERROR   = 400500; // Internal server error
    public static final int SERVICE_UNAVAILABLE = 400503; // Service unavailable
    public static final int TIMEOUT        = 400504; // Gateway timeout

    private StatusCode() {
        // Prevent instantiation
    }
}