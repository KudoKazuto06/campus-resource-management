package com.campus_resource_management.courseservice.repository.course_offering;

import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.entity.CourseOffering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseOfferingRepositoryForFilter {

    Page<CourseOffering> filterCourseOffering(
            FilterCourseOfferingRequest filter,
            Pageable pageable
    );
}
