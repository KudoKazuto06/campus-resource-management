package com.campus_resource_management.studentservice.controller;

import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
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
    public ServiceResponse<SummaryStudentProfileResponse> addStudentProfile(@RequestBody @Valid AddStudentProfileRequest  addStudentProfileRequest) {
        return studentProfileService.addStudentProfile(addStudentProfileRequest);
    }


}
