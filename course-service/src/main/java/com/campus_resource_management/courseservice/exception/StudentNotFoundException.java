package com.campus_resource_management.courseservice.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String studentIdentityId) {
        super("Student not found with id: " + studentIdentityId);
    }
}
