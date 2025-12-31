package com.campus_resource_management.courseservice.exception;

public class InvalidAcademicTermException extends RuntimeException {
    public InvalidAcademicTermException(String value) {
        super("Invalid academic term: " + value);
    }
}
