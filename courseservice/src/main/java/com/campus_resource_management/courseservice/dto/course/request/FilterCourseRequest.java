package com.campus_resource_management.courseservice.dto.course.request;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterCourseRequest {

    @Min(value = 0, message = MessageResponse.PAGE_FORMAT_INVALID)
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = MessageResponse.SIZE_FORMAT_INVALID)
    @Builder.Default
    private Integer size = 10;

    private String courseCode;

    private String courseName;

    @Min(value = 0, message = MessageResponse.CREDIT_MIN_VALUE)
    @Max(value = 6, message = MessageResponse.CREDIT_MAX_VALUE)
    private Double creditFrom;

    @Min(value = 0, message = MessageResponse.CREDIT_MIN_VALUE)
    @Max(value = 6, message = MessageResponse.CREDIT_MAX_VALUE)
    private Double creditTo;

    @Pattern(
            regexp = "COMPUTER_SCIENCE|SOFTWARE_ENGINEERING|MATHEMATICS|PHYSICS|CHEMISTRY|BIOLOGY|ECONOMICS|BUSINESS|PSYCHOLOGY|EDUCATION|HISTORY|LITERATURE|PHILOSOPHY|ENGINEERING|MEDICINE|NURSING|ART|MUSIC|LAW|SOCIOLOGY",
            message = MessageResponse.DEPARTMENT_TYPE_INVALID
    )
    private String department;

    @Size(max = 1000, message = MessageResponse.COURSE_DESCRIPTION_MAX_LENGTH)
    private String description;

    private String sortBy;
}
