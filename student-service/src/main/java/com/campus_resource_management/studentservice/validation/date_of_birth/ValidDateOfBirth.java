package com.campus_resource_management.studentservice.validation.date_of_birth;

import com.campus_resource_management.studentservice.constant.MessageResponse;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateOfBirth {
    String message() default MessageResponse.DATE_OF_BIRTH_MUST_BE_WITHIN_TIME_LIMIT;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
