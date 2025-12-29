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
    public static final String
            ACADEMIC_TERM_INVALID = "Academic Term Invalid";
    public static final String
            ACADEMIC_YEAR_MIN_VALUE = "Academic Year Must be Larger Than [2000]";
    public static final String
            ACADEMIC_YEAR_MAX_VALUE = "Academic Year Must be Smaller Than [2100]";
    public static final String
            MAX_STUDENTS_MIN_VALUE = "Max Student Must be Positive Number";
    public static final String
            MAX_STUDENTS_MAX_VALUE =  "Max Student Must be Smaller Than [1000]";
    public static final String
            CREDIT_FROM_GREATER_THAN_TO = "Credit From Greater Than To";

    // REQUIRED
    public static final String
            COURSE_CODE_IS_REQUIRED = "Course Code is required";
    public static final String
            COURSE_NAME_IS_REQUIRED = "Course Name is required";
    public static final String
            CREDIT_IS_REQUIRED =  "Credit is required";
    public static final String
            ACADEMIC_TERM_IS_REQUIRED = "Academic Term is required";
    public static final String
            ACADEMIC_YEAR_IS_REQUIRED = "Academic Year is required";
    public static final String
            MAX_STUDENTS_IS_REQUIRED = "Max Students is required";
    public static final String
            COURSE_OFFERING_ID_IS_REQUIRED = "Course Offer Id is required";

    // NOT FOUND
    public static final String
            NO_DATA = "No data found";

    // EXISTED
    public static final String
            COURSE_CODE_ALREADY_EXISTS = "Course Code Already Exists";

    // INSTRUCTOR-SERVICE
    public static final String
            INSTRUCTOR_IDENTITY_ID_IS_REQUIRED = "Instructor Identity Id is required";
    public static final String
            INSTRUCTOR_IDENTITY_ID_INVALID_LENGTH = "Instructor Identity Id Should be of Length [12]";
    public static final String
            INSTRUCTOR_NAME_MAX_LENGTH = "Instructor Name Max Length [100]";
    public static final String
            ACADEMIC_RANK_TYPE_INVALID = "Academic Rank Invalid";

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
