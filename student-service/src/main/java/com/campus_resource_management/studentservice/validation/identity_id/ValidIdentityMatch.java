package com.campus_resource_management.studentservice.validation.identity_id;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IdentityMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIdentityMatch {
    String message() default "Identity Id does not match Gender or Date of Birth";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
