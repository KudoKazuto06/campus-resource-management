package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.entity.StudentProfile;

public interface StudentProfileService {

    ServiceResponse<StudentProfile> addStudentProfile(AddStudentProfileRequest addStudentProfileRequest);

}
