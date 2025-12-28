package com.campus_resource_management.courseservice.dto.course.request;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseRequest {

    @NotBlank(message = MessageResponse.COURSE_CODE_IS_REQUIRED)
    @Pattern(
            regexp = "^[A-Z]{2,4}\\d{3}$",
            message = MessageResponse.COURSE_CODE_INVALID
    )
    private String courseCode; // exact match required for update

    @Pattern(
            regexp = "COMPUTER_SCIENCE|SOFTWARE_ENGINEERING|MATHEMATICS|PHYSICS|CHEMISTRY|BIOLOGY|ECONOMICS|BUSINESS|PSYCHOLOGY|EDUCATION|HISTORY|LITERATURE|PHILOSOPHY|ENGINEERING|MEDICINE|NURSING|ART|MUSIC|LAW|SOCIOLOGY",
            message = MessageResponse.DEPARTMENT_TYPE_INVALID
    )
    private String department;

    @Size(max = 100, message = MessageResponse.COURSE_NAME_MAX_LENGTH)
    private String courseName;

    @DecimalMin(value = "0.0", message = MessageResponse.CREDIT_MIN_VALUE)
    @DecimalMax(value = "6.0", message = MessageResponse.CREDIT_MAX_VALUE)
    private Double credit;

    @Size(max = 1000, message = MessageResponse.COURSE_DESCRIPTION_MAX_LENGTH)
    private String description;
}
