package com.campus_resource_management.courseservice.controller;

import com.campus_resource_management.courseservice.dto.PaginationResponse;
import com.campus_resource_management.courseservice.dto.ServiceResponse;
import com.campus_resource_management.courseservice.dto.course.request.AddCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.FilterCourseRequest;
import com.campus_resource_management.courseservice.dto.course.request.UpdateCourseRequest;
import com.campus_resource_management.courseservice.dto.course.response.DetailedCourseResponse;
import com.campus_resource_management.courseservice.dto.course.response.SummaryCourseResponse;
import com.campus_resource_management.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<SummaryCourseResponse> addCourse(
            @RequestBody @Valid AddCourseRequest addCourseRequest) {
        return courseService.addCourse(addCourseRequest);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<SummaryCourseResponse> updateCourse(
            @RequestBody @Valid UpdateCourseRequest updateCourseRequest) {
        return courseService.updateCourse(updateCourseRequest);
    }

    @GetMapping("/viewAll")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<PaginationResponse<SummaryCourseResponse>> viewAllCourses(
            @Valid FilterCourseRequest filterCourseRequest) {
        return courseService.viewFilteredCourses(filterCourseRequest);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<DetailedCourseResponse> viewDetailedCourseByCourseCode(
            @RequestParam String courseCode) {
        return courseService.viewDetailedCourseByCourseCode(courseCode);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> deleteCourse(@RequestParam String courseCode) {
        return courseService.deleteCourseByCourseCode(courseCode);
    }

    @PutMapping("/restore")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> restoreCourse(@RequestParam String courseCode) {
        return courseService.restoreCourseByCourseCode(courseCode);
    }
}
