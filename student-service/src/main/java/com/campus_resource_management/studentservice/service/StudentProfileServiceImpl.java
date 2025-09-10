package com.campus_resource_management.studentservice.service;

import com.campus_resource_management.studentservice.constant.*;
import com.campus_resource_management.studentservice.dto.ServiceResponse;
import com.campus_resource_management.studentservice.dto.student_profile.request.AddStudentProfileRequest;
import com.campus_resource_management.studentservice.dto.student_profile.response.DetailedStudentProfileResponse;
import com.campus_resource_management.studentservice.dto.student_profile.response.SummaryStudentProfileResponse;
import com.campus_resource_management.studentservice.entity.StudentProfile;
import com.campus_resource_management.studentservice.mapper.StudentProfileMapper;
import com.campus_resource_management.studentservice.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository profileRepository;
    private final StudentProfileMapper studentProfileMapper;

    @Override
    public ServiceResponse<SummaryStudentProfileResponse> addStudentProfile(AddStudentProfileRequest addStudentProfileRequest) {

        // 1. Create new entity and map fields from request
        StudentProfile studentProfile = new StudentProfile();
        studentProfileMapper.addStudentProfileRequestBodyToStudentProfile(addStudentProfileRequest, studentProfile);

        // 2. Set dynamic / generated fields
        studentProfile.setSchoolEmail(
                generateUniqueSchoolEmail(
                        addStudentProfileRequest.getFirstName(),
                        addStudentProfileRequest.getLastName()
                )
        );
        studentProfile.setCreatedBy("SYSTEM");

        // 3. Save entity
        StudentProfile savedStudentProfile = profileRepository.save(studentProfile);

        // 4. Map saved entity to response DTO
        SummaryStudentProfileResponse summaryStudentProfileResponse = studentProfileMapper.toSummaryResponse(savedStudentProfile);

        // 5. Return response wrapped in ServiceResponse
        return ServiceResponse.<SummaryStudentProfileResponse>builder()
                .statusCode(StatusCode.CREATED)
                .status(StatusResponse.SUCCESS)
                .message(MessageResponse.format(MessageResponse.ADD_STUDENT_PROFILE_SUCCESS))
                .data(summaryStudentProfileResponse)
                .build();
    }



    public String generateUniqueSchoolEmail(String firstName, String lastName){
        String baseEmail =
                (firstName.replaceAll("\\s+", "") + "."
                        + lastName.replaceAll("\\s+", "")).toLowerCase();
        String email = baseEmail + "@school.com";
        int counter = 1;
        
        while (profileRepository.findBySchoolEmail(email).isPresent()) {
            email = baseEmail + counter + "@school.com";
            counter++;
        }
        return email;
    }

}
