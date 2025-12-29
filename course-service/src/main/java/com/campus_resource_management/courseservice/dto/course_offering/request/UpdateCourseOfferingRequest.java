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

    @NotNull(message = MessageResponse.COURSE_OFFERING_ID_IS_REQUIRED)
    private UUID offeringId;

    @Pattern(
            regexp = "FALL|WINTER|SPRING|SUMMER",
            message = MessageResponse.ACADEMIC_TERM_INVALID
    )
    private String term;

    @Min(value = 2000, message = MessageResponse.ACADEMIC_YEAR_MIN_VALUE)
    @Max(value = 2100, message = MessageResponse.ACADEMIC_YEAR_MAX_VALUE)
    private Integer year;

    @Size(min = 12, max = 12, message = MessageResponse.INSTRUCTOR_IDENTITY_ID_INVALID_LENGTH)
    private String instructorIdentityId;

    @Min(value = 1, message = MessageResponse.MAX_STUDENTS_MIN_VALUE)
    @Max(value = 1000, message = MessageResponse.MAX_STUDENTS_MAX_VALUE)
    private Integer maxStudents;
}
