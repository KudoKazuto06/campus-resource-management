package com.campus_resource_management.studentservice.exception;

public class StudentProfileNotFoundException extends RuntimeException {
    public StudentProfileNotFoundException(String identityId) {
        super("Student profile not found with id: " + identityId);
    }
}
