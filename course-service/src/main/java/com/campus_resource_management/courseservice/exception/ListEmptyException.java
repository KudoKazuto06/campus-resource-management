package com.campus_resource_management.courseservice.exception;

public class ListEmptyException extends RuntimeException {
    public ListEmptyException(String message) {
        super(message);
    }
}

