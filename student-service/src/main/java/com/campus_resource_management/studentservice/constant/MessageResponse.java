package com.campus_resource_management.studentservice.constant;

public class MessageResponse {

    // SUCCESS
    public static final String
            ADD_STUDENT_PROFILE_SUCCESS = "Add Student Profile Successfully";
    public static final String
            UPDATE_STUDENT_PROFILE_SUCCESS = "Update Student Profile Successfully";

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
            STUDENT_NOTE_MAX_LENGTH = "Student Note Maximum Length [1000]";
    public static final String
            EMAIL_FORMAT_INVALID = "Invalid Email Format";
    public static final String
            GENDER_FORMAT_INVALID = "Invalid Gender Format";


    // REQUIRED
    public static final String
            STUDENT_PROFILE_ID_IS_REQUIRED = "Student Profile ID is Required";
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
            DEGREE_TYPE_IS_REQUIRED = "Degree Type is required";
    public static final String
            MAJOR_TYPE_IS_REQUIRED = "Major Type is required";
    public static final String
            MINOR_TYPE_IS_REQUIRED = "Minor Type is required";


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
