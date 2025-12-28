package com.campus_resource_management.courseservice.dto.course.request;

import com.campus_resource_management.courseservice.constant.Department;
import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseRequest {

    @NotNull(message = MessageResponse.DEPARTMENT_IS_REQUIRED)
    @Pattern(
            regexp = "COMPUTER_SCIENCE|SOFTWARE_ENGINEERING|MATHEMATICS|PHYSICS|CHEMISTRY|BIOLOGY|ECONOMICS|BUSINESS|PSYCHOLOGY|EDUCATION|HISTORY|LITERATURE|PHILOSOPHY|ENGINEERING|MEDICINE|NURSING|ART|MUSIC|LAW|SOCIOLOGY",
            message = MessageResponse.DEPARTMENT_TYPE_INVALID
    )
    private String department;

    @NotBlank(message = MessageResponse.COURSE_CODE_IS_REQUIRED)
    @Size(max = 20, message = MessageResponse.COURSE_CODE_MAX_LENGTH)
    @Min(value = 100, message = MessageResponse.COURSE_CODE_MIN_VALUE)
    @Max(value = 999, message = MessageResponse.COURSE_CODE_MAX_VALUE)
    private String courseCode;

    @NotBlank(message = MessageResponse.COURSE_NAME_IS_REQUIRED)
    @Size(max = 100, message = MessageResponse.COURSE_NAME_MAX_LENGTH)
    private String courseName;

    @NotNull(message = MessageResponse.CREDIT_IS_REQUIRED)
    @DecimalMin(value = "0.0", message = MessageResponse.CREDIT_MIN_VALUE)
    @DecimalMax(value = "6.0", message = MessageResponse.CREDIT_MAX_VALUE)
    private Double credit;

    @Size(max = 1000, message = MessageResponse.COURSE_DESCRIPTION_MAX_LENGTH)
    private String description;

}
