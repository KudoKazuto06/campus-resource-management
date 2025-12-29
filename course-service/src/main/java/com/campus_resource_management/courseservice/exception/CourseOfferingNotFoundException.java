package com.campus_resource_management.courseservice.exception;

public class CourseOfferingNotFoundException extends RuntimeException {
    public CourseOfferingNotFoundException(String message) {
        super(message);
    }
}
