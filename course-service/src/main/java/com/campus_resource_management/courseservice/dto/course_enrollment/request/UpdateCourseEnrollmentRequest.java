package com.campus_resource_management.courseservice.dto.course_enrollment.request;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseEnrollmentRequest {

    @NotBlank(message = MessageResponse.COURSE_OFFERING_CODE_IS_REQUIRED)
    private String offeringCode;

    @NotBlank(message = MessageResponse.STUDENT_IDENTITY_ID_IS_REQUIRED)
    private String studentIdentityId;

    private Boolean isWithdrawn;

    @DecimalMin(value = "0.0", message = MessageResponse.FINAL_GRADE_MIN_VALUE)
    @DecimalMax(value = "100.0", message = MessageResponse.FINAL_GRADE_MAX_VALUE)
    private Double finalGrade;

    @Pattern(
            regexp = "A_PLUS|A|A_MINUS|B_PLUS|B|B_MINUS|C_PLUS|C|C_MINUS|D|F|PASS|FAIL|INCOMPLETE",
            message = MessageResponse.LETTER_GRADE_INVALID
    )
    private String letterGrade;

}
