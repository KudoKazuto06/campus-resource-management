package com.campus_resource_management.instructorservice.dto.instructor_profile.response;

import com.campus_resource_management.instructorservice.constant.AcademicRank;
import com.campus_resource_management.instructorservice.constant.EmploymentStatus;
import com.campus_resource_management.instructorservice.constant.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class DetailedInstructorProfileResponse {

    private String identityId;

    private String firstName;
    private String lastName;
    private Gender gender;

    private String email;
    private String schoolEmail;

    private String phoneNumber;
    private String officeLocation;

    private String department;
    private AcademicRank academicRank;
    private EmploymentStatus employmentStatus;

    private LocalDate hireDate;
    private String salaryBand;
    private String officeHours;

    private String instructorNote;

    private Boolean isDeleted;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
