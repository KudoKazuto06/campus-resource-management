package com.campus_resource_management.instructorservice.service;

import com.campus_resource_management.instructorservice.dto.PaginationResponse;
import com.campus_resource_management.instructorservice.dto.ServiceResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.AddInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.FilterInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.UpdateInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.DetailedInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.FilterInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.SummaryInstructorProfileResponse;
import jakarta.validation.Valid;

public interface InstructorProfileService {

    ServiceResponse<SummaryInstructorProfileResponse> addInstructorProfile(
            @Valid AddInstructorProfileRequest request
    );

    ServiceResponse<SummaryInstructorProfileResponse> updateInstructorProfile(
            @Valid UpdateInstructorProfileRequest request
    );

    ServiceResponse<PaginationResponse<FilterInstructorProfileResponse>> viewFilteredInstructorProfile(
            @Valid FilterInstructorProfileRequest request
    );

    ServiceResponse<DetailedInstructorProfileResponse> viewDetailedInstructorProfileByIdentityId(
            String identityId
    );

    ServiceResponse<Void> deleteInstructorProfileByIdentityId(
            String identityId
    );

    ServiceResponse<Void> restoreInstructorProfile(
            String identityId
    );
}
