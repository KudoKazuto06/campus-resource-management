package com.campus_resource_management.instructorservice.dto.instructor_profile.response;

import com.campus_resource_management.instructorservice.constant.AcademicRank;
import com.campus_resource_management.instructorservice.constant.EmploymentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilterInstructorProfileResponse {

    private String identityId;
    private String fullName;
    private String department;
    private AcademicRank academicRank;
    private EmploymentStatus employmentStatus;

}
