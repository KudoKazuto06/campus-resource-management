package com.campus_resource_management.instructorservice.exception;

public class InstructorProfileNotFoundException extends RuntimeException {
    public InstructorProfileNotFoundException(String identityId) {
        super("Instructor profile not found with id: " + identityId);
    }
}
