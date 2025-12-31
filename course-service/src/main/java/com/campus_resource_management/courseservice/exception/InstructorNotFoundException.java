package com.campus_resource_management.courseservice.exception;

public class InstructorNotFoundException extends RuntimeException {
    public InstructorNotFoundException(String instructorIdentityId) {
        super("Instructor not found with id: " + instructorIdentityId);
    }
}
