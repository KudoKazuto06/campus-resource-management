package com.campus_resource_management.instructorservice.repository;

import com.campus_resource_management.instructorservice.dto.instructor_profile.request.FilterInstructorProfileRequest;
import com.campus_resource_management.instructorservice.entity.InstructorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstructorProfileRepositoryForFilter {

    Page<InstructorProfile> filterInstructorProfile(
            FilterInstructorProfileRequest filterInstructorProfileRequest,
            Pageable pageable
    );
}
