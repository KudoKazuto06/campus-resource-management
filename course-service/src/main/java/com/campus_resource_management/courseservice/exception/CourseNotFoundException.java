package com.campus_resource_management.courseservice.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String courseCode) {
        super("Course not found with code: " + courseCode);
    }
}
