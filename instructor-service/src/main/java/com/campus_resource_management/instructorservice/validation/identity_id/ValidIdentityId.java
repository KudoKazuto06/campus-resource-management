package com.campus_resource_management.instructorservice.validation.identity_id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdentityIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIdentityId {
    String message() default "Invalid Identity ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}