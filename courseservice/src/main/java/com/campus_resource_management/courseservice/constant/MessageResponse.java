package com.campus_resource_management.courseservice.constant;

public class MessageResponse{

    // SUCCESS
    public static final String
            ADD_COURSE_SUCCESS = "Add Course Successfully";
    public static final String
            UPDATE_COURSE_SUCCESS = "Update Course Successfully";
    public static final String
            VIEW_ALL_COURSES_SUCCESS =  "View All Successfully";
    public static final String
            VIEW_DETAIL_COURSE_SUCCESS = "View Course Successfully";
    public static final String
            DELETE_COURSE_SUCCESS = "Delete Course Successfully";
    public static final String
            RESTORE_COURSE_SUCCESS = "Restore Course Successfully";

    // VALIDATION
    public static final String
            COURSE_CODE_MAX_LENGTH = "Course Code Reached Maximum Length [20]";
    public static final String
            COURSE_CODE_INVALID = "Course Code must follow format like CSC101";
    public static final String
            COURSE_CODE_MIN_VALUE = "Course Code Must not be Smaller Than 100";
    public static final String
            COURSE_CODE_MAX_VALUE = "Course Code Must not be Larger Than 999";
    public static final String
            COURSE_NAME_MAX_LENGTH = "Course Name Reached Maximum Length [100]";
    public static final String
            COURSE_DESCRIPTION_MAX_LENGTH = "Course Description Reached Maximum Length [1000]";
    public static final String
            CREDIT_MIN_VALUE = "Credit Must be Positive Number";
    public static final String
            CREDIT_MAX_VALUE = "Credit Must not be Larger Than 6";
    public static final String
            PAGE_FORMAT_INVALID = "Page Format Invalid";
    public static final String
            SIZE_FORMAT_INVALID = "Size Format Invalid";
    public static final String
            DEPARTMENT_IS_REQUIRED = "Department is required";
    public static final String
            DEPARTMENT_TYPE_INVALID = "Department Type Invalid";

    // REQUIRED
    public static final String
            COURSE_CODE_IS_REQUIRED = "Course Code is required";
    public static final String
            COURSE_NAME_IS_REQUIRED = "Course Name is required";
    public static final String
            CREDIT_IS_REQUIRED =  "Credit is required";



    // NOT FOUND
    public static final String
            NO_DATA = "No data found";

    // EXISTED
    public static final String
            COURSE_CODE_ALREADY_EXISTS = "Course Code Already Exists";


    public static String format(String message){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 3) {
            StackTraceElement stackTraceElement = stackTraceElements[3];
            String functionName = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
            return "[" + functionName + "] " + message;
        }
        return "[UnknownFunction] " + message;
    }

}
