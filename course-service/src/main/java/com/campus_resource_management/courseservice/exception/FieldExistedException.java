package com.campus_resource_management.courseservice.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class FieldExistedException extends DataIntegrityViolationException {
    public FieldExistedException(String message) {
        super(message);
    }
}
