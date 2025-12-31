package com.campus_resource_management.courseservice.dto.course_enrollment.request;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseEnrollmentRequest {

    @NotBlank(message = MessageResponse.COURSE_OFFERING_CODE_IS_REQUIRED)
    @Size(max = 100, message = MessageResponse.COURSE_OFFERING_CODE_MAX_LENGTH)
    private String offeringCode;

    @NotBlank(message = MessageResponse.STUDENT_IDENTITY_ID_IS_REQUIRED)
    @Size(min = 12, max = 12, message = MessageResponse.STUDENT_IDENTITY_ID_INVALID_LENGTH)
    private String studentIdentityId;

}
