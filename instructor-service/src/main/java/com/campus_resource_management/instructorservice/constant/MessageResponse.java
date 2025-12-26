package com.campus_resource_management.instructorservice.constant;

public class MessageResponse{

    // SUCCESS
    public static final String
            ADD_INSTRUCTOR_PROFILE_SUCCESS =  "Add Instructor Profile Success";
    public static final String
            UPDATE_INSTRUCTOR_PROFILE_SUCCESS = "Update Instructor Profile Success";
    public static final String
            VIEW_ALL_INSTRUCTOR_PROFILE_SUCCESS = "View All Instructor Profile Success";
    public static final String
            VIEW_DETAIL_INSTRUCTOR_PROFILE_SUCCESS = "View Detail Instructor Profile Success";
    public static final String
            DELETE_INSTRUCTOR_PROFILE_SUCCESS = "Delete Instructor Profile Success";
    public static final String
            RESTORE_INSTRUCTOR_PROFILE_SUCCESS = "Restore Instructor Profile Success";

    // VALIDATION
    public static final String
            DATE_OF_BIRTH_MUST_BE_IN_THE_PAST = "Date of Birth must be in the past";
    public static final String
            DATE_OF_BIRTH_MUST_BE_WITHIN_TIME_LIMIT = "Date of Birth must be within the time limit [100]";
    public static final String
            FIRST_NAME_MAX_LENGTH = "First Name Reached Maximum Length [50]";
    public static final String
            LAST_NAME_MAX_LENGTH = "Last Name Reached Maximum Length [50]";
    public static final String
            OFFICE_NUMBER_MAX_LENGTH = "Office Number Reached Maximum Length [50]";
    public static final String
            INSTRUCTOR_NOTE_MAX_LENGTH = "Instructor Note Maximum Length [1000]";
    public static final String
            EMAIL_FORMAT_INVALID = "Invalid Email Format";
    public static final String
            GENDER_FORMAT_INVALID = "Invalid Gender Format";
    public static final String
            PAGE_FORMAT_INVALID = "Invalid Page Format";
    public static final String
            SIZE_FORMAT_INVALID =  "Invalid Size Format";
    public static final String
            DEPARTMENT_TYPE_INVALID = "Invalid Department Type Format";
    public static final String
            ACADEMIC_RANK_TYPE_INVALID =  "Invalid Academic Rank Type Format";
    public static final String
            EMPLOYMENT_STATUS_TYPE_INVALID =  "Invalid Employment Status Type Format";

    // REQUIRED
    public static final String
            INSTRUCTOR_PROFILE_ID_IS_REQUIRED = "Instructor Profile ID is Required";
    public static final String
            IDENTITY_ID_IS_REQUIRED = "Identity ID is required";
    public static final String
            FIRST_NAME_IS_REQUIRED = "First Name is required";
    public static final String
            LAST_NAME_IS_REQUIRED = "Last Name is required";
    public static final String
            EMAIL_IS_REQUIRED = "Email is required";
    public static final String
            GENDER_IS_REQUIRED = "Gender is required";
    public static final String
            DATE_OF_BIRTH_IS_REQUIRED = "Date of Birth is required";
    public static final String
            PHONE_NUMBER_IS_REQUIRED = "Phone Number is required";
    public static final String
            OFFICE_NUMBER_IS_REQUIRED = "Office Number is required";
    public static final String
            DEPARTMENT_IS_REQUIRED = "Department is required";
    public static final String
            ACADEMIC_RANK_IS_REQUIRED = "Academic Rank is required";
    public static final String
            EMPLOYMENT_STATUS_IS_REQUIRED  = "Employment Status is required";
    public static final String
            HIRE_DATE_IS_REQUIRED = "Hire Date is required";
    public static final String
            SALARY_BAND_IS_REQUIRED = "Salary band is required";


    // NOT FOUND
    public static final String
            INSTRUCTOR_PROFILE_NOT_FOUND = "Instructor Profile Not Found";
    public static final String
            NO_DATA = "No data found";

    // EXISTED
    public static final String
            EMAIL_ALREADY_EXISTS = "Email Already Exists";
    public static final String
            IDENTIFY_ID_ALREADY_EXISTS = "Identify ID Already Exists";


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