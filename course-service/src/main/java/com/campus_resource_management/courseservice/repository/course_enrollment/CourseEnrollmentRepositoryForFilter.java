package com.campus_resource_management.courseservice.repository.course_enrollment;

import com.campus_resource_management.courseservice.dto.course_enrollment.request.FilterCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.entity.CourseEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseEnrollmentRepositoryForFilter {

    Page<CourseEnrollment> filterCourseEnrollment(
            FilterCourseEnrollmentRequest filter,
            Pageable pageable
    );
}
