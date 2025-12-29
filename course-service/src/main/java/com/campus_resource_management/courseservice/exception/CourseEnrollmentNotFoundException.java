package com.campus_resource_management.courseservice.exception;

public class CourseEnrollmentNotFoundException extends RuntimeException {
    public CourseEnrollmentNotFoundException(String message) {
        super(message);
    }
}
