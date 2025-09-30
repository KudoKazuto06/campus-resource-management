package com.campus_resource_management.studentservice.dto.student_profile.response;

import com.campus_resource_management.studentservice.constant.DegreeType;
import com.campus_resource_management.studentservice.constant.Gender;
import com.campus_resource_management.studentservice.constant.StudentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FilterStudentProfileResponse {

    private String identityId;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String email;
    private String schoolEmail;
    private DegreeType degreeType;
    private String major;
    private Double gpa;
    private Integer yearOfStudy;
    private StudentStatus studentStatus;
}
