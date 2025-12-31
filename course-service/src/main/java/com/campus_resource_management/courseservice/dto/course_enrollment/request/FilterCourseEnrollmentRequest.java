package com.campus_resource_management.courseservice.dto.course_enrollment.request;

import com.campus_resource_management.courseservice.constant.MessageResponse;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterCourseEnrollmentRequest {

    // ================= Pagination =================
    @Min(value = 0, message = MessageResponse.PAGE_FORMAT_INVALID)
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = MessageResponse.SIZE_FORMAT_INVALID)
    @Builder.Default
    private Integer size = 10;

    // ================= Filters =================
    @Size(max = 50, message = MessageResponse.COURSE_OFFERING_CODE_MAX_LENGTH)
    private String offeringCode;

    @Size(min = 12, max = 12, message = MessageResponse.STUDENT_IDENTITY_ID_INVALID_LENGTH)
    private String studentIdentityId;

    @Size(max = 100, message = MessageResponse.STUDENT_NAME_MAX_LENGTH)
    private String studentName;

    @Pattern(
            regexp = "A_PLUS|A|A_MINUS|B_PLUS|B|B_MINUS|C_PLUS|C|C_MINUS|D|F|PASS|FAIL|INCOMPLETE",
            message = MessageResponse.LETTER_GRADE_INVALID
    )
    private String letterGrade;

    private Boolean isWithdrawn;

    private LocalDate enrolledFrom;
    private LocalDate enrolledTo;

    // ================= Sorting =================
    private String sortBy;
}
