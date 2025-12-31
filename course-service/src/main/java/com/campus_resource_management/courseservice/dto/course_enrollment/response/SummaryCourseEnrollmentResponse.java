package com.campus_resource_management.courseservice.dto.course_enrollment.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryCourseEnrollmentResponse {

    private String enrollmentId;
    private String offeringCode;
    private String studentIdentityId;
    private Boolean isWithdrawn;
    private LocalDate enrolledAt;
}
