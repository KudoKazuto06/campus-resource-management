package com.campus_resource_management.courseservice.controller;

import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.AddCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.FilterCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.request.UpdateCourseEnrollmentRequest;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.DetailedCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.dto.course_enrollment.response.SummaryCourseEnrollmentResponse;
import com.campus_resource_management.courseservice.service.CourseEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-enrollment")
@RequiredArgsConstructor
public class CourseEnrollmentController {

    private final CourseEnrollmentService courseEnrollmentService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<SummaryCourseEnrollmentResponse> addCourseEnrollment(
            @RequestBody @Valid AddCourseEnrollmentRequest request) {
        return courseEnrollmentService.addCourseEnrollment(request);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<SummaryCourseEnrollmentResponse> updateCourseEnrollment(
            @RequestBody @Valid UpdateCourseEnrollmentRequest request) {
        return courseEnrollmentService.updateCourseEnrollment(request);
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> withdrawCourseEnrollment(
            @RequestParam String offeringCode,
            @RequestParam String studentIdentityId) {
        return courseEnrollmentService.withdrawCourseEnrollment(offeringCode, studentIdentityId);
    }

    @GetMapping("/viewAll")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<PaginationResponse<SummaryCourseEnrollmentResponse>> viewAllCourseEnrollments(
            @Valid FilterCourseEnrollmentRequest filterRequest) {
        return courseEnrollmentService.filterCourseEnrollments(filterRequest);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<DetailedCourseEnrollmentResponse> viewDetailedCourseEnrollment(
            @RequestParam String offeringCode,
            @RequestParam String studentIdentityId) {
        return courseEnrollmentService.viewDetailedCourseEnrollment(offeringCode, studentIdentityId);
    }

}
