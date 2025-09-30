package com.campus_resource_management.studentservice.repository;

import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentProfileRepositoryForFilter {
    Page<StudentProfile> filterStudentProfile(FilterStudentProfileRequest filterStudentProfileRequest, Pageable pageable);
}
