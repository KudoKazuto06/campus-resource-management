package com.campus_resource_management.instructorservice.dto.instructor_profile.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SummaryInstructorProfileResponse {

    private String identityId;
    private String firstName;
    private String lastName;
    private String department;
    private String email;
    private String schoolEmail;

}
