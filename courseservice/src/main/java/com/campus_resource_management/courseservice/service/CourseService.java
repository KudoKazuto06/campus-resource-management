package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course.request.AddCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.FilterCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.UpdateCourseRequest;
import com.campus_resource_management.courseservice.dto.course.response.DetailedCourseResponse;
import com.campus_resource_management.courseservice.dto.course.response.SummaryCourseResponse;
import jakarta.validation.Valid;

public interface CourseService {

    ServiceResponse<SummaryCourseResponse> addCourse( @Valid AddCourseRequest addCourseRequest);

    ServiceResponse<SummaryCourseResponse> updateCourse( @Valid UpdateCourseRequest updateCourseRequest);

    ServiceResponse<PaginationResponse<SummaryCourseResponse>> viewFilteredCourses( @Valid FilterCourseRequest filterCourseRequest);

    ServiceResponse<DetailedCourseResponse> viewDetailedCourseByCourseCode(String courseCode);

    ServiceResponse<Void> deleteCourseByCourseCode(String courseCode);

    ServiceResponse<Void> restoreCourseByCourseCode(String courseCode);
}
