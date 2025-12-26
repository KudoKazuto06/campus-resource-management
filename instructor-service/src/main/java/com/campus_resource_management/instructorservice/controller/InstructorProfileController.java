package com.campus_resource_management.instructorservice.controller;

import com.campus_resource_management.instructorservice.dto.PaginationResponse;
import com.campus_resource_management.instructorservice.dto.ServiceResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.AddInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.FilterInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.request.UpdateInstructorProfileRequest;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.DetailedInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.FilterInstructorProfileResponse;
import com.campus_resource_management.instructorservice.dto.instructor_profile.response.SummaryInstructorProfileResponse;
import com.campus_resource_management.instructorservice.service.InstructorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instructor-profile")
@RequiredArgsConstructor
public class InstructorProfileController {

    private final InstructorProfileService instructorProfileService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceResponse<SummaryInstructorProfileResponse> addInstructorProfile(
            @RequestBody @Valid AddInstructorProfileRequest addInstructorProfileRequest) {
        return instructorProfileService.addInstructorProfile(addInstructorProfileRequest);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<SummaryInstructorProfileResponse> updateInstructorProfile(
            @RequestBody @Valid UpdateInstructorProfileRequest updateInstructorProfileRequest) {
        return instructorProfileService.updateInstructorProfile(updateInstructorProfileRequest);
    }

    @GetMapping("/viewAll")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<PaginationResponse<FilterInstructorProfileResponse>> viewAllInstructorProfiles(
            @Valid FilterInstructorProfileRequest filterInstructorProfileRequest) {
        return instructorProfileService.viewFilteredInstructorProfile(
                filterInstructorProfileRequest);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<DetailedInstructorProfileResponse> viewDetailedInstructorProfileByIdentityId(
            @RequestParam String identityId) {
        return instructorProfileService.viewDetailedInstructorProfileByIdentityId(identityId);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> deleteInstructorProfile(@RequestParam String identityId) {
        return instructorProfileService.deleteInstructorProfileByIdentityId(identityId);
    }

    @PutMapping("/restore")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse<Void> restoreInstructorProfile(@RequestParam String identityId) {
        return instructorProfileService.restoreInstructorProfile(identityId);
    }
}
