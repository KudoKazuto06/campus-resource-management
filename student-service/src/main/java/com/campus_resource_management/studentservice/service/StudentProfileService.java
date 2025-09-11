package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.dto.PaginationResponse;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.FilterStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.request.UpdateStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

public interface StudentProfileService {

    ServiceResponse<SummaryStudentProfileResponse>
    addStudentProfile(@Valid AddStudentProfileRequest addStudentProfileRequest);

    ServiceResponse<SummaryStudentProfileResponse>
    updateStudentProfile(@Valid UpdateStudentProfileRequest updateStudentProfileRequest);

    ServiceResponse<PaginationResponse>
    viewFilteredStudentProfile(@Valid FilterStudentProfileRequest filterStudentProfileRequest);

    ServiceResponse<DetailedStudentProfileResponse>
    viewDetailedStudentProfileByIdentityId(String identityId);

    ServiceResponse<Void>
    deleteStudentProfileByIdentityId(String identityId);

    ServiceResponse<Void>
    restoreStudentProfile(String identityId);

}
