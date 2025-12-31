package com.campus_resource_management.courseservice.dto.course_enrollment.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailedCourseEnrollmentResponse {

    private String offeringCode;
    private String courseName;
    private String term;
    private Integer year;
    private String instructorName;

    private String studentIdentityId;
    private String studentName;
    private String studentEmail;
    private Integer yearOfStudy;
    private String degreeType;

    private Boolean isWithdrawn;
    private LocalDate enrolledAt;
    private Double finalGrade;
    private String letterGrade;


}
