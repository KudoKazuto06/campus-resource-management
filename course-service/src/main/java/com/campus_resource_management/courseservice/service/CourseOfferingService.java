package com.campus_resource_management.courseservice.service;

import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import jakarta.validation.Valid;

public interface CourseOfferingService {

    ServiceResponse<SummaryCourseOfferingResponse> addCourseOffering( @Valid AddCourseOfferingRequest request);

    ServiceResponse<SummaryCourseOfferingResponse> updateCourseOffering( @Valid UpdateCourseOfferingRequest request);

    ServiceResponse<PaginationResponse<SummaryCourseOfferingResponse>> filterCourseOfferings( @Valid FilterCourseOfferingRequest filterRequest);

    ServiceResponse<DetailedCourseOfferingResponse> viewDetailedCourseOfferingByCode(String courseOfferingCode);

    ServiceResponse<Void> deleteCourseOfferingByOfferingCode(String courseOfferingCode);

    ServiceResponse<Void> restoreCourseOfferingByOfferingCode(String courseOfferingCode);

}


