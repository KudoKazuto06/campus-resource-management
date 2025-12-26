package com.campus_resource_management.instructorservice.validation.date_of_birth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateOfBirthValidator
        implements ConstraintValidator<ValidDateOfBirth, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) return true;
        LocalDate hundredYearsAgo = LocalDate.now().minusYears(100);
        return !date.isBefore(hundredYearsAgo);
    }

}