package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.AddCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.FilterCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.UpdateCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.DetailedCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.SummaryCourseEnrollmentResponse;
import jakarta.validation.Valid;

public interface CourseEnrollmentService {

    ServiceResponse<SummaryCourseEnrollmentResponse> addCourseEnrollment( @Valid AddCourseEnrollmentRequest request);

    ServiceResponse<SummaryCourseEnrollmentResponse> updateCourseEnrollment( @Valid UpdateCourseEnrollmentRequest request);

    ServiceResponse<PaginationResponse<SummaryCourseEnrollmentResponse>> filterCourseEnrollments( @Valid FilterCourseEnrollmentRequest filterRequest);

    ServiceResponse<Void> withdrawCourseEnrollment(String offeringCode, String studentIdentityId);

    ServiceResponse<DetailedCourseEnrollmentResponse> viewDetailedCourseEnrollment(String offeringCode, String studentIdentityId);


}
