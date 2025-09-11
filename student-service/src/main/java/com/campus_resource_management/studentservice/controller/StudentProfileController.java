package com.campus_resource_management.studentservice.controller;

import com.campus_resource_management.studentservice.dto.PaginationResponse;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.UpdateStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import com.campus_resource_management.studentservice.service.StudentProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-profile")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<SummaryStudentProfileResponse> addStudentProfile(
            @RequestBody @Valid AddStudentProfileRequest  addStudentProfileRequest) {
        return studentProfileService.addStudentProfile(addStudentProfileRequest);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<SummaryStudentProfileResponse> updateStudentProfile(
            @RequestBody @Valid UpdateStudentProfileRequest updateStudentProfileRequest) {
        return studentProfileService.updateStudentProfile(updateStudentProfileRequest);
    }

    @GetMapping("/viewAll")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<PaginationResponse> viewAllStudentProfile(
            @Valid FilterStudentProfileRequest filterStudentProfileRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return studentProfileService.viewFilteredStudentProfile(filterStudentProfileRequest);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<DetailedStudentProfileResponse> viewDetailedStudentProfileByIdentityId(
            @RequestParam String identityId) {
        return studentProfileService.viewDetailedStudentProfileByIdentityId(identityId);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ServiceResponse<Void> deleteStudentProfile(@RequestParam String identityId) {
        return studentProfileService.deleteStudentProfileByIdentityId(identityId);
    }

    @PutMapping("/restore")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> restoreStudentProfile(@RequestParam String identityId) {
        return studentProfileService.restoreStudentProfile(identityId);
    }


}
