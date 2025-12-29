package com.campus_resource_management.courseservice.dto.course_offering.request;

import com.campus_resource_management.courseservice.constant.AcademicTerm;
import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseOfferingRequest {

    @NotBlank(message = MessageResponse.COURSE_CODE_IS_REQUIRED)
    @Size(max = 20, message = MessageResponse.COURSE_CODE_MAX_LENGTH)
    private String courseCode;

    @NotNull(message = MessageResponse.ACADEMIC_TERM_IS_REQUIRED)
    @Pattern(
            regexp = "FALL|WINTER|SPRING|SUMMER",
            message = MessageResponse.ACADEMIC_TERM_INVALID
    )
    private String term;

    @NotNull(message = MessageResponse.ACADEMIC_YEAR_IS_REQUIRED)
    @Min(value = 2000, message = MessageResponse.ACADEMIC_YEAR_MIN_VALUE)
    @Max(value = 2100, message = MessageResponse.ACADEMIC_YEAR_MAX_VALUE)
    private Integer year;

    @NotBlank(message = MessageResponse.INSTRUCTOR_IDENTITY_ID_IS_REQUIRED)
    @Size(min = 12, max = 12, message = MessageResponse.INSTRUCTOR_IDENTITY_ID_INVALID_LENGTH)
    private String instructorIdentityId;

    @NotNull(message = MessageResponse.MAX_STUDENTS_IS_REQUIRED)
    @Min(value = 1, message = MessageResponse.MAX_STUDENTS_MIN_VALUE)
    @Max(value = 1000, message = MessageResponse.MAX_STUDENTS_MAX_VALUE)
    private Integer maxStudents;
}
