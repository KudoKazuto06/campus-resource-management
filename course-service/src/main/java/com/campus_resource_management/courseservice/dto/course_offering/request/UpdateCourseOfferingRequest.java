package com.campus_resource_management.courseservice.dto.course_offering.request;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseOfferingRequest {

    @NotBlank(message = MessageResponse.COURSE_OFFERING_CODE_IS_REQUIRED)
    private String courseOfferingCode;

    @Size(min = 12, max = 12, message = MessageResponse.INSTRUCTOR_IDENTITY_ID_INVALID_LENGTH)
    private String instructorIdentityId;

    @Min(value = 1, message = MessageResponse.MAX_STUDENTS_MIN_VALUE)
    @Max(value = 1000, message = MessageResponse.MAX_STUDENTS_MAX_VALUE)
    private Integer maxStudents;

    @Size(max = 1000, message = MessageResponse.COURSE_DESCRIPTION_MAX_LENGTH)
    private String description;
}
