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
    public static final String
            ADD_COURSE_OFFERING_SUCCESS = "Add Course Offering Successfully";
    public static final String
            UPDATE_COURSE_OFFERING_SUCCESS = "Update Course Offering Successfully";
    public static final String
            VIEW_ALL_COURSE_OFFERINGS_SUCCESS =  "View All Offerings Successfully";
    public static final String
            VIEW_DETAIL_COURSE_OFFERING_SUCCESS = "View Course Offering Successfully";
    public static final String
            DELETE_COURSE_OFFERING_SUCCESS = "Delete Course Offering Successfully";
    public static final String
            RESTORE_COURSE_OFFERING_SUCCESS = "Restore Course Offering Successfully";
    public static final String
            ADD_COURSE_ENROLLMENT_SUCCESS = "Add Course Enrollment Successfully";
    public static final String
            UPDATE_COURSE_ENROLLMENT_SUCCESS = "Update Course Enrollment Successfully";
    public static final String
            VIEW_ALL_COURSE_ENROLLMENTS_SUCCESS =  "View All Enrollments Successfully";
    public static final String
            VIEW_DETAIL_COURSE_ENROLLMENT_SUCCESS = "View Course Enrollment Successfully";
    public static final String
            DELETE_COURSE_ENROLLMENT_SUCCESS = "Delete Course Enrollment Successfully";
    public static final String
            RESTORE_COURSE_ENROLLMENT_SUCCESS = "Restore Course Enrollment Successfully";
    public static final String
            WITHDRAW_COURSE_ENROLLMENT_SUCCESS = "Withdraw Course Enrollment Successfully";

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
    public static final String
            COURSE_OFFERING_CODE_MAX_LENGTH = "Course Offering Code Maximum Length [100]";
    public static final String
            ACADEMIC_RANK_TYPE_INVALID = "Academic Rank Invalid";
    public static final String
            SECTION_MAX_LENGTH = "Section Max Length [20]";

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
            COURSE_OFFERING_CODE_IS_REQUIRED = "Course Offer Code is required";
    public static final String
            SECTION_IS_REQUIRED = "Section is required";
    public static final String
            STUDENT_IDENTITY_ID_IS_REQUIRED = "Student Identity Id is required";
    public static final String
            ENROLLMENT_ID_IS_REQUIRED = "Enrollment Id is required";
    public static final String
            FINAL_GRADE_MIN_VALUE =  "Final Grade Must Not Be Below [0]";
    public static final String
            FINAL_GRADE_MAX_VALUE =  "Final Grade Must Not Exceed [100]";
    public static final String
            LETTER_GRADE_INVALID = "Letter Grade Invalid";

    // NOT FOUND
    public static final String
            NO_DATA = "No data found";
    public static final String
            COURSE_OFFERING_NOT_FOUND = "Course Offer Not Found";

    // EXISTED
    public static final String
            COURSE_CODE_ALREADY_EXISTS = "Course Code Already Exists";
    public static final String
            COURSE_OFFERING_ALREADY_EXISTS = "Course Offering Already Exists";
    public static final String
            COURSE_ENROLLMENT_ALREADY_EXISTS = "Course Enrollment Already Exists";

    // INSTRUCTOR-SERVICE
    public static final String
            INSTRUCTOR_IDENTITY_ID_IS_REQUIRED = "Instructor Identity Id is required";
    public static final String
            INSTRUCTOR_IDENTITY_ID_INVALID_LENGTH = "Instructor Identity Id Should be of Length [12]";
    public static final String
            INSTRUCTOR_NAME_MAX_LENGTH = "Instructor Name Max Length [100]";

    // STUDENT-SERVICE
    public static final String
            STUDENT_IDENTITY_ID_INVALID_LENGTH =  "Student Identity Id Should be of Length [12]";
    public static final String
            STUDENT_NAME_MAX_LENGTH = "Student Name Max Length [100]";

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
