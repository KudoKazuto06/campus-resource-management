package com.campus_resource_management.courseservice.repository;

import com.campus_resource_management.courseservice.dto.course.request.FilterCourseRequest;
import com.campus_resource_management.courseservice.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryForFilter {

    Page<Course> filterCourse(
            FilterCourseRequest filterCourseRequest,
            Pageable pageable
    );
}
