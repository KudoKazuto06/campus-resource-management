package com.campus_resource_management.courseservice.controller;

import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course_offering.request.AddCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.FilterCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.request.UpdateCourseOfferingRequest;
import com.campus_resource_management.courseservice.dto.course_offering.response.DetailedCourseOfferingResponse;
import com.campus_resource_management.courseservice.dto.course_offering.response.SummaryCourseOfferingResponse;
import com.campus_resource_management.courseservice.service.CourseOfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-offering")
@RequiredArgsConstructor
public class CourseOfferingController {

    private final CourseOfferingService courseOfferingService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<SummaryCourseOfferingResponse> addCourseOffering(
            @RequestBody @Valid AddCourseOfferingRequest request) {
        return courseOfferingService.addCourseOffering(request);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<SummaryCourseOfferingResponse> updateCourseOffering(
            @RequestBody @Valid UpdateCourseOfferingRequest request) {
        return courseOfferingService.updateCourseOffering(request);
    }

    @GetMapping("/viewAll")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<PaginationResponse<SummaryCourseOfferingResponse>> viewAllCourseOfferings(
            @Valid FilterCourseOfferingRequest filterRequest) {
        return courseOfferingService.filterCourseOfferings(filterRequest);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<DetailedCourseOfferingResponse> viewDetailedCourseOffering(
            @RequestParam String courseOfferingCode) {
        return courseOfferingService.viewDetailedCourseOfferingByCode(courseOfferingCode);
    }


    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> deleteCourseOffering(@RequestParam String courseOfferingCode) {
        return courseOfferingService.deleteCourseOfferingByOfferingCode(courseOfferingCode);
    }

    @PutMapping("/restore")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> restoreCourseOffering(@RequestParam String courseOfferingCode) {
        return courseOfferingService.restoreCourseOfferingByOfferingCode(courseOfferingCode);
    }

}
